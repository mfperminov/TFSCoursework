package xyz.mperminov.tfscoursework

import android.app.Application
import xyz.mperminov.tfscoursework.di.*
import xyz.mperminov.tfscoursework.fragments.courses.lectures.di.DaggerLecturesComponent
import xyz.mperminov.tfscoursework.fragments.courses.lectures.di.LecturesComponent
import xyz.mperminov.tfscoursework.fragments.courses.lectures.di.LecturesModule
import xyz.mperminov.tfscoursework.fragments.students.di.StudentComponent
import xyz.mperminov.tfscoursework.fragments.students.di.StudentModule

class TFSCourseWorkApp : Application() {
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().application(this).plus(AppModule).build()
    }

    fun initLecturesComponent() {
        lecturesComponent = DaggerLecturesComponent.builder().plus(appComponent).plus(LecturesModule).build()
    }

    fun initStudentComponent() {
        studentComponent = userComponent.studentComponentBuilder().plus(StudentModule).build()
    }

    fun initUserComponent() {
        userComponent = DaggerUserComponent.builder().appComponent(appComponent).build()
    }

    companion object {
        @JvmStatic
        lateinit var appComponent: AppComponent
        @JvmStatic
        lateinit var lecturesComponent: LecturesComponent
        @JvmStatic
        lateinit var studentComponent: StudentComponent
        @JvmStatic
        lateinit var userComponent: UserComponent
    }
}