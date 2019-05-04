package xyz.mperminov.tfscoursework.fragments.courses

import android.util.Log
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import xyz.mperminov.tfscoursework.TFSCourseWorkApp
import xyz.mperminov.tfscoursework.fragments.base.BaseViewModel
import xyz.mperminov.tfscoursework.repositories.students.CourseResponse
import xyz.mperminov.tfscoursework.repositories.students.StudentsRepository
import javax.inject.Inject

class CoursesViewModel : BaseViewModel() {
    private val TAG = this::class.java.simpleName
    @Inject
    lateinit var studentsRepository: StudentsRepository
    val courseinfo: MutableLiveData<CourseResponse?> = MutableLiveData()

    init {
        TFSCourseWorkApp.studentComponent.inject(this)
    }

    fun updateCourseInfo() {
        val d = studentsRepository.getCourses().observeOn(AndroidSchedulers.mainThread())
            .subscribe({ res -> courseinfo.value = res }, { e -> Log.e(TAG, e.message) })
        compositeDisposable.add(d)
    }
}