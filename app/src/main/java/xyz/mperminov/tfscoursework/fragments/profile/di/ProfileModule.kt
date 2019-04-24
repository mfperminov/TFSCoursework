package xyz.mperminov.tfscoursework.fragments.profile.di

import dagger.Module
import dagger.Provides
import xyz.mperminov.tfscoursework.network.Api
import xyz.mperminov.tfscoursework.network.AuthHolder
import xyz.mperminov.tfscoursework.repositories.user.network.UserNetworkRepository

@Module
object ProfileModule {
    @Provides
    @ProfileScope
    fun repository(authHolder: AuthHolder, api: Api) = UserNetworkRepository(authHolder, api)
}