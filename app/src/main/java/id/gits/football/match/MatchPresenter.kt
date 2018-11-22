package id.gits.football.match

import id.gits.football.data.Team
import id.gits.football.data.Match
import id.gits.football.data.source.SportsDataSource
import id.gits.football.data.source.SportsRepository

class MatchPresenter(
        private val match: Match,
        private val sportsRepository: SportsRepository,
        private val view: MatchContract.View) : MatchContract.Presenter {

    private var isFavorite: Boolean = false

    init {
        view.presenter = this
    }

    override fun start() {
        getClub()
        checkIsFavorite()
    }

    override fun getClub() {
        view.showLoading()
        sportsRepository.getTeam(match.idHomeTeam, object : SportsDataSource.GetTeamCallback {
            override fun onError(message: String?) {
                view.hideLoading()
                view.showError(message)
            }

            override fun onTeamLoaded(team: Team) {
                view.hideLoading()
                view.showClubHome(team)
            }

        })

        sportsRepository.getTeam(match.idAwayTeam, object : SportsDataSource.GetTeamCallback {
            override fun onError(message: String?) {
                view.hideLoading()
                view.showError(message)
            }

            override fun onTeamLoaded(team: Team) {
                view.hideLoading()
                view.showClubAway(team)
            }

        })
    }

    private fun checkIsFavorite(){
        sportsRepository.isFavoritedMatch(match, object : SportsDataSource.CheckFavoriteCallback {
            override fun onError(message: String?) {
                view.showError(message)
            }

            override fun onCheckedFavorited(isFavoritedNow: Boolean) {
                isFavorite = isFavoritedNow
                view.invalidateMenu()
            }

        })
    }

    override fun isFavorite(): Boolean {
        return isFavorite
    }

    override fun removeFromFavorite() {
        sportsRepository.deleteFavoriteMatch(match,  object : SportsDataSource.ToggleFavoriteMatchCallback {
            override fun onError(message: String?) {
                view.showToggleFavoriteError()
            }

            override fun onToggleSuccess(match: Match, isFavoritedNow: Boolean) {
                view.showRemoveFavoriteSuccess()
                isFavorite = isFavoritedNow
                view.invalidateMenu()
            }

        })
    }

    override fun addToFavorite() {
        sportsRepository.saveFavoriteMatch(match,  object : SportsDataSource.ToggleFavoriteMatchCallback {
            override fun onError(message: String?) {
                view.showToggleFavoriteError()
            }

            override fun onToggleSuccess(match: Match, isFavoritedNow: Boolean) {
                view.showAddFavoriteSuccess()
                isFavorite = isFavoritedNow
                view.invalidateMenu()
            }

        })
    }
}