package xyz.mperminov.tfscoursework.repositories.lectures.network

import io.reactivex.Observable
import xyz.mperminov.tfscoursework.TFSCourseWorkApp
import xyz.mperminov.tfscoursework.network.Api
import xyz.mperminov.tfscoursework.network.AuthHolder
import xyz.mperminov.tfscoursework.repositories.lectures.HomeworksRepository
import xyz.mperminov.tfscoursework.repositories.models.Lectures
import javax.inject.Inject

class HomeworksNetworkRepository @Inject constructor(val api: Api, val authHolder: AuthHolder) : HomeworksRepository {

    init {
        TFSCourseWorkApp.lecturesComponent.inject(this)
    }

    override fun getLectures(): Observable<Lectures> {
        return api.getLectures(authHolder.getToken() ?: "")
    }
}