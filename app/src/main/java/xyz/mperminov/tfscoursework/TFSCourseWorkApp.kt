package xyz.mperminov.tfscoursework

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import xyz.mperminov.tfscoursework.di.AppComponent
import xyz.mperminov.tfscoursework.di.AppModule
import xyz.mperminov.tfscoursework.di.DaggerAppComponent
import xyz.mperminov.tfscoursework.fragments.courses.lectures.di.DaggerLecturesComponent
import xyz.mperminov.tfscoursework.fragments.courses.lectures.di.LecturesComponent
import xyz.mperminov.tfscoursework.fragments.courses.lectures.di.LecturesModule
import xyz.mperminov.tfscoursework.fragments.profile.di.DaggerProfileComponent
import xyz.mperminov.tfscoursework.fragments.profile.di.ProfileComponent
import xyz.mperminov.tfscoursework.fragments.profile.di.ProfileModule
import xyz.mperminov.tfscoursework.fragments.students.di.DaggerStudentComponent
import xyz.mperminov.tfscoursework.fragments.students.di.StudentComponent
import xyz.mperminov.tfscoursework.fragments.students.di.StudentModule
import xyz.mperminov.tfscoursework.network.AuthHolder
import xyz.mperminov.tfscoursework.repositories.user.UserRepository
import xyz.mperminov.tfscoursework.repositories.user.prefs.SharedPrefUserRepository

class TFSCourseWorkApp : Application(), AuthHolder.PrefsProvider {

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().application(this).plus(AppModule).build()
        initUserRepositoryInstance(applicationContext)
        initAuthHolder(this, this)
    }

    fun initLecturesComponent() {
        lecturesComponent = DaggerLecturesComponent.builder().plus(appComponent).plus(LecturesModule).build()
    }

    fun initStudentComponent() {
        studentComponent = DaggerStudentComponent.builder().plus(appComponent).plus(StudentModule).build()
    }

    fun initProfileComponent() {
        profileComponent = DaggerProfileComponent.builder().plus(appComponent).plus(ProfileModule).build()
    }

    override fun getPreferences(): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(this)
    }

    companion object {
        @JvmStatic
        lateinit var appComponent: AppComponent
        @JvmStatic
        lateinit var lecturesComponent: LecturesComponent
        @JvmStatic
        lateinit var studentComponent: StudentComponent
        @JvmStatic
        lateinit var profileComponent: ProfileComponent
        lateinit var repository: UserRepository
        lateinit var authHolder: AuthHolder
        private fun initUserRepositoryInstance(context: Context) {
            repository = SharedPrefUserRepository(context)
        }

        private fun initAuthHolder(
            prefsProvider: AuthHolder.PrefsProvider,
            context: Context
        ) {
            authHolder = AuthHolder(PreferenceManager.getDefaultSharedPreferences(context))
        }


    }
}