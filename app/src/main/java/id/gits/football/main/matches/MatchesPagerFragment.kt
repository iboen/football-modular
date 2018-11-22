package id.gits.football.main.matches

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.gits.football.Injection
import id.gits.football.main.MainActivity
import org.jetbrains.anko.*
import org.jetbrains.anko.design.tabLayout
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.viewPager
import id.gits.football.R

class MatchesPagerFragment : androidx.fragment.app.Fragment() {
    private lateinit var viewPager: androidx.viewpager.widget.ViewPager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = SchedulePagerFragmentUI().createView(AnkoContext.create(ctx, this))

        with(view.findViewById<androidx.viewpager.widget.ViewPager>(SchedulePagerFragmentUI.viewPagerId)) {
            viewPager = this
            adapter = PagerAdapter(ctx, activity?.supportFragmentManager)
        }

        with(view.findViewById<com.google.android.material.tabs.TabLayout>(SchedulePagerFragmentUI.tabLayoutId)) {
            setupWithViewPager(viewPager)
        }

        return view
    }

    companion object {

        fun newInstance() = MatchesPagerFragment()
    }

    class SchedulePagerFragmentUI : AnkoComponent<androidx.fragment.app.Fragment> {
        companion object {
            const val tabLayoutId = 1
            const val viewPagerId = 2
        }

        override fun createView(ui: AnkoContext<androidx.fragment.app.Fragment>) = with(ui) {
            verticalLayout {
                tabLayout {
                    id = SchedulePagerFragmentUI.tabLayoutId
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
                    id = SchedulePagerFragmentUI.viewPagerId
                }.lparams(matchParent, matchParent)
            }
        }
    }

    class PagerAdapter(val ctx: Context, fm: androidx.fragment.app.FragmentManager?) : androidx.fragment.app.FragmentStatePagerAdapter(fm) {
        override fun getItem(position: Int): androidx.fragment.app.Fragment {
            val fragment: MatchesFragment = when (position) {
                0 -> MatchesFragment.newInstance(MainActivity.TYPE.PAST)
                else -> MatchesFragment.newInstance(MainActivity.TYPE.NEXT)
            }
            MatchesPresenter(Injection.provideSportsRepository(ctx), fragment)
            return fragment
        }

        override fun getCount(): Int = 2

        override fun getPageTitle(position: Int): CharSequence? {
            return if (position == 0) "Past" else "Next"
        }
    }
}