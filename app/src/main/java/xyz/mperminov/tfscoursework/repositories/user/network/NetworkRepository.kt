package xyz.mperminov.tfscoursework.repositories.user.network

import android.util.Log
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import xyz.mperminov.tfscoursework.models.User
import xyz.mperminov.tfscoursework.network.Api
import xyz.mperminov.tfscoursework.network.HttpClient
import xyz.mperminov.tfscoursework.repositories.user.UserRepository

class NetworkRepository(private val tokenProvider: TokenProvider) :
    UserRepository {

    private val retrofit = Retrofit.Builder().baseUrl(Api.API_URL).client(HttpClient.okHttpClient)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addConverterFactory(GsonConverterFactory.create()).build()
    private val api: Api = retrofit.create(Api::class.java)

    override fun getUser(): User? {
        Log.d("token", "${tokenProvider.getToken()}")
        return if (tokenProvider.getToken() == null) null
        else api.getUser(tokenProvider.getToken() as String).blockingFirst().user
    }

    override fun saveUser(user: User) {
        // no impl
    }

    interface TokenProvider {
        fun getToken(): String?
    }
}