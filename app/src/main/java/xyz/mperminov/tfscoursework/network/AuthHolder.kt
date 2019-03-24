package xyz.mperminov.tfscoursework.network

import android.content.SharedPreferences
import android.util.Log
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class AuthHolder(private val callback: Callback, private val prefsProvider: PrefsProvider) {

    private val AUTH_TOKEN_ARG = "auth_token"
    private val cookiesRecInterceptor = HttpClient.cookiesRecInterceptor
    private val retrofit = Retrofit.Builder().baseUrl(Api.API_URL).client(HttpClient.okHttpClient)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addConverterFactory(GsonConverterFactory.create()).build()
    private val api: Api = retrofit.create(Api::class.java)

    fun saveToken() {
        Log.d("SavingToken", "${cookiesRecInterceptor.authCookie}")
        prefsProvider.getPreferences().edit().putString(AUTH_TOKEN_ARG, cookiesRecInterceptor.authCookie).apply()
    }

    fun updateToken(email: String, password: String): Completable {
        return api.updateToken(AuthRequest(email, password)).observeOn(AndroidSchedulers.mainThread())
            .doOnComplete { saveToken(); callback.onAuthSuccess() }.doOnError { callback.onAuthFailure() }
    }

    interface Callback {
        fun onAuthSuccess()

        fun onAuthFailure()
    }

    interface PrefsProvider {
        fun getPreferences(): SharedPreferences
    }
}

data class AuthRequest(val email: String, val password: String)