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
    val okHttpClientLogin: OkHttpClient =
        OkHttpClient.Builder().addInterceptor(cookiesRecInterceptor)
            .addInterceptor(
                setupLoggingInterceptor()
            )
            .addInterceptor { chain ->
                TimeUnit.SECONDS.sleep(TIMEOUT_IN_SECONDS)
                chain.proceed(chain.request())
            }.build()
    val okHttpClientDefault: OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(
                setupLoggingInterceptor()
            )
            .addInterceptor { chain ->
                TimeUnit.SECONDS.sleep(TIMEOUT_IN_SECONDS)
                chain.proceed(chain.request())
            }.build()
}