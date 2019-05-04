package xyz.mperminov.tfscoursework.repositories.students

import android.util.Log
import dagger.Lazy
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import xyz.mperminov.tfscoursework.TFSCourseWorkApp
import xyz.mperminov.tfscoursework.repositories.lectures.db.HomeworkDatabase
import xyz.mperminov.tfscoursework.repositories.students.db.Student
import xyz.mperminov.tfscoursework.repositories.students.network.NetworkStudentsRepository
import javax.inject.Inject

class StudentsRepository @Inject constructor(
    database: HomeworkDatabase,
    private val updateTimeSaver: UpdateTimeSaver
) {
    private val TAG = this.javaClass.simpleName
    private val CACHE_LIFETIME_SEC = 10
    private val studentsDao = database.studentsDao()
    @Inject
    lateinit var networkRepository: Lazy<NetworkStudentsRepository>

    init {
        TFSCourseWorkApp.studentComponent.inject(this)
    }

    fun getStudents(): Single<List<Student>> {
        return studentsDao.getCount().subscribeOn(Schedulers.io())
            .flatMap { count ->
                if ((count > 0) && (updateTimeSaver.getTimeDiffInSeconds(System.currentTimeMillis()) < CACHE_LIFETIME_SEC))
                    retrieveStudentsFromDb()
                else
                    fetchStudentsFromNetwork()
            }
    }

    fun getCourses(): Single<CourseResponse> {
        return networkRepository.get().getCourses().map { it -> it.courses[0] }
    }

    private fun fetchStudentsFromNetwork(): Single<List<Student>> {
        Log.i(TAG, "start network synchronization")
        return networkRepository.get().getStudents()
            .flatMapCompletable { newStudents -> studentsDao.deleteAll().andThen(studentsDao.insertAll(newStudents)) }
            .doOnComplete { updateTimeSaver.saveUpdateTime(System.currentTimeMillis()) }
            .andThen(retrieveStudentsFromDb())
    }

    private fun retrieveStudentsFromDb(): Single<List<Student>> {
        Log.i(TAG, "retrieve from database")
        return studentsDao.getStudents().subscribeOn(Schedulers.io())
    }

    interface UpdateTimeSaver {
        fun saveUpdateTime(timestamp: Long)
        fun getTimeDiffInSeconds(currentTime: Long): Long
    }
}