package xyz.mperminov.tfscoursework.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import xyz.mperminov.tfscoursework.network.Api
import xyz.mperminov.tfscoursework.network.AuthHolder
import xyz.mperminov.tfscoursework.repositories.lectures.LecturesRepository
import xyz.mperminov.tfscoursework.repositories.lectures.db.HomeworkDatabase
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(lecturesRepository: LecturesRepository)
    fun provideHomeWorkDatabase(): HomeworkDatabase
    fun provideAuthHolder(): AuthHolder
    fun provideApi(): Api

    @Component.Builder
    interface Builder {
        fun build(): AppComponent
        fun plus(appModule: AppModule): Builder
        @BindsInstance
        fun application(application: Application): Builder
    }
}