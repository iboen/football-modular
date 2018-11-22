package id.gits.football.data.source.local

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.google.gson.Gson
import id.gits.football.data.Match
import id.gits.football.data.Team
import id.gits.football.data.source.SportsDataSource
import org.jetbrains.anko.db.*

class SportsLocalDataSource private constructor(
        private val db: MyDatabaseOpenHelper
) : SportsDataSource {
    override fun getLastMatches(leagueId: String, callback: SportsDataSource.LoadMatchesCallback) {
        // not implemented, not stored in local db
    }

    override fun getNextMatches(leagueId: String, callback: SportsDataSource.LoadMatchesCallback) {
        // not implemented, not stored in local db
    }

    override fun getLeagues(callback: SportsDataSource.LoadLeaguesCallback) {
        // not implemented, not stored in local db
    }

    override fun getTeams(leagueId: String, callback: SportsDataSource.LoadTeamsCallback) {
        // not implemented, not stored in local db
    }

    override fun getTeam(clubId: String, callback: SportsDataSource.GetTeamCallback) {
        // not implemented, not stored in local db
    }

    override fun getPlayers(teamId: String, callback: SportsDataSource.LoadPlayersCallback) {
        // not implemented, not stored in local db
    }
    override fun isFavoritedMatch(match: Match, callback: SportsDataSource.CheckFavoriteCallback) {
        db.use {
            select(FavoriteMatchDao.TABLE_FAVORITE_MATCH).whereArgs("${FavoriteMatchDao.MATCH_ID} = {matchId}",
                    "matchId" to match.idEvent).exec {
                callback.onCheckedFavorited(this.count > 0)
            }
        }
    }

    override fun isFavoritedTeam(team: Team, callback: SportsDataSource.CheckFavoriteCallback) {
        db.use {
            select(FavoriteTeamDao.TABLE_FAVORITE_TEAM).whereArgs("${FavoriteTeamDao.TEAM_ID} = {teamId}",
                    "teamId" to team.idTeam).exec {
                callback.onCheckedFavorited(this.count > 0)
            }
        }
    }

    override fun saveFavoriteMatch(match: Match, callback: SportsDataSource.ToggleFavoriteMatchCallback) {
        db.use {
            insert(FavoriteMatchDao.TABLE_FAVORITE_MATCH,
                    FavoriteMatchDao.MATCH_ID to match.idEvent,
                    FavoriteMatchDao.JSON to Gson().toJson(match),
                    FavoriteMatchDao.IS_PAST to if (match.isPast) 0 else 1
            )
            callback.onToggleSuccess(match, true)
        }
    }

    override fun deleteFavoriteMatch(match: Match, callback: SportsDataSource.ToggleFavoriteMatchCallback) {
        db.use {
            delete(FavoriteMatchDao.TABLE_FAVORITE_MATCH, "${FavoriteMatchDao.MATCH_ID} = {matchId}",
                    "matchId" to match.idEvent)
            callback.onToggleSuccess(match, false)
        }
    }

    override fun deleteFavoriteTeam(team: Team, callback: SportsDataSource.ToggleFavoriteTeamCallback) {
        db.use {
            delete(FavoriteTeamDao.TABLE_FAVORITE_TEAM, "${FavoriteTeamDao.TEAM_ID} = {teamId}",
                    "teamId" to team.idTeam)
            callback.onToggleSuccess(team, false)
        }
    }

    override fun deleteAllFavorites() {
        db.use {
            delete(FavoriteMatchDao.TABLE_FAVORITE_MATCH)
        }
    }

    override fun getFavoriteMatches(callback: SportsDataSource.LoadMatchesCallback) {
        db.use {
            select(FavoriteMatchDao.TABLE_FAVORITE_MATCH).exec {
                val matches = this.parseList(MatchRowParser())
                callback.onMatchesLoaded(matches)
            }
        }
    }

    override fun getFavoriteTeams(callback: SportsDataSource.LoadTeamsCallback) {
        db.use {
            select(FavoriteTeamDao.TABLE_FAVORITE_TEAM).exec {
                val teams = this.parseList(TeamRowParser())
                callback.onTeamsLoaded(teams)
            }
        }
    }

    override fun saveFavoriteTeam(team: Team, callback: SportsDataSource.ToggleFavoriteTeamCallback) {
        db.use {
            insert(FavoriteTeamDao.TABLE_FAVORITE_TEAM,
                    FavoriteTeamDao.TEAM_ID to team.idTeam,
                    FavoriteTeamDao.JSON to Gson().toJson(team)
            )
            callback.onToggleSuccess(team, true)
        }
    }

    companion object {
        private var INSTANCE: SportsLocalDataSource? = null

        @JvmStatic
        fun getInstance(ctx: Context): SportsLocalDataSource {
            if (INSTANCE == null) {
                synchronized(SportsLocalDataSource::javaClass) {
                    INSTANCE = SportsLocalDataSource(ctx.database)
                }
            }

            // I am sure it will not be null
            return INSTANCE!!
        }

        @VisibleForTesting
        fun clearInstance() {
            INSTANCE = null
        }
    }


    class MatchRowParser : MapRowParser<Match> {
        override fun parseRow(columns: Map<String, Any?>): Match {
            val json: String? = columns[FavoriteMatchDao.JSON].toString()
            return Gson().fromJson<Match>(json, Match::class.java)
        }

    }

    class TeamRowParser : MapRowParser<Team> {
        override fun parseRow(columns: Map<String, Any?>): Team {
            val json: String? = columns[FavoriteTeamDao.JSON].toString()
            return Gson().fromJson<Team>(json, Team::class.java)
        }

    }

}