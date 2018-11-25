package id.gits.football.team

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.gits.football.data.Team
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.nestedScrollView

class TeamOverviewFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val team = arguments?.get(ARGUMENT_TEAM) as Team


        return TeamOverviewFragmentUI(team).createView(AnkoContext.create(ctx, this))
    }

    companion object {
        private const val ARGUMENT_TEAM = "TEAM"

        fun newInstance(team: Team) =
                TeamOverviewFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(ARGUMENT_TEAM, team)
                    }
                }
    }

    class TeamOverviewFragmentUI constructor(var team: Team?) : AnkoComponent<Fragment> {
        override fun createView(ui: AnkoContext<Fragment>) = with(ui) {
            nestedScrollView {
                verticalLayout {
                    lparams(matchParent, matchParent)
                    padding = dip(16)

                    gravity = Gravity.CENTER_HORIZONTAL

                    textView {
                        text = team?.strDescriptionEN
                        textSize = 14f
                    }.lparams {
                        gravity = Gravity.CENTER
                    }
                }
            }
        }

    }

}