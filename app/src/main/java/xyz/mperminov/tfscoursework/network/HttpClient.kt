package xyz.mperminov.tfscoursework.network

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import xyz.mperminov.tfscoursework.network.interceptors.CookiesRecInterceptor
import java.util.concurrent.TimeUnit

object HttpClient {

    private fun setupLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { msg -> Log.d("OkHttp", msg) })
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    private val TIMEOUT_IN_SECONDS: Long = 2
    val cookiesRecInterceptor = CookiesRecInterceptor()
    val okHttpClient =
        OkHttpClient.Builder().addInterceptor(cookiesRecInterceptor)
            .addInterceptor(
                setupLoggingInterceptor()
            )
            .callTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
            .connectTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
            .build()
}