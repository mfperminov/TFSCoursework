package xyz.mperminov.tfscoursework.network

import android.content.SharedPreferences
import android.util.Log
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import xyz.mperminov.tfscoursework.network.RestClient.apiLogin
import javax.inject.Inject

class AuthHolder @Inject constructor(private val sharedPreferences: SharedPreferences) {

    companion object {
        val AUTH_TOKEN_ARG = "auth_token"
    }

    private val cookiesRecInterceptor = HttpClient.cookiesRecInterceptor

    private fun saveToken() {
        Log.d("SavingToken", "${cookiesRecInterceptor.authCookie}")
        sharedPreferences.edit().putString(AUTH_TOKEN_ARG, cookiesRecInterceptor.authCookie).apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString(AUTH_TOKEN_ARG, null)
    }

    fun updateToken(email: String, password: String): Completable {
        return apiLogin.updateToken(AuthRequest(email, password)).observeOn(AndroidSchedulers.mainThread())
            .doOnComplete { saveToken() }
    }

    fun removeToken() {
        sharedPreferences.edit().putString(AUTH_TOKEN_ARG, "").apply()
    }
}

data class AuthRequest(val email: String, val password: String)