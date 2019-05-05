package xyz.mperminov.tfscoursework.repositories.user.network

import android.util.Log
import io.reactivex.Single
import xyz.mperminov.tfscoursework.TFSCourseWorkApp
import xyz.mperminov.tfscoursework.models.User
import xyz.mperminov.tfscoursework.network.Api
import xyz.mperminov.tfscoursework.network.AuthHolder
import xyz.mperminov.tfscoursework.repositories.user.UserRepository
import xyz.mperminov.tfscoursework.repositories.user.prefs.SharedPrefUserRepository
import javax.inject.Inject

class UserNetworkRepository @Inject constructor(
    private val authHolder: AuthHolder,
    private val api: Api,
    private val prefRepository: SharedPrefUserRepository
) :
    UserRepository {
    init {
        TFSCourseWorkApp.userComponent.inject(this)
    }

    override fun getUser(): Single<User> {
        val token = authHolder.getToken()
        return if (token != null) {
            Log.d("token", "$token")
            api.getUser(token).firstOrError()
                .doOnSuccess { it.user?.let { user -> prefRepository.saveUser(user) } }
                .onErrorResumeNext { _ ->
                    if (prefRepository.hasSavedUser())
                        Single.just(UserSchema().apply {
                            user = prefRepository.getUser()
                        }) else
                        Single.just(UserSchema().apply { user = User.NOBODY })
                }
                .map { t: UserSchema -> t.user }
        } else
            Single.just(User.NOBODY)
    }

    override fun saveUser(user: User) {
        // no impl
    }
}