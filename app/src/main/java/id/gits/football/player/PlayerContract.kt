package id.gits.football.player

import android.os.Bundle
import id.gits.football.BasePresenter
import id.gits.football.BaseView
import id.gits.football.data.Player

/**
 * This specifies the contract between the view and the presenter.
 */
interface PlayerContract {
    interface View : BaseView<Presenter> {
        fun showPlayer(player: Player)
        fun showLoading()
        fun hideLoading()
    }

    interface Presenter : BasePresenter {
        var player: Player

        fun setPlayerFromBundle(bundle: Bundle?)

        fun getPlayer()
    }
}