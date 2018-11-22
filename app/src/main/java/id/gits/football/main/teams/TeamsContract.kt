package id.gits.football.main.teams

import id.gits.football.BasePresenter
import id.gits.football.BaseView
import id.gits.football.data.League
import id.gits.football.data.Team

/**
 * This specifies the contract between the view and the presenter.
 */
interface TeamsContract {

    interface View : BaseView<Presenter> {
        fun showLeagues(leagues: List<League>)
        fun showTeams(teams: List<Team>)
        fun showLoading()
        fun hideLoading()
        fun showTeamDetailUi(team: Team)
    }

    interface Presenter : BasePresenter {
        fun setType(type: TYPE)
        fun setLeague(leagueId: String)
        fun getLeagues()
        fun getTeams()
    }

    enum class TYPE {
        NORMAL, FAV
    }
}