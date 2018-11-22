package id.gits.football.players

import id.gits.football.BasePresenter
import id.gits.football.BaseView
import id.gits.football.data.Player
import id.gits.football.data.Team

/**
 * This specifies the contract between the view and the presenter.
 */
interface PlayersContract {

    interface View : BaseView<Presenter> {
        fun showPlayers(players: List<Player>)
        fun showLoading()
        fun hideLoading()
        fun showPlayerDetailUi(player: Player)
    }

    interface Presenter : BasePresenter {
        fun setTeam(team: Team)
        fun getPlayers()
    }
}