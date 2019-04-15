package xyz.mperminov.tfscoursework

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.room.Room
import xyz.mperminov.tfscoursework.network.AuthHolder
import xyz.mperminov.tfscoursework.repositories.lectures.db.HomeworkDatabase
import xyz.mperminov.tfscoursework.repositories.user.UserRepository
import xyz.mperminov.tfscoursework.repositories.user.network.UserNetworkRepository
import xyz.mperminov.tfscoursework.repositories.user.prefs.SharedPrefUserRepository

class TFSCourseWorkApp : Application(), AuthHolder.PrefsProvider, UserNetworkRepository.TokenProvider {

    override fun onCreate() {
        super.onCreate()
        initUserRepositoryInstance(applicationContext)
        initHomeworkDatabase(applicationContext)
        initAuthHolder(this)
        initUserNetworkRepository(this)
    }

    override fun getPreferences(): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(this)
    }

    override fun getToken(): String? {
        return getPreferences().getString(AuthHolder.AUTH_TOKEN_ARG, null)
    }

    companion object {
        lateinit var repository: UserRepository
        lateinit var database: HomeworkDatabase
        lateinit var authHolder: AuthHolder
        lateinit var userNetworkRepository: UserNetworkRepository
        private fun initUserRepositoryInstance(context: Context) {
            repository = SharedPrefUserRepository(context)
        }

        private fun initHomeworkDatabase(context: Context) {
            database = Room.databaseBuilder(
                context,
                HomeworkDatabase::class.java, DATABASE_NAME
                // понимаю, что этот метод удалит все данные при изменении версии
                // постараюсь так не делать впредь, настрою миграцию
            ).fallbackToDestructiveMigration()
                .build()
        }

        private fun initAuthHolder(prefsProvider: AuthHolder.PrefsProvider) {
            authHolder = AuthHolder(prefsProvider)
        }

        private fun initUserNetworkRepository(tokenProvider: UserNetworkRepository.TokenProvider) {
            userNetworkRepository = UserNetworkRepository(tokenProvider)
        }

        private const val DATABASE_NAME = "lectures.db"
    }
}