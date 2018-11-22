package id.gits.football.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.annotation.VisibleForTesting
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.test.espresso.IdlingResource
import id.gits.football.Injection
import id.gits.football.R
import id.gits.football.main.matches.MatchesPagerFragment
import id.gits.football.main.teams.TeamsContract
import id.gits.football.main.teams.TeamsFragment
import id.gits.football.main.teams.TeamsPresenter
import id.gits.football.utils.EspressoIdlingResource
import id.gits.football.utils.SearchEvent
import id.gits.football.utils.replaceFragmentInActivity
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.*
import org.jetbrains.anko.design.bottomNavigationView


class MainActivity : AppCompatActivity() {

    private val menuMatch = 1
    private val menuTeam = 2
    private val menuFav = 3

    private lateinit var searchView: SearchView
    private lateinit var bottomNavigationView: com.google.android.material.bottomnavigation.BottomNavigationView

    private var selectedFragment: androidx.fragment.app.Fragment? = null

    val countingIdlingResource: IdlingResource
        @VisibleForTesting
        get() = EspressoIdlingResource.countingIdlingResource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainActivityUI().setContentView(this)

        initBottomBar()

        // Verify the action and get the query
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                doMySearch(query)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        // Associate searchable configuration with the SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu?.findItem(R.id.search)?.actionView as SearchView
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(componentName))
        searchView.maxWidth = Integer.MAX_VALUE

        searchView.setOnCloseListener {
            bottomNavigationView.visibility = View.VISIBLE
            doMySearch("")
            false
        }

        searchView.setOnQueryTextFocusChangeListener { v, hasFocus ->
            run {
                if (hasFocus) {
                    bottomNavigationView.visibility = View.GONE
                }
            }
        }

        // listening to search query text change
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // filter recycler view when query submitted
                doMySearch(query)
                return false
            }


            override fun onQueryTextChange(query: String): Boolean {
                // filter recycler view when text is changed
                doMySearch(query)
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.search -> {
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified) {
            searchView.isIconified = true
            return
        }
        super.onBackPressed()
    }

    private fun doMySearch(query: String) {
        val type = when (selectedFragment) {
            is MatchesPagerFragment -> SearchEvent.TYPE_MATCH
            is TeamsFragment -> SearchEvent.TYPE_TEAM
            else -> SearchEvent.TYPE_FAV
        }
        EventBus.getDefault().post(SearchEvent(query, type))
    }

    private fun initBottomBar() {
        with(findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(MainActivityUI.navigationView)) {
            bottomNavigationView = this
            menu.add(Menu.NONE, menuMatch, Menu.NONE, R.string.menu_match).setIcon(R.drawable.ic_baseline_calendar)
            menu.add(Menu.NONE, menuTeam, Menu.NONE, R.string.menu_team).setIcon(R.drawable.ic_soccer_ball)
            menu.add(Menu.NONE, menuFav, Menu.NONE, R.string.menu_fav).setIcon(R.drawable.ic_baseline_favorites)

            setOnNavigationItemSelectedListener {
                selectedFragment = when (it.itemId) {
                    menuMatch -> {
                        MatchesPagerFragment.newInstance()
                    }
                    menuTeam -> {
                        TeamsFragment.newInstance(TeamsContract.TYPE.NORMAL).apply {
                            TeamsPresenter(Injection.provideSportsRepository(ctx), this)
                        }
                    }
                    menuFav -> {
                        FavoritesPagerFragment.newInstance()
                    }
                    else -> null
                }

                selectedFragment?.let { it1 -> replaceFragmentInActivity(it1, MainActivityUI.contentFrame) }
                true
            }

            // create initial fragment for the first time only
            selectedFragment = MatchesPagerFragment.newInstance().apply {
                replaceFragmentInActivity(this, MainActivityUI.contentFrame)
            }
        }
    }

    class MainActivityUI : AnkoComponent<MainActivity> {
        companion object {
            const val contentFrame = 1
            const val navigationView = 2
        }

        override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
            linearLayout {
                orientation = LinearLayout.VERTICAL

                frameLayout {
                    id = contentFrame
                }.lparams(width = LinearLayout.LayoutParams.MATCH_PARENT, height = 0, weight = 1F)

                bottomNavigationView {
                    id = navigationView
                    backgroundColorResource = R.color.colorPrimary

                    val states = arrayOf(
                            intArrayOf(android.R.attr.state_selected),
                            intArrayOf(android.R.attr.state_enabled)
                    )

                    val colors = intArrayOf(Color.WHITE, Color.DKGRAY)

                    val colorStateList = ColorStateList(states, colors)
                    itemTextColor = colorStateList
                    itemIconTintList = colorStateList

                }.lparams(width = LinearLayout.LayoutParams.MATCH_PARENT)
            }
        }

    }

    enum class TYPE {
        PAST, NEXT, FAV
    }

}

