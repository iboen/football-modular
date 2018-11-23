package id.gits.football.main

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.gits.football.Injection
import id.gits.football.main.matches.MatchesFragment
import id.gits.football.main.matches.MatchesPresenter
import id.gits.football.main.teams.TeamsContract
import id.gits.football.main.teams.TeamsFragment
import id.gits.football.main.teams.TeamsPresenter
import org.jetbrains.anko.*
import org.jetbrains.anko.design.tabLayout
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.viewPager
import id.gits.football.R

class FavoritesPagerFragment : Fragment() {
    private lateinit var viewPager: ViewPager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = FavoritesPagerFragmentUI().createView(AnkoContext.create(ctx, this))

        with(view.findViewById<ViewPager>(FavoritesPagerFragmentUI.viewPagerId)) {
            viewPager = this
            adapter = PagerAdapter(ctx, activity?.supportFragmentManager)
        }

        with(view.findViewById<TabLayout>(FavoritesPagerFragmentUI.tabLayoutId)) {
            setupWithViewPager(viewPager)
        }

        return view
    }

    companion object {

        fun newInstance() = FavoritesPagerFragment()
    }

    class FavoritesPagerFragmentUI : AnkoComponent<Fragment> {
        companion object {
            const val tabLayoutId = 1
            const val viewPagerId = 2
        }

        override fun createView(ui: AnkoContext<Fragment>) = with(ui) {
            verticalLayout {
                tabLayout {
                    id = tabLayoutId

                    setBackgroundResource(R.color.colorDarkRed)
                    val states = arrayOf(
                            intArrayOf(android.R.attr.state_selected),
                            intArrayOf(android.R.attr.state_enabled)
                    )
                    val colors = intArrayOf(Color.WHITE, Color.LTGRAY)
                    val colorStateList = ColorStateList(states, colors)

                    tabTextColors = colorStateList
                }.lparams(matchParent, wrapContent)

                viewPager {
                    id = viewPagerId
                }.lparams(matchParent, matchParent)
            }
        }
    }

    class PagerAdapter(val ctx: Context, fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {

            return when (position) {
                0 -> MatchesFragment.newInstance(MainActivity.TYPE.FAV).also {
                    MatchesPresenter(Injection.provideSportsRepository(ctx), it)

                }
                else -> TeamsFragment.newInstance(TeamsContract.TYPE.FAV).also {
                    TeamsPresenter(Injection.provideSportsRepository(ctx), it)
                }
            }
        }

        override fun getCount(): Int = 2

        override fun getPageTitle(position: Int): CharSequence? {
            return if (position == 0) "Matches" else "Teams"
        }
    }
}