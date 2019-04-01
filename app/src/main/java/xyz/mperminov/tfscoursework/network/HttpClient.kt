package xyz.mperminov.tfscoursework.network

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import xyz.mperminov.tfscoursework.network.interceptors.CookiesRecInterceptor

object HttpClient {

    private fun setupLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { msg -> Log.d("OkHttp", msg) })
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    private val TIMEOUT_IN_SECONDS: Long = 2
    val cookiesRecInterceptor = CookiesRecInterceptor()
    val okHttpClient: OkHttpClient =
        OkHttpClient.Builder().addInterceptor(cookiesRecInterceptor)
            .addInterceptor(
                setupLoggingInterceptor()
            ).build()
}