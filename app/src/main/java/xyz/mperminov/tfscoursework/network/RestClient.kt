package xyz.mperminov.tfscoursework.network

import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RestClient {
    private val retrofit = Retrofit.Builder().baseUrl(Api.API_URL).client(HttpClient.okHttpClient)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addConverterFactory(GsonConverterFactory.create()).build()
    val api: Api = retrofit.create(Api::class.java)
}