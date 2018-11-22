package id.gits.football.player

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import id.gits.football.R
import id.gits.football.data.Player
import kotlinx.android.synthetic.main.player_fragment.*
import org.jetbrains.anko.support.v4.toast

class PlayerFragment : androidx.fragment.app.Fragment(), PlayerContract.View {

    override lateinit var presenter: PlayerContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.player_fragment, container, false)

        presenter.setPlayerFromBundle(arguments as Bundle)
        return view
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun showPlayer(player: Player) {
        playerTvWeight.text = player.strWeight
        playerTvHeight.text = player.strHeight
        playerTvDescription.text = player.strDescriptionEN
        playerTvPosition.text = player.strPosition

        if (!player.strFanart1.isNullOrEmpty()) {
            Picasso.get()
                    .load(player.strFanart1)
                    .placeholder(R.color.colorBackgroundGrey)
                    .error(R.color.colorError)
                    .into(playerIvPoster)
        }
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showError(message: String?) {
        message?.let { toast(it) }
    }

    companion object {
        const val ARGUMENT_PLAYER = "PLAYER"

        fun newInstance(player: Player) =
                PlayerFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(ARGUMENT_PLAYER, player)
                    }
                }
    }

}