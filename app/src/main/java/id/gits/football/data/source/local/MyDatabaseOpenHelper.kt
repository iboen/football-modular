package id.gits.football.data.source.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

class MyDatabaseOpenHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "FootballMatch.db", null, 1) {
    companion object {
        private var instance: MyDatabaseOpenHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): MyDatabaseOpenHelper {
            if (instance == null) {
                instance = MyDatabaseOpenHelper(ctx.applicationContext)
            }
            return instance as MyDatabaseOpenHelper
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Here you create tables
        db.createTable(FavoriteMatchDao.TABLE_FAVORITE_MATCH, true,
                FavoriteMatchDao.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                FavoriteMatchDao.MATCH_ID to INTEGER,

                FavoriteMatchDao.JSON to TEXT,
                FavoriteMatchDao.IS_PAST to INTEGER
        )

        db.createTable(FavoriteTeamDao.TABLE_FAVORITE_TEAM, true,
                FavoriteTeamDao.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                FavoriteTeamDao.TEAM_ID to INTEGER,
                FavoriteTeamDao.JSON to TEXT
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Here you can upgrade tables, as usual
        db.dropTable(FavoriteMatchDao.TABLE_FAVORITE_MATCH, true)
    }
}

// Access property for Context
val Context.database: MyDatabaseOpenHelper
    get() = MyDatabaseOpenHelper.getInstance(applicationContext)