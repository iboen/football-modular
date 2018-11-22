package id.gits.football.data.source

import id.gits.football.data.Match
import id.gits.football.data.Team
import id.gits.football.data.source.local.SportsLocalDataSource
import id.gits.football.data.source.remote.SportsRemoteDataSource

open class SportsRepository(
        private val sportsLocalDataSource: SportsDataSource,
        private val sportsRemoteDataSource: SportsDataSource
) : SportsDataSource {
    override fun getLastMatches(leagueId: String, callback: SportsDataSource.LoadMatchesCallback) {
        sportsRemoteDataSource.getLastMatches(leagueId, callback)
    }

    override fun getNextMatches(leagueId: String, callback: SportsDataSource.LoadMatchesCallback) {
        sportsRemoteDataSource.getNextMatches(leagueId, callback)
    }

    override fun getLeagues(callback: SportsDataSource.LoadLeaguesCallback) {
        sportsRemoteDataSource.getLeagues(callback)
    }

    override fun getTeams(leagueId: String, callback: SportsDataSource.LoadTeamsCallback) {
        sportsRemoteDataSource.getTeams(leagueId, callback)
    }

    override fun getTeam(clubId: String, callback: SportsDataSource.GetTeamCallback) {
        sportsRemoteDataSource.getTeam(clubId, callback)
    }

    override fun getPlayers(teamId: String, callback: SportsDataSource.LoadPlayersCallback) {
        sportsRemoteDataSource.getPlayers(teamId, callback)
    }

    override fun isFavoritedMatch(match: Match, callback: SportsDataSource.CheckFavoriteCallback) {
        sportsLocalDataSource.isFavoritedMatch(match, callback)
    }

    override fun isFavoritedTeam(team: Team, callback: SportsDataSource.CheckFavoriteCallback) {
        sportsLocalDataSource.isFavoritedTeam(team, callback)
    }

    override fun deleteAllFavorites() {
        sportsLocalDataSource.deleteAllFavorites()
    }

    override fun saveFavoriteMatch(match: Match, callback: SportsDataSource.ToggleFavoriteMatchCallback) {
        sportsLocalDataSource.saveFavoriteMatch(match, callback)
    }

    override fun deleteFavoriteMatch(match: Match, callback: SportsDataSource.ToggleFavoriteMatchCallback) {
        sportsLocalDataSource.deleteFavoriteMatch(match, callback)
    }

    override fun deleteFavoriteTeam(team: Team, callback: SportsDataSource.ToggleFavoriteTeamCallback) {
        sportsLocalDataSource.deleteFavoriteTeam(team, callback)
    }

    override fun getFavoriteMatches(callback: SportsDataSource.LoadMatchesCallback) {
        sportsLocalDataSource.getFavoriteMatches(callback)
    }

    override fun getFavoriteTeams(callback: SportsDataSource.LoadTeamsCallback) {
        sportsLocalDataSource.getFavoriteTeams(callback)
    }

    override fun saveFavoriteTeam(team: Team, callback: SportsDataSource.ToggleFavoriteTeamCallback) {
        sportsLocalDataSource.saveFavoriteTeam(team, callback)
    }

    companion object {

        private var INSTANCE: SportsRepository? = null

        /**
         * Returns the single instance of this class, creating it if necessary.
         * @param sportsLocalDataSource the local data source
         * @param sportsRemoteDataSource the backend data source
         * *
         * @return the [SportsRepository] instance
         */
        @JvmStatic
        fun getInstance(sportsLocalDataSource: SportsLocalDataSource, sportsRemoteDataSource: SportsRemoteDataSource): SportsRepository {
            return INSTANCE ?: SportsRepository(sportsLocalDataSource, sportsRemoteDataSource)
                    .apply { INSTANCE = this }
        }

        /**
         * Used to force [getInstance] to create a new instance
         * next time it's called.
         */
        @JvmStatic
        fun destroyInstance() {
            INSTANCE = null
        }
    }

}