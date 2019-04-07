package xyz.mperminov.tfscoursework.network

import io.reactivex.Completable
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import xyz.mperminov.tfscoursework.repositories.user.network.UserSchema


interface Api {
    companion object {
        const val API_URL = "https://fintech.tinkoff.ru/api/"
        const val API_AVATAR_HOST = "https://fintech.tinkoff.ru"
    }

    @POST("signin")
    fun updateToken(@Body authRequest: AuthRequest): Completable

    @GET("user")
    fun getUser(@Header("Cookie") sessionToken: String): Observable<UserSchema>
}