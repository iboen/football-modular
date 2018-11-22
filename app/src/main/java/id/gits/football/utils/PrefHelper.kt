package id.gits.football.utils

import android.content.Context
import android.preference.PreferenceManager
import id.gits.football.BuildConfig

class PrefHelper {
    companion object {
        private const val PREF_LEAGUE = "league"

        fun saveLeague(ctx: Context, leagueId: String) {
            PreferenceManager.getDefaultSharedPreferences(ctx).edit()
                    .putString(PREF_LEAGUE, leagueId).apply()
        }

        fun getLeague(ctx: Context): String {
            return PreferenceManager.getDefaultSharedPreferences(ctx).getString(PREF_LEAGUE, BuildConfig.LEAGUE_ID)
                    ?: return BuildConfig.LEAGUE_ID
        }

    }
}