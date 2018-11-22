package id.gits.football

import android.content.Context
import id.gits.football.data.source.SportsDataSource
import id.gits.football.data.source.SportsRepository
import id.gits.football.data.source.local.SportsLocalDataSource
import id.gits.football.data.source.remote.SportsRemoteDataSource


/**
 * Enables injection of production implementations for
 * [SportsDataSource] at compile time.
 */
object Injection {
    fun provideSportsRepository(ctx:Context): SportsRepository {
        return SportsRepository.getInstance(SportsLocalDataSource.getInstance(ctx),
                SportsRemoteDataSource.getInstance())
    }
}
