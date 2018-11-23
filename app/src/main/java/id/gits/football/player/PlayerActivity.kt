package id.gits.football.player

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import id.gits.football.data.Player
import id.gits.football.utils.replaceFragmentInActivity
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.setContentView

class PlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PlayerActivityUI().setContentView(this)

        intent.getParcelableExtra<Player>(EXTRA_PLAYER).apply {
            val fragment = PlayerFragment.newInstance(this).apply {
                replaceFragmentInActivity(this, PlayerActivityUI.contentFrameId)
            }
            PlayerPresenter(fragment)

            title = strPlayer
        }


    }

    companion object {
        const val EXTRA_PLAYER = "PLAYER"
    }

    class PlayerActivityUI : AnkoComponent<PlayerActivity> {
        companion object {
            const val contentFrameId = 1
        }

        override fun createView(ui: AnkoContext<PlayerActivity>) = with(ui) {
            frameLayout {
                id = contentFrameId
            }
        }

    }
}
