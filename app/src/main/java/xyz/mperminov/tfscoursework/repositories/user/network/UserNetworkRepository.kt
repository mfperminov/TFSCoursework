package xyz.mperminov.tfscoursework.repositories.user.network

import android.util.Log
import xyz.mperminov.tfscoursework.models.User
import xyz.mperminov.tfscoursework.network.Api
import xyz.mperminov.tfscoursework.network.RestClient
import xyz.mperminov.tfscoursework.repositories.user.UserRepository

class UserNetworkRepository(private val tokenProvider: TokenProvider) :
    UserRepository {
    private val api: Api = RestClient.api

    override fun getUser(): User? {
        Log.d("token", "${tokenProvider.getToken()}")
        return if (tokenProvider.getToken() == null) null
        else api.getUser(tokenProvider.getToken() as String).onErrorReturn { _ -> UserSchema() }.blockingFirst().user
    }

    override fun saveUser(user: User) {
        // no impl
    }

    interface TokenProvider {
        fun getToken(): String?
    }
}