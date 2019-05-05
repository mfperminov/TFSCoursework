package xyz.mperminov.tfscoursework.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.BindsInstance
import dagger.Component
import xyz.mperminov.tfscoursework.activities.LoginRepository
import xyz.mperminov.tfscoursework.activities.LoginViewModel
import xyz.mperminov.tfscoursework.fragments.courses.tasks.TasksFragment
import xyz.mperminov.tfscoursework.network.Api
import xyz.mperminov.tfscoursework.network.AuthHolder
import xyz.mperminov.tfscoursework.repositories.lectures.db.HomeworkDatabase
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun provideHomeWorkDatabase(): HomeworkDatabase
    fun provideAuthHolder(): AuthHolder
    fun provideApi(): Api
    fun provideContext(): Context
    fun provideSharedPrefs(): SharedPreferences
    fun inject(tasksFragment: TasksFragment)
    fun inject(loginRepository: LoginRepository)
    fun inject(loginViewModel: LoginViewModel)

    @Component.Builder
    interface Builder {
        fun build(): AppComponent
        fun plus(appModule: AppModule): Builder
        @BindsInstance
        fun application(application: Application): Builder
    }
}