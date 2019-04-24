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
import xyz.mperminov.tfscoursework.fragments.students.di.DaggerStudentComponent
import xyz.mperminov.tfscoursework.fragments.students.di.StudentComponent
import xyz.mperminov.tfscoursework.fragments.students.di.StudentModule
import xyz.mperminov.tfscoursework.network.AuthHolder
import xyz.mperminov.tfscoursework.repositories.lectures.db.HomeworkDatabase
import xyz.mperminov.tfscoursework.repositories.user.UserRepository
import xyz.mperminov.tfscoursework.repositories.user.network.UserNetworkRepository
import xyz.mperminov.tfscoursework.repositories.user.prefs.SharedPrefUserRepository

class TFSCourseWorkApp : Application(), AuthHolder.PrefsProvider, UserNetworkRepository.TokenProvider {

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().application(this).plus(AppModule).build()
        initUserRepositoryInstance(applicationContext)
        initHomeworkDatabase(applicationContext)
        initAuthHolder(this, this)
        initUserNetworkRepository(this)
    }

    fun initLecturesComponent() {
        lecturesComponent = DaggerLecturesComponent.builder().plus(appComponent).plus(LecturesModule).build()
    }

    fun initStudentComponent() {
        studentComponent = DaggerStudentComponent.builder().plus(appComponent).plus(StudentModule).build()
    }

    override fun getPreferences(): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(this)
    }

    override fun getToken(): String? {
        return getPreferences().getString(AuthHolder.AUTH_TOKEN_ARG, null)
    }

    companion object {
        @JvmStatic
        lateinit var appComponent: AppComponent
        @JvmStatic
        lateinit var lecturesComponent: LecturesComponent
        @JvmStatic
        lateinit var studentComponent: StudentComponent
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

        private fun initAuthHolder(
            prefsProvider: AuthHolder.PrefsProvider,
            context: Context
        ) {
            authHolder = AuthHolder(PreferenceManager.getDefaultSharedPreferences(context))
        }

        private fun initUserNetworkRepository(tokenProvider: UserNetworkRepository.TokenProvider) {
            userNetworkRepository = UserNetworkRepository(tokenProvider)
        }

        private const val DATABASE_NAME = "lectures.db"
        private const val ARG_TIME_UPDATE = "last_time_updated"
    }
}