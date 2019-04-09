package xyz.mperminov.tfscoursework.repositories.students.network

import io.reactivex.Single
import xyz.mperminov.tfscoursework.network.RestClient
import xyz.mperminov.tfscoursework.repositories.students.db.Student
import xyz.mperminov.tfscoursework.repositories.students.db.StudentMapper
import xyz.mperminov.tfscoursework.repositories.user.network.UserNetworkRepository

class NetworkStudentsRepository(private val tokenProvider: UserNetworkRepository.TokenProvider) {
    private val api = RestClient.api
    private val mapper = StudentMapper()
    fun getStudents(): Single<List<Student>> {
        val token = tokenProvider.getToken()
        return if (token != null)
            api.getStudents(token).firstOrError().map { list -> mapper.mapToDbModel(list) }
        else Single.just(emptyList())
    }
}