package id.gits.football.team

import id.gits.football.BasePresenter
import id.gits.football.BaseView
import id.gits.football.data.Team

/**
 * This specifies the contract between the view and the presenter.
 */
interface TeamContract {
    interface View : BaseView<Presenter> {
        fun showTeam(team: Team)
        fun showLoading()
        fun hideLoading()
        fun showAddFavoriteSuccess()
        fun showRemoveFavoriteSuccess()
        fun showToggleFavoriteError()
        fun invalidateMenu()
    }

    interface Presenter : BasePresenter {
        fun getTeam()
        fun isFavorite(): Boolean
        fun addToFavorite()
        fun removeFromFavorite()
    }
}