package id.gits.football.team

import id.gits.football.data.Team
import id.gits.football.data.source.SportsDataSource
import id.gits.football.data.source.SportsRepository

class TeamPresenter(
        private val team: Team,
        private val sportsRepository: SportsRepository,
        private val view: TeamContract.View) : TeamContract.Presenter {

    private var isFavorite: Boolean = false

    init {
        view.presenter = this
    }

    override fun start() {
        getTeam()
        checkIsFavorite()
    }

    override fun getTeam() {
        view.showLoading()
        sportsRepository.getTeam(team.idTeam, object : SportsDataSource.GetTeamCallback {
            override fun onError(message: String?) {
                view.hideLoading()
                view.showError(message)
            }

            override fun onTeamLoaded(team: Team) {
                view.hideLoading()
                view.showTeam(team)
            }

        })
    }

    private fun checkIsFavorite() {
        sportsRepository.isFavoritedTeam(team, object : SportsDataSource.CheckFavoriteCallback {
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
        sportsRepository.deleteFavoriteTeam(team, object : SportsDataSource.ToggleFavoriteTeamCallback {
            override fun onError(message: String?) {
                view.showToggleFavoriteError()
            }

            override fun onToggleSuccess(team: Team, isFavoritedNow: Boolean) {
                view.showRemoveFavoriteSuccess()
                isFavorite = isFavoritedNow
                view.invalidateMenu()
            }

        })
    }

    override fun addToFavorite() {
        sportsRepository.saveFavoriteTeam(team, object : SportsDataSource.ToggleFavoriteTeamCallback {
            override fun onError(message: String?) {
                view.showToggleFavoriteError()
            }

            override fun onToggleSuccess(team: Team, isFavoritedNow: Boolean) {
                view.showAddFavoriteSuccess()
                isFavorite = isFavoritedNow
                view.invalidateMenu()
            }

        })
    }
}