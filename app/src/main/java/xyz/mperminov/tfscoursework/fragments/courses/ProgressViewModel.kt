package xyz.mperminov.tfscoursework.fragments.courses

import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import xyz.mperminov.tfscoursework.TFSCourseWorkApp
import xyz.mperminov.tfscoursework.fragments.activitites.archive.Result
import xyz.mperminov.tfscoursework.fragments.base.BaseViewModel
import xyz.mperminov.tfscoursework.repositories.students.StudentsRepository
import xyz.mperminov.tfscoursework.repositories.students.db.Student
import javax.inject.Inject

class ProgressViewModel : BaseViewModel() {
    @Inject
    lateinit var studentsRepository: StudentsRepository
    val topStudents: MutableLiveData<Result<Student>> = MutableLiveData()

    init {
        TFSCourseWorkApp.studentComponent.inject(this)
    }

    fun getTopStudents(size: Long) {
        val d = studentsRepository.getStudents()
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
}