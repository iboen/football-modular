package id.gits.football.main.matches

import id.gits.football.data.League
import id.gits.football.data.Match
import id.gits.football.data.source.SportsDataSource
import id.gits.football.data.source.SportsRepository
import id.gits.football.main.MainActivity

class MatchesPresenter(
        private val sportsRepository: SportsRepository,
        private val view: MatchesContract.View
) : MatchesContract.Presenter {
    private lateinit var type: MainActivity.TYPE

    private lateinit var leagueId: String

    init {
        view.presenter = this
    }

    override fun setType(type: MainActivity.TYPE) {
        this.type = type
    }

    override fun getLeagues() {
        sportsRepository.getLeagues(object : SportsDataSource.LoadLeaguesCallback {
            override fun onError(message: String?) {
                view.showError(message)
            }

            override fun onLeaguesLoaded(leagues: List<League>) {
                view.showLeagues(leagues)
            }

        })
    }

    override fun getMatches() {
        view.showLoading()
        val cb = object : SportsDataSource.LoadMatchesCallback {
            override fun onError(message: String?) {
                view.hideLoading()
                view.showError(message)
            }

            override fun onMatchesLoaded(matches: List<Match>) {
                view.hideLoading()
                view.showMatches(matches)
            }

        }

        when (type) {
            MainActivity.TYPE.PAST -> sportsRepository.getLastMatches(leagueId, cb)
            MainActivity.TYPE.NEXT -> sportsRepository.getNextMatches(leagueId, cb)
            MainActivity.TYPE.FAV -> sportsRepository.getFavoriteMatches(cb)
        }
    }

    override fun setLeague(leagueId: String) {
        this.leagueId = leagueId
    }

    override fun start() {
        if (type != MainActivity.TYPE.FAV)
            getLeagues()
        getMatches()
    }

}