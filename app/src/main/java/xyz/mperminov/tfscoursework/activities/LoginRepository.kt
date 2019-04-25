package xyz.mperminov.tfscoursework.activities

import io.reactivex.Completable
import xyz.mperminov.tfscoursework.TFSCourseWorkApp
import xyz.mperminov.tfscoursework.network.AuthHolder
import javax.inject.Inject

class LoginRepository @Inject constructor(private val authHolder: AuthHolder) {

    init {
        TFSCourseWorkApp.appComponent.inject(this)
    }

    fun login(email: String, password: String): Completable {
        return authHolder.updateToken(email, password)
    }

    fun isTokenValid(): Boolean {
        //Todo more checks (expires?)
        return authHolder.getToken() != null
    }
}