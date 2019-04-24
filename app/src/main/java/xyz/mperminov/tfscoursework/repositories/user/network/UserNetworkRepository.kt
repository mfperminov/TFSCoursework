package xyz.mperminov.tfscoursework.repositories.user.network

import android.util.Log
import io.reactivex.Single
import xyz.mperminov.tfscoursework.TFSCourseWorkApp
import xyz.mperminov.tfscoursework.models.User
import xyz.mperminov.tfscoursework.network.Api
import xyz.mperminov.tfscoursework.network.AuthHolder
import xyz.mperminov.tfscoursework.repositories.user.UserRepository
import javax.inject.Inject

class UserNetworkRepository @Inject constructor(private val authHolder: AuthHolder, private val api: Api) :
    UserRepository {
    init {
        TFSCourseWorkApp.profileComponent.inject(this)
    }

    override fun getUser(): Single<User> {
        val token = authHolder.getToken()
        return if (token != null) {
            Log.d("token", "$token")
            api.getUser(token).firstOrError().map { t: UserSchema -> t.user }
        } else
            Single.just(User.NOBODY)
    }

    override fun saveUser(user: User) {
        // no impl
    }
}