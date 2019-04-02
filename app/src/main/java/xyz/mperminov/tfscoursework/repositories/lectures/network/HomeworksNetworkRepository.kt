package xyz.mperminov.tfscoursework.repositories.lectures.network

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import xyz.mperminov.tfscoursework.network.Api
import xyz.mperminov.tfscoursework.network.HttpClient
import xyz.mperminov.tfscoursework.repositories.lectures.HomeworksRepository

class HomeworksNetworkRepository(private val sessionToken: String) : HomeworksRepository {
    private val retrofit = Retrofit.Builder().baseUrl(Api.API_URL).client(HttpClient.okHttpClient)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addConverterFactory(GsonConverterFactory.create()).build()

    private val api: Api = retrofit.create(Api::class.java)
    override fun getLectures(): Observable<Lectures> {
        return api.getLectures(sessionToken)
    }
}