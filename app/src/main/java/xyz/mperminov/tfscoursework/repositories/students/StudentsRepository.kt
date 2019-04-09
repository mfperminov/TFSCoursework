package xyz.mperminov.tfscoursework.repositories.students

import android.util.Log
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import xyz.mperminov.tfscoursework.TFSCourseWorkApp
import xyz.mperminov.tfscoursework.repositories.students.db.Student
import xyz.mperminov.tfscoursework.repositories.students.network.NetworkStudentsRepository
import xyz.mperminov.tfscoursework.repositories.user.network.UserNetworkRepository

class StudentsRepository(
    private val tokenProvider: UserNetworkRepository.TokenProvider,
    private val updateTimeSaver: UpdateTimeSaver
) {
    private val TAG = this.javaClass.simpleName
    private val CACHE_LIFETIME_SEC = 10
    private val networkRepository: NetworkStudentsRepository by lazy {
        NetworkStudentsRepository(tokenProvider)
    }
    private val studentsDao = TFSCourseWorkApp.database.studentsDao()
    fun getStudents(): Single<List<Student>> {
        return studentsDao.getCount().subscribeOn(Schedulers.io())
            .flatMap { count ->
                if ((count > 0) && (updateTimeSaver.getTimeDiffInSeconds(System.currentTimeMillis()) < CACHE_LIFETIME_SEC))
                    retrieveStudentsFromDb()
                else
                    fetchStudentsFromNetwork()
            }
    }

    private fun fetchStudentsFromNetwork(): Single<List<Student>> {
        Log.i(TAG, "start network synchronization")
        return networkRepository.getStudents().flatMapCompletable { newStudents -> studentsDao.insertAll(newStudents) }
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