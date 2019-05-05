package xyz.mperminov.tfscoursework.di

import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import xyz.mperminov.tfscoursework.network.Api
import xyz.mperminov.tfscoursework.network.AuthHolder
import xyz.mperminov.tfscoursework.repositories.activities.ActivitiesNetworkRepository
import xyz.mperminov.tfscoursework.repositories.activities.ArchiveRepository
import xyz.mperminov.tfscoursework.repositories.lectures.db.HomeworkDatabase
import xyz.mperminov.tfscoursework.repositories.user.network.UserNetworkRepository
import xyz.mperminov.tfscoursework.repositories.user.prefs.SharedPrefUserRepository

@Module
object UserModule {
    @Provides
    @UserScope
    fun repository(authHolder: AuthHolder, api: Api, userSharedPrefRepository: SharedPrefUserRepository) =
        UserNetworkRepository(authHolder, api, userSharedPrefRepository)

    @Provides
    @UserScope
    fun archiveNetworkRepository(authHolder: AuthHolder, api: Api) = ActivitiesNetworkRepository(api, authHolder)

    @Provides
    @UserScope
    fun archiveRepository(database: HomeworkDatabase, preferences: SharedPreferences) =
        ArchiveRepository(database, preferences)

    @Provides
    @UserScope
    fun userSharedPrefRepository(preferences: SharedPreferences): SharedPrefUserRepository =
        SharedPrefUserRepository(preferences)
}