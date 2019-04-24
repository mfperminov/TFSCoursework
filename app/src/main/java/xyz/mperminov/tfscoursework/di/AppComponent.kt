package xyz.mperminov.tfscoursework.di

import android.app.Application
import android.content.SharedPreferences
import dagger.BindsInstance
import dagger.Component
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
    fun provideSharedPrefs(): SharedPreferences

    @Component.Builder
    interface Builder {
        fun build(): AppComponent
        fun plus(appModule: AppModule): Builder
        @BindsInstance
        fun application(application: Application): Builder
    }
}