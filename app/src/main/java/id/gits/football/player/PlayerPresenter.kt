package id.gits.football.player

import android.os.Bundle
import id.gits.football.data.Player

class PlayerPresenter(
        private val view: PlayerContract.View) : PlayerContract.Presenter {

    override lateinit var player: Player

    init {
        view.presenter = this
    }

    override fun start() {
        getPlayer()
    }

    override fun setPlayerFromBundle(bundle: Bundle?) {
        this.player = bundle?.get(PlayerFragment.ARGUMENT_PLAYER) as Player
    }

    override fun getPlayer() {
        view.showPlayer(player)
    }

}