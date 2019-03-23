package xyz.mperminov.tfscoursework.repositories.user

import android.content.Context
import android.preference.PreferenceManager
import xyz.mperminov.tfscoursework.models.User


class SharedPrefUserRepository(private val context: Context) :
    UserRepository {

    companion object {
        const val USER_FNAME = "first_name"
        const val USER_LNAME = "last_name"
        const val USER_PATRONYMIC = "patronymic"
    }

    override fun getUser(): User? {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val lastName = prefs.getString(USER_LNAME, null)
        val firstName = prefs.getString(USER_FNAME, null)
        val patronymic = prefs.getString(USER_PATRONYMIC, null)
        return if (lastName != null && firstName != null && patronymic != null)
            User(lastName, firstName, patronymic)
        else null
    }

    override fun saveUser(user: User) {
        val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
        editor.putString(USER_LNAME, user.lastName)
        editor.putString(USER_FNAME, user.firstName)
        editor.putString(USER_PATRONYMIC, user.patronymic)
        editor.commit()
    }
}