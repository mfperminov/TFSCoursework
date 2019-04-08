package xyz.mperminov.tfscoursework.network

import android.content.SharedPreferences
import android.util.Log
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import xyz.mperminov.tfscoursework.network.RestClient.apiLogin

class AuthHolder(private val prefsProvider: PrefsProvider) {

    companion object {
        val AUTH_TOKEN_ARG = "auth_token"
    }

    private val cookiesRecInterceptor = HttpClient.cookiesRecInterceptor
    private val api: Api = RestClient.api

    private fun saveToken() {
        Log.d("SavingToken", "${cookiesRecInterceptor.authCookie}")
        prefsProvider.getPreferences().edit().putString(AUTH_TOKEN_ARG, cookiesRecInterceptor.authCookie).apply()
    }

    fun getToken(): String? {
        return prefsProvider.getPreferences().getString(AUTH_TOKEN_ARG, null)
    }

    fun updateToken(email: String, password: String): Completable {
        return apiLogin.updateToken(AuthRequest(email, password)).observeOn(AndroidSchedulers.mainThread())
            .doOnComplete { saveToken() }
    }

    interface PrefsProvider {
        fun getPreferences(): SharedPreferences
    }
}

data class AuthRequest(val email: String, val password: String)