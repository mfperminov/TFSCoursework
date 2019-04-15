package xyz.mperminov.tfscoursework.activities

import io.reactivex.Completable
import xyz.mperminov.tfscoursework.TFSCourseWorkApp

class LoginRepository {
    private val authHolder = TFSCourseWorkApp.authHolder
    fun login(email: String, password: String): Completable {
        return authHolder.updateToken(email, password)
    }
}