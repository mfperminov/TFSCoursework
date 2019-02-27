package xyz.mperminov.tfscoursework.repositories

import xyz.mperminov.tfscoursework.models.User

interface UserRepository {
    companion object {
        const val USER_FNAME = "first_name"
        const val USER_LNAME = "last_name"
        const val USER_PATRONYMIC = "patronymic"
    }

    fun getUser(): User?

    fun saveUser(user: User)
}