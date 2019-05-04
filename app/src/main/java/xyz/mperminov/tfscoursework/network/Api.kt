package xyz.mperminov.tfscoursework.network

import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import xyz.mperminov.tfscoursework.repositories.activities.ActivitiesResponce
import xyz.mperminov.tfscoursework.repositories.models.Lectures
import xyz.mperminov.tfscoursework.repositories.students.ConnectionResponse
import xyz.mperminov.tfscoursework.repositories.students.network.StudentSchema
import xyz.mperminov.tfscoursework.repositories.user.network.UserSchema

interface Api {
    companion object {
        const val API_URL = "https://fintech.tinkoff.ru/api/"
        const val API_AVATAR_HOST = "https://fintech.tinkoff.ru"
    }

    @GET("user")
    fun getUser(@Header("Cookie") sessionToken: String): Observable<UserSchema>

    @GET("course/android_spring_2019/homeworks")
    fun getLectures(@Header("Cookie") sessionToken: String): Observable<Lectures>

    @GET("course/android_spring_2019/grades")
    fun getStudents(@Header("Cookie") sessionToken: String): Observable<List<StudentSchema>>

    @GET("connections")
    fun getCourses(@Header("Cookie") sessionToken: String): Single<ConnectionResponse>

    @GET("calendar/list/event")
    fun getActivities(@Header("Cookie") sessionToken: String): Single<ActivitiesResponce>
}