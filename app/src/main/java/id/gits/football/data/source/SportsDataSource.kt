package id.gits.football.data.source

import id.gits.football.data.League
import id.gits.football.data.Team
import id.gits.football.data.Match
import id.gits.football.data.Player

interface SportsDataSource {
    interface BaseCallback {

        fun onError(message: String?)
    }

    interface LoadMatchesCallback : BaseCallback {
        fun onMatchesLoaded(matches: List<Match>)
    }

    interface LoadTeamsCallback : BaseCallback {
        fun onTeamsLoaded(teams: List<Team>)
    }

    interface LoadPlayersCallback : BaseCallback {
        fun onPlayersLoaded(players: List<Player>)
    }

    interface LoadLeaguesCallback : BaseCallback {
        fun onLeaguesLoaded(leagues: List<League>)
    }

    interface GetTeamCallback : BaseCallback {
        fun onTeamLoaded(team: Team)
    }

    interface ToggleFavoriteMatchCallback : BaseCallback {
        fun onToggleSuccess(match: Match, isFavoritedNow: Boolean)
    }

    interface ToggleFavoriteTeamCallback : BaseCallback {
        fun onToggleSuccess(team: Team, isFavoritedNow: Boolean)
    }

    interface CheckFavoriteCallback : BaseCallback {
        fun onCheckedFavorited(isFavoritedNow: Boolean)
    }

    fun getLastMatches(leagueId: String, callback: LoadMatchesCallback)

    fun getNextMatches(leagueId: String, callback: LoadMatchesCallback)

    fun getLeagues(callback: LoadLeaguesCallback)

    fun getTeams(leagueId: String, callback: LoadTeamsCallback)

    fun getTeam(clubId: String, callback: GetTeamCallback)

    fun getPlayers(teamId: String, callback: LoadPlayersCallback)

    fun getFavoriteMatches(callback: LoadMatchesCallback)

    fun saveFavoriteMatch(match: Match, callback: ToggleFavoriteMatchCallback)

    fun getFavoriteTeams(callback: LoadTeamsCallback)

    fun saveFavoriteTeam(team: Team, callback: ToggleFavoriteTeamCallback)

    fun deleteFavoriteMatch(match: Match, callback: ToggleFavoriteMatchCallback)

    fun deleteFavoriteTeam(team: Team, callback: ToggleFavoriteTeamCallback)

    fun deleteAllFavorites()

    fun isFavoritedMatch(match: Match, callback: CheckFavoriteCallback)

    fun isFavoritedTeam(team: Team, callback: CheckFavoriteCallback)
}
