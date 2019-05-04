package xyz.mperminov.tfscoursework.fragments.courses

import android.os.Handler
import android.util.Log
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import xyz.mperminov.tfscoursework.TFSCourseWorkApp
import xyz.mperminov.tfscoursework.fragments.base.BaseViewModel
import xyz.mperminov.tfscoursework.repositories.lectures.LecturesRepository
import xyz.mperminov.tfscoursework.repositories.lectures.db.HomeworkDatabase
import xyz.mperminov.tfscoursework.repositories.lectures.db.TasksDao
import javax.inject.Inject

typealias ratingOverall = Pair<Int, Int>

class RatingViewModel : BaseViewModel() {
    @Inject
    lateinit var lecturesRepository: LecturesRepository
    @Inject
    lateinit var database: HomeworkDatabase
    private var tasksDao: TasksDao
    val testLiveData: MutableLiveData<ratingOverall> = MutableLiveData()
    val homeworkLiveData: MutableLiveData<ratingOverall> = MutableLiveData()
    val lecturesCount: MutableLiveData<Int> = MutableLiveData()
    private val handler = Handler { msg -> lecturesCount.value = msg.what; true }

    init {
        TFSCourseWorkApp.lecturesComponent.inject(this)
        tasksDao = database.tasksDao()
    }

    fun getTestsRating() {
        val d = lecturesRepository.getLectures().doOnSuccess { list -> handler.sendEmptyMessage(list.size) }
            .flatMap { tasksDao.getAllTasks() }
            .flattenAsObservable { it }
            .filter { it.taskDetails.task_type == TASK_TYPE_TEST }
            .toList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ filteredList ->
                testLiveData.value =
                    ratingOverall(filteredList.filter { it.status == TASK_STATUS_ACCEPTED }.size, filteredList.size)
            },
                { e ->
                    testLiveData.value = ratingOverall(0, 0); Log.e("RatingViewModel", e.message)
                })
        compositeDisposable.add(d)
    }

    fun getHomeworkRating() {
        val d = lecturesRepository.getLectures().doOnSuccess { list -> handler.sendEmptyMessage(list.size) }
            .flatMap { tasksDao.getAllTasks() }
            .flattenAsObservable { it }
            .filter { it.taskDetails.task_type == TASK_TYPE_HOMEWORK }
            .toList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ filteredList ->
                homeworkLiveData.value =
                    ratingOverall(filteredList.filter { it.status == TASK_STATUS_ACCEPTED }.size, filteredList.size)
            },
                { e ->
                    homeworkLiveData.value =
                        ratingOverall(0, 0)
                    Log.e("RatingViewModel", e.message)
                })
        compositeDisposable.add(d)
    }

    companion object {
        const val TASK_STATUS_ACCEPTED = "accepted"
        const val TASK_TYPE_HOMEWORK = "full"
        const val TASK_TYPE_TEST = "test_during_lecture"
    }
}