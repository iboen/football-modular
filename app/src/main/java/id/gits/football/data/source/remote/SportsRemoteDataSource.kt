package id.gits.football.data.source.remote

import androidx.annotation.VisibleForTesting
import id.gits.football.BuildConfig
import id.gits.football.data.Match
import id.gits.football.data.Team
import id.gits.football.data.source.SportsDataSource
import id.gits.football.utils.EspressoIdlingResource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SportsRemoteDataSource private constructor(
        private val apiService: SportsService
) : SportsDataSource {
    private fun <T> callback2(success: ((Response<T>) -> Unit)?, failure: ((t: Throwable) -> Unit)? = null): Callback<T> {
        return object : Callback<T> {
            override fun onResponse(call: Call<T>, response: retrofit2.Response<T>) {
                success?.invoke(response)
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                failure?.invoke(t)
            }
        }
    }

    override fun getLastMatches(leagueId: String, callback: SportsDataSource.LoadMatchesCallback) {
        EspressoIdlingResource.increment() // App is busy until further notice

        apiService.listPastMatches(leagueId).enqueue(callback2(
                { r ->
                    if (!EspressoIdlingResource.countingIdlingResource.isIdleNow) {
                        EspressoIdlingResource.decrement() // Set app as idle.
                    }
                    r.body()?.let { it -> callback.onMatchesLoaded(it.events.apply { forEach { it.isPast = true } }) }
                },
                { t ->
                    if (!EspressoIdlingResource.countingIdlingResource.isIdleNow) {
                        EspressoIdlingResource.decrement() // Set app as idle.
                    }
                    callback.onError(t.message)
                }))
    }

    override fun getNextMatches(leagueId: String, callback: SportsDataSource.LoadMatchesCallback) {
        EspressoIdlingResource.increment() // App is busy until further notice
        apiService.listNextMatches(leagueId).enqueue(callback2(
                { r ->
                    if (!EspressoIdlingResource.countingIdlingResource.isIdleNow) {
                        EspressoIdlingResource.decrement() // Set app as idle.
                    }
                    r.body()?.let { callback.onMatchesLoaded(it.events) }
                },
                { t ->
                    if (!EspressoIdlingResource.countingIdlingResource.isIdleNow) {
                        EspressoIdlingResource.decrement() // Set app as idle.
                    }
                    callback.onError(t.message)
                }))
    }

    override fun getLeagues(callback: SportsDataSource.LoadLeaguesCallback) {
        EspressoIdlingResource.increment() // App is busy until further notice
        apiService.listLeagues().enqueue(callback2(
                { r ->
                    if (!EspressoIdlingResource.countingIdlingResource.isIdleNow) {
                        EspressoIdlingResource.decrement() // Set app as idle.
                    }
                    r.body()?.let { it -> callback.onLeaguesLoaded(it.leagues.filter { it.strSport.toLowerCase() == "soccer" }) }
                },
                { t ->
                    if (!EspressoIdlingResource.countingIdlingResource.isIdleNow) {
                        EspressoIdlingResource.decrement() // Set app as idle.
                    }
                    callback.onError(t.message)
                }))
    }

    override fun getTeams(leagueId: String, callback: SportsDataSource.LoadTeamsCallback) {
        EspressoIdlingResource.increment() // App is busy until further notice
        apiService.listTeams(leagueId).enqueue(callback2(
                { r ->
                    if (!EspressoIdlingResource.countingIdlingResource.isIdleNow) {
                        EspressoIdlingResource.decrement() // Set app as idle.
                    }
                    r.body()?.let { it.teams?.let { it1 -> callback.onTeamsLoaded(it1) } }
                },
                { t ->
                    if (!EspressoIdlingResource.countingIdlingResource.isIdleNow) {
                        EspressoIdlingResource.decrement() // Set app as idle.
                    }
                    callback.onError(t.message)
                }))
    }

    override fun getTeam(clubId: String, callback: SportsDataSource.GetTeamCallback) {
        apiService.teamDetail(clubId).enqueue(callback2(
                { r ->
                    r.body()?.let {
                        if (it.teams != null)
                            callback.onTeamLoaded(it.teams[0])
                        else
                            callback.onError("Team not found")
                    }
                },
                { t ->
                    callback.onError(t.message)
                }))
    }

    override fun getPlayers(teamId: String, callback: SportsDataSource.LoadPlayersCallback) {
        EspressoIdlingResource.increment() // App is busy until further notice
        apiService.listPlayers(teamId).enqueue(callback2(
                { r ->
                    if (!EspressoIdlingResource.countingIdlingResource.isIdleNow) {
                        EspressoIdlingResource.decrement() // Set app as idle.
                    }
                    r.body()?.let { it.player?.let { it1 -> callback.onPlayersLoaded(it1) } }
                },
                { t ->
                    if (!EspressoIdlingResource.countingIdlingResource.isIdleNow) {
                        EspressoIdlingResource.decrement() // Set app as idle.
                    }
                    callback.onError(t.message)
                }))
    }


    override fun isFavoritedMatch(match: Match, callback: SportsDataSource.CheckFavoriteCallback) {
        // not implemented, local data source only
    }

    override fun deleteAllFavorites() {
        // not implemented, local data source only
    }

    override fun deleteFavoriteTeam(team: Team, callback: SportsDataSource.ToggleFavoriteTeamCallback) {
        // not implemented, local data source only
    }

    override fun isFavoritedTeam(team: Team, callback: SportsDataSource.CheckFavoriteCallback) {
        // not implemented, local data source only
    }

    override fun getFavoriteMatches(callback: SportsDataSource.LoadMatchesCallback) {
        // not implemented, local data source only
    }

    override fun saveFavoriteMatch(match: Match, callback: SportsDataSource.ToggleFavoriteMatchCallback) {
        // not implemented, local data source only
    }

    override fun getFavoriteTeams(callback: SportsDataSource.LoadTeamsCallback) {
        // not implemented, local data source only
    }

    override fun saveFavoriteTeam(team: Team, callback: SportsDataSource.ToggleFavoriteTeamCallback) {
        // not implemented, local data source only
    }

    override fun deleteFavoriteMatch(match: Match, callback: SportsDataSource.ToggleFavoriteMatchCallback) {
        // not implemented, local data source only
    }

    companion object {
        private var INSTANCE: SportsRemoteDataSource? = null

        @JvmStatic
        fun getInstance(): SportsRemoteDataSource {
            if (INSTANCE == null) {
                synchronized(SportsRemoteDataSource::javaClass) {
                    val retrofit = Retrofit.Builder()
                            .addConverterFactory(GsonConverterFactory.create())
                            .baseUrl(BuildConfig.BASE_API_URL)
                            .build()

                    INSTANCE = SportsRemoteDataSource(retrofit.create(SportsService::class.java))
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

}