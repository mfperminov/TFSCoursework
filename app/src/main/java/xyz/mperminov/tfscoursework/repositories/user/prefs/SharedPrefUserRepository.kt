package xyz.mperminov.tfscoursework.repositories.user.prefs

import android.content.Context
import android.preference.PreferenceManager
import io.reactivex.Single
import xyz.mperminov.tfscoursework.models.User
import xyz.mperminov.tfscoursework.repositories.user.UserRepository


class SharedPrefUserRepository(private val context: Context) :
    UserRepository {

    companion object {
        const val USER_FNAME = "first_name"
        const val USER_LNAME = "last_name"
        const val USER_PATRONYMIC = "patronymic"
    }

    override fun getUser(): Single<User> {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val lastName = prefs.getString(USER_LNAME, null)
        val firstName = prefs.getString(USER_FNAME, null)
        val patronymic = prefs.getString(USER_PATRONYMIC, null)
        return if (lastName != null && firstName != null && patronymic != null)
            Single.just(User(lastName, firstName, patronymic, null))
        else Single.just(User.NOBODY)
    }

    override fun saveUser(user: User) {
        val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
        editor.putString(USER_LNAME, user.lastName)
        editor.putString(USER_FNAME, user.firstName)
        editor.putString(USER_PATRONYMIC, user.patronymic)
        editor.apply()
    }
}