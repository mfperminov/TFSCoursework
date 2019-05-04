package xyz.mperminov.tfscoursework.repositories.lectures

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import xyz.mperminov.tfscoursework.TFSCourseWorkApp
import xyz.mperminov.tfscoursework.repositories.lectures.db.HomeworkDatabase
import xyz.mperminov.tfscoursework.repositories.lectures.db.LectureDao
import xyz.mperminov.tfscoursework.repositories.lectures.db.TasksDao
import xyz.mperminov.tfscoursework.repositories.lectures.network.HomeworksNetworkRepository
import xyz.mperminov.tfscoursework.repositories.models.Lecture
import xyz.mperminov.tfscoursework.repositories.models.Lectures
import xyz.mperminov.tfscoursework.repositories.models.Task
import javax.inject.Inject

class LecturesRepository @Inject constructor(
    private val networkRepository: HomeworksNetworkRepository,
    database: HomeworkDatabase
) {

    init {
        TFSCourseWorkApp.lecturesComponent.inject(this)
    }

    private val lectureDao: LectureDao = database.lectureDao()
    private val tasksDao: TasksDao = database.tasksDao()
    fun getLectures(): Single<List<Lecture>> {
        return lectureDao.getCount().subscribeOn(Schedulers.io())
            .flatMap { count -> if (count > 0) fetchLecturesFromNetwork() else fetchLecturesFromNetwork() }
    }

    private fun fetchLecturesFromNetwork(): Single<List<Lecture>> {
        return networkRepository.getLectures().firstOrError().subscribeOn(Schedulers.io())
            .flatMapCompletable { lectures ->
                lectureDao.deleteAll()
                    .andThen(tasksDao.deleteAll())
                    .andThen(lectureDao.insertAll(lectures.lectures))
                    .andThen(tasksDao.saveHomeworks(mapLecturesToTasks(lectures)))
            }.andThen(getLecturesFromDb())
    }

    private fun getLecturesFromDb(): Single<List<Lecture>> {
        return lectureDao.getLectures().firstOrError().subscribeOn(Schedulers.io())
    }

    private fun mapLecturesToTasks(lectures: Lectures): List<Task> {
        val tasks = mutableListOf<Task>()
        lectures.lectures.forEach { lecture ->
            lecture.tasks.forEach { task ->
                tasks.add(
                    Task(
                        lecture.id,
                        task.id,
                        task.mark,
                        task.status,
                        task.taskDetails
                    )
                )
            }
        }
        return tasks.toList()
    }
}