package xyz.mperminov.tfscoursework

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.room.Room
import xyz.mperminov.tfscoursework.di.AppComponent
import xyz.mperminov.tfscoursework.di.AppModule
import xyz.mperminov.tfscoursework.di.DaggerAppComponent
import xyz.mperminov.tfscoursework.fragments.courses.lectures.di.DaggerLecturesComponent
import xyz.mperminov.tfscoursework.fragments.courses.lectures.di.LecturesComponent
import xyz.mperminov.tfscoursework.fragments.courses.lectures.di.LecturesModule
import xyz.mperminov.tfscoursework.network.AuthHolder
import xyz.mperminov.tfscoursework.repositories.lectures.db.HomeworkDatabase
import xyz.mperminov.tfscoursework.repositories.students.StudentsRepository
import xyz.mperminov.tfscoursework.repositories.user.UserRepository
import xyz.mperminov.tfscoursework.repositories.user.network.UserNetworkRepository
import xyz.mperminov.tfscoursework.repositories.user.prefs.SharedPrefUserRepository
import java.util.concurrent.TimeUnit

class TFSCourseWorkApp : Application(), AuthHolder.PrefsProvider, UserNetworkRepository.TokenProvider,
    StudentsRepository.UpdateTimeSaver {

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().application(this).plus(AppModule).build()
        initUserRepositoryInstance(applicationContext)
        initHomeworkDatabase(applicationContext)
        initAuthHolder(this, this)
        initUserNetworkRepository(this)
        initStudentsRepository(this, this)
    }

    fun initLecturesComponent() {
        lecturesComponent = DaggerLecturesComponent.builder().plus(appComponent).plus(LecturesModule).build()
    }

    override fun getPreferences(): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(this)
    }

    override fun getToken(): String? {
        return getPreferences().getString(AuthHolder.AUTH_TOKEN_ARG, null)
    }

    override fun saveUpdateTime(timestamp: Long) {
        getPreferences().edit().putLong(ARG_TIME_UPDATE, timestamp).apply()
    }

    override fun getTimeDiffInSeconds(currentTime: Long): Long {
        val lastTimeUpdate = getPreferences().getLong(ARG_TIME_UPDATE, 0)
        return TimeUnit.MILLISECONDS.toSeconds(currentTime - lastTimeUpdate)
    }

    companion object {
        @JvmStatic
        lateinit var appComponent: AppComponent
        @JvmStatic
        lateinit var lecturesComponent: LecturesComponent
        lateinit var repository: UserRepository
        lateinit var database: HomeworkDatabase
        lateinit var authHolder: AuthHolder
        lateinit var userNetworkRepository: UserNetworkRepository
        lateinit var studentsRepository: StudentsRepository
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

        private fun initAuthHolder(
            prefsProvider: AuthHolder.PrefsProvider,
            context: Context
        ) {
            authHolder = AuthHolder(PreferenceManager.getDefaultSharedPreferences(context))
        }

        private fun initUserNetworkRepository(tokenProvider: UserNetworkRepository.TokenProvider) {
            userNetworkRepository = UserNetworkRepository(tokenProvider)
        }

        fun initStudentsRepository(
            tokenProvider: UserNetworkRepository.TokenProvider,
            updateTimeSaver: StudentsRepository.UpdateTimeSaver
        ) {
            studentsRepository = StudentsRepository(tokenProvider, updateTimeSaver)
        }

        private const val DATABASE_NAME = "lectures.db"
        private const val ARG_TIME_UPDATE = "last_time_updated"
    }
}