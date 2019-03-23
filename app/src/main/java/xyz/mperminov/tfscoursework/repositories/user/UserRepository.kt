package xyz.mperminov.tfscoursework.repositories.user

import xyz.mperminov.tfscoursework.models.User

interface UserRepository {

    fun getUser(): User?

    fun saveUser(user: User)
}