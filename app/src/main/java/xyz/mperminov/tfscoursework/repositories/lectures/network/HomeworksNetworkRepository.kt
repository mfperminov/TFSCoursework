package xyz.mperminov.tfscoursework.repositories.lectures.network

import io.reactivex.Observable
import xyz.mperminov.tfscoursework.network.Api
import xyz.mperminov.tfscoursework.network.RestClient
import xyz.mperminov.tfscoursework.repositories.lectures.HomeworksRepository

class HomeworksNetworkRepository(private val sessionToken: String) : HomeworksRepository {
    private val api: Api = RestClient.api
    override fun getLectures(): Observable<Lectures> {
        return api.getLectures(sessionToken)
    }
}