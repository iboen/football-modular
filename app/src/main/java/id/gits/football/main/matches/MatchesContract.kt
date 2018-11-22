package id.gits.football.main.matches

import id.gits.football.BasePresenter
import id.gits.football.BaseView
import id.gits.football.data.League
import id.gits.football.data.Match
import id.gits.football.main.MainActivity

/**
 * This specifies the contract between the view and the presenter.
 */
interface MatchesContract {

    interface View : BaseView<Presenter> {
        fun showMatches(matches: List<Match>)
        fun showLeagues(leagues: List<League>)
        fun showLoading()
        fun hideLoading()
        fun showMatchDetailUi(match: Match)
    }

    interface Presenter : BasePresenter {
        fun setType(type: MainActivity.TYPE)
        fun getMatches()
        fun getLeagues()
        fun setLeague(leagueId: String)
    }
}