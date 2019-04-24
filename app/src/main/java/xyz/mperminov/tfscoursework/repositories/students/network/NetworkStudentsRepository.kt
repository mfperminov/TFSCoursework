package xyz.mperminov.tfscoursework.repositories.students.network

import io.reactivex.Single
import xyz.mperminov.tfscoursework.TFSCourseWorkApp
import xyz.mperminov.tfscoursework.network.Api
import xyz.mperminov.tfscoursework.network.AuthHolder
import xyz.mperminov.tfscoursework.repositories.students.db.Student
import xyz.mperminov.tfscoursework.repositories.students.db.StudentMapper
import javax.inject.Inject

class NetworkStudentsRepository @Inject constructor(
    private val authHolder: AuthHolder,
    private val api: Api,
    private val mapper: StudentMapper
) {

    init {
        TFSCourseWorkApp.studentComponent.inject(this)
    }

    fun getStudents(): Single<List<Student>> {
        val token = authHolder.getToken()
        return if (token != null)
            api.getStudents(token).firstOrError().map { list -> mapper.mapToDbModel(list) }
        else Single.just(emptyList())
    }
}