package id.gits.football.team

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import id.gits.football.Injection
import id.gits.football.data.Team
import id.gits.football.utils.replaceFragmentInActivity
import id.gits.football.utils.ActivityHelper
import org.jetbrains.anko.*

class TeamActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TeamActivityUI().setContentView(this)

        intent.getParcelableExtra<Team>(ActivityHelper.Team.EXTRA_TEAM).apply {
            val fragment = TeamFragment.newInstance(this).apply {
                replaceFragmentInActivity(this, TeamActivityUI.contentFrameId)
            }
            TeamPresenter(this, Injection.provideSportsRepository(ctx), fragment)
        }

        title = "Team Detail"
    }

    class TeamActivityUI : AnkoComponent<TeamActivity> {
        companion object {
            const val contentFrameId = 1
        }

        override fun createView(ui: AnkoContext<TeamActivity>) = with(ui) {
            frameLayout {
                id = contentFrameId
            }
        }

    }
}
