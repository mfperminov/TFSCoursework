package xyz.mperminov.tfscoursework.fragments.courses.lectures.di

import dagger.Module
import dagger.Provides
import xyz.mperminov.tfscoursework.fragments.courses.lectures.LecturesViewModel
import xyz.mperminov.tfscoursework.network.Api
import xyz.mperminov.tfscoursework.network.AuthHolder
import xyz.mperminov.tfscoursework.repositories.lectures.LecturesRepository
import xyz.mperminov.tfscoursework.repositories.lectures.db.HomeworkDatabase
import xyz.mperminov.tfscoursework.repositories.lectures.network.HomeworksNetworkRepository

@Module
object LecturesModule {
    @Provides
    @LecturesScope
    fun repository(networkRepository: HomeworksNetworkRepository, database: HomeworkDatabase) =
        LecturesRepository(networkRepository, database)

    @Provides
    @LecturesScope
    fun networkRepository(api: Api, authHolder: AuthHolder) = HomeworksNetworkRepository(api, authHolder)

    @Provides
    @LecturesScope
    fun viewModel() = LecturesViewModel()
}