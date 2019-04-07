package xyz.mperminov.tfscoursework.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response


class CookiesRecInterceptor : Interceptor {

    var authCookie: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())
        if (originalResponse.headers("Set-Cookie").isNotEmpty()) {
            for (cookieString in originalResponse.headers("Set-Cookie"))
                if (cookieString.contains("anygen")) {
                    val match = Regex(";").find(cookieString)
                    val anygenPart = match?.range?.start?.let { cookieString.substring(0, it) }
                    if (anygenPart != null)
                        authCookie = anygenPart
                }
        }
        return originalResponse
    }
}