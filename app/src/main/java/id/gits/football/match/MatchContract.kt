package id.gits.football.match

import id.gits.football.BasePresenter
import id.gits.football.BaseView
import id.gits.football.data.Team

/**
 * This specifies the contract between the view and the presenter.
 */
interface MatchContract {
    interface View : BaseView<Presenter> {
        fun showClubHome(team: Team)
        fun showClubAway(team: Team)
        fun showLoading()
        fun hideLoading()
        fun showAddFavoriteSuccess()
        fun showRemoveFavoriteSuccess()
        fun showToggleFavoriteError()
        fun invalidateMenu()
    }

    interface Presenter : BasePresenter {
        fun getClub()
        fun isFavorite(): Boolean
        fun addToFavorite()
        fun removeFromFavorite()
    }
}