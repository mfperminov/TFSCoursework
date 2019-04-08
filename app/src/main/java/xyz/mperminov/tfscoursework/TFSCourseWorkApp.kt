package xyz.mperminov.tfscoursework

import android.app.Application
import android.content.Context
import androidx.room.Room
import xyz.mperminov.tfscoursework.repositories.lectures.db.HomeworkDatabase
import xyz.mperminov.tfscoursework.repositories.user.UserRepository
import xyz.mperminov.tfscoursework.repositories.user.prefs.SharedPrefUserRepository

class TFSCourseWorkApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initUserRepositoryInstance(applicationContext)
        initHomeworkDatabase(applicationContext)
    }

    companion object {
        lateinit var repository: UserRepository
        lateinit var database: HomeworkDatabase
        private fun initUserRepositoryInstance(context: Context) {
            repository = SharedPrefUserRepository(context)
        }

        private fun initHomeworkDatabase(context: Context) {
            database = Room.databaseBuilder(
                context,
                HomeworkDatabase::class.java, DATABASE_NAME
            )
                .build()
        }

        private const val DATABASE_NAME = "lectures.db"
    }
}