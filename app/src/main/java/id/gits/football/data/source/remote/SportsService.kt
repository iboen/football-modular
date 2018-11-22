package id.gits.football.data.source.remote

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SportsService {
    @GET("eventspastleague.php")
    fun listPastMatches(@Query("id") leagueId: String): Call<ScheduleDao>

    @GET("eventsnextleague.php")
    fun listNextMatches(@Query("id") leagueId: String): Call<ScheduleDao>

    @GET("lookup_all_teams.php")
    fun listTeams(@Query("id") leagueId: String): Call<TeamDao>

    @GET("lookup_all_players.php")
    fun listPlayers(@Query("id") teamId: String): Call<PlayerDao>

    @GET("lookupteam.php")
    fun teamDetail(@Query("id") clubId: String): Call<TeamDao>

    @GET("all_leagues.php")
    fun listLeagues(): Call<LeaguesDao>
}