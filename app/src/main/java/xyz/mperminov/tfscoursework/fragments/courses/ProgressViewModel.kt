package xyz.mperminov.tfscoursework.fragments.courses

import android.os.Handler
import android.util.Log
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import xyz.mperminov.tfscoursework.TFSCourseWorkApp
import xyz.mperminov.tfscoursework.fragments.activitites.archive.Result
import xyz.mperminov.tfscoursework.fragments.base.BaseViewModel
import xyz.mperminov.tfscoursework.models.User
import xyz.mperminov.tfscoursework.repositories.students.StudentsRepository
import xyz.mperminov.tfscoursework.repositories.students.db.Student
import xyz.mperminov.tfscoursework.repositories.user.network.UserNetworkRepository
import javax.inject.Inject

class ProgressViewModel : BaseViewModel() {
    @Inject
    lateinit var studentsRepository: StudentsRepository
    @Inject
    lateinit var userNetworkRepository: UserNetworkRepository
    val topStudents: MutableLiveData<Result<Student>> = MutableLiveData()
    val userRating: MutableLiveData<ratingOverall> = MutableLiveData()
    val userMark: MutableLiveData<Int> = MutableLiveData()
    private var user = User.NOBODY
    private val handler = Handler { msg -> topStudents.value = msg.obj as Result<Student>; true }

    init {
        TFSCourseWorkApp.studentComponent.inject(this)
    }

    fun getTopStudents(size: Long) {
        val d = studentsRepository.getStudents()
            .doOnSubscribe { handler.sendMessage(handler.obtainMessage(1, Result.Loading<Student>())) }
            .subscribeOn(Schedulers.computation())
            .flattenAsObservable { it }
            .sorted { s1, s2 -> (s2.mark - s1.mark).toInt() }
            .take(size)
            .toList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ list ->
                if (list.isEmpty())
                    topStudents.value = Result.Empty()
                else topStudents.value = Result.Success(list.toList())
            }, { e -> Result.Error<Student>(e) })
        compositeDisposable.add(d)
    }

    fun getUserProgress() {
        val d = userNetworkRepository.getUser().flatMap { user -> this.user = user; studentsRepository.getStudents() }
            .subscribeOn(Schedulers.computation())
            .flattenAsObservable { it }
            .sorted { s1, s2 -> (s2.mark - s1.mark).toInt() }
            .toList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ list ->
                list.forEachIndexed { index, student ->
                    if (student.name == "${this.user.lastName} ${this.user.firstName}") {
                        userRating.value =
                            ratingOverall(index, list.size)
                        userMark.value = student.mark.toInt()
                    }
                }
            }, { e -> Log.e("ProgressViewModel", e.message) })
        compositeDisposable.add(d)
    }
}