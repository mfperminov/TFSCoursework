package xyz.mperminov.tfscoursework.repositories.user

import io.reactivex.Single
import xyz.mperminov.tfscoursework.models.User

interface UserRepository {
    fun getUser(): Single<User>

    fun saveUser(user: User)
}