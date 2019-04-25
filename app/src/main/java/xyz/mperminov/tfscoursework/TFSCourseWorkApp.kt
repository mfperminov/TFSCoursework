package xyz.mperminov.tfscoursework

import android.app.Application
import android.content.Context
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
import xyz.mperminov.tfscoursework.repositories.user.UserRepository
import xyz.mperminov.tfscoursework.repositories.user.prefs.SharedPrefUserRepository

class TFSCourseWorkApp : Application() {

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().application(this).plus(AppModule).build()
        initUserRepositoryInstance(applicationContext)
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
        private fun initUserRepositoryInstance(context: Context) {
            repository = SharedPrefUserRepository(context)
        }
    }
}