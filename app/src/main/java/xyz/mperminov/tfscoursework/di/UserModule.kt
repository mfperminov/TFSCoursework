package xyz.mperminov.tfscoursework.di

import dagger.Module
import dagger.Provides
import xyz.mperminov.tfscoursework.network.Api
import xyz.mperminov.tfscoursework.network.AuthHolder
import xyz.mperminov.tfscoursework.repositories.activities.ActivitiesNetworkRepository
import xyz.mperminov.tfscoursework.repositories.user.network.UserNetworkRepository

@Module
object UserModule {
    @Provides
    @UserScope
    fun repository(authHolder: AuthHolder, api: Api) = UserNetworkRepository(authHolder, api)

    @Provides
    @UserScope
    fun activitiesRepository(authHolder: AuthHolder, api: Api) = ActivitiesNetworkRepository(api, authHolder)
}