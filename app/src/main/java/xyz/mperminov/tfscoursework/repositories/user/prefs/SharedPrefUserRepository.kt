package xyz.mperminov.tfscoursework.repositories.user.prefs

import android.content.SharedPreferences
import xyz.mperminov.tfscoursework.TFSCourseWorkApp
import xyz.mperminov.tfscoursework.models.User
import javax.inject.Inject

class SharedPrefUserRepository @Inject constructor(private val prefs: SharedPreferences) {
    companion object {
        const val ARG_USER = "user"
    }

    init {
        TFSCourseWorkApp.userComponent.inject(this)
    }

    fun getUser(): User {
        return if (hasSavedUser()) {
            val userString = prefs.getString(ARG_USER, null)
            val userInfo = userString.split(delimiters = *arrayOf(" "), limit = 4)
            User(
                userInfo[0], userInfo[1], null, null, userInfo[2], null,
                null, null, null, null, null, null,
                null, null, null, userInfo[3].toLong()
            )
        } else
            User.NOBODY
    }

    fun saveUser(user: User) {
        prefs.edit().putString(ARG_USER, "${user.lastName} ${user.firstName} ${user.email} ${user.id}").apply()
    }

    fun hasSavedUser(): Boolean {
        val userString = prefs.getString(ARG_USER, null)
        return userString != null
    }
}