package xyz.mperminov.tfscoursework.repositories.user.network

import android.util.Log
import io.reactivex.Single
import xyz.mperminov.tfscoursework.models.User
import xyz.mperminov.tfscoursework.network.Api
import xyz.mperminov.tfscoursework.network.RestClient
import xyz.mperminov.tfscoursework.repositories.user.UserRepository

class UserNetworkRepository(private val tokenProvider: TokenProvider) :
    UserRepository {
    private val api: Api = RestClient.api
    override fun getUser(): Single<User> {
        val token = tokenProvider.getToken()
        if (token != null) {
            Log.d("token", "$token")
            return api.getUser(token).firstOrError().map { t: UserSchema -> t.user }
        }
        return Single.just(User.NOBODY)
    }

    override fun saveUser(user: User) {
        // no impl
    }

    interface TokenProvider {
        fun getToken(): String?
    }
}