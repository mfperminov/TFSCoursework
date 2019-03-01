package xyz.mperminov.tfscoursework.repositories

import android.content.Context
import android.preference.PreferenceManager
import xyz.mperminov.tfscoursework.models.User


class SharedPrefUserRepository(private val context: Context) : UserRepository {


    override fun getUser(): User? {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val lastName = prefs.getString(UserRepository.USER_LNAME, null)
        val firstName = prefs.getString(UserRepository.USER_FNAME, null)
        val patronymic = prefs.getString(UserRepository.USER_PATRONYMIC, null)
        return if (lastName != null && firstName != null && patronymic != null)
            User(lastName,firstName,patronymic)
        else null


    }

    override fun saveUser(user: User) {
        val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
        editor.putString(UserRepository.USER_LNAME, user.lastName)
        editor.putString(UserRepository.USER_FNAME, user.firstName)
        editor.putString(UserRepository.USER_PATRONYMIC, user.patronymic)
        editor.commit()
    }

}