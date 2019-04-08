package xyz.mperminov.tfscoursework.network

import io.reactivex.Completable
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiLogin {
    @POST("signin")
    fun updateToken(@Body authRequest: AuthRequest): Completable
}