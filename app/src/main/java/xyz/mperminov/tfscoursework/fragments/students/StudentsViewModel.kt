package xyz.mperminov.tfscoursework.fragments.students

import android.util.Log
import android.widget.Filter
import android.widget.Filterable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import xyz.mperminov.tfscoursework.TFSCourseWorkApp
import xyz.mperminov.tfscoursework.repositories.students.StudentsRepository
import xyz.mperminov.tfscoursework.repositories.students.db.Student
import xyz.mperminov.tfscoursework.repositories.user.network.UserNetworkRepository
import javax.inject.Inject

class StudentsViewModel : ViewModel(), Filterable {
    @Inject
    lateinit var studentsRepository: StudentsRepository
    @Inject
    lateinit var userNetworkRepository: UserNetworkRepository
    private val TAG = this.javaClass.simpleName
    private var studentSchemaDisposable: Disposable? = null
    private var students: List<Student> = listOf()
        set(value) {
            field = value
            studentsLiveData.value = value
        }
    val studentsLiveData: MutableLiveData<List<Student>> = MutableLiveData()
    val searchQuery: MutableLiveData<CharSequence> = MutableLiveData()
    val result: MutableLiveData<Result> = MutableLiveData()

    init {
        TFSCourseWorkApp.studentComponent.inject(this)
        Log.d("userRepo", "$userNetworkRepository")
    }

    override fun onCleared() {
        studentSchemaDisposable?.dispose()
        studentSchemaDisposable = null
        super.onCleared()
    }

    //Todo Не учитывается значение фильтра.
    //Я вбиваю в поиск Ми происходит фильтрация, затем я обновляю с помощью PullToRefresh,
    // зачем происходит обновление списка без учета данных в текстовом поиске.
    fun getStudents() {
        studentSchemaDisposable =
            studentsRepository.getStudents()
                .doOnSubscribe { result.value = Result.Loading() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { students ->
                        if (students.isEmpty()) {
                            result.value = Result.Empty()
                        } else {
                            this.students = students
                            result.value = Result.Success()
                        }
                    },
                    { e ->
                        Log.e(TAG, e.localizedMessage)
                        result.value = Result.Error(e)
                    })
    }

    fun sortStudentsByMarks() {
        studentsLiveData.value = studentsLiveData.value?.sortedWith(
            CompareStudents.Companion
        )
    }

    fun sortStudentsAlphabetically() {
        studentsLiveData.value = studentsLiveData.value?.sortedBy { it.name }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(query: CharSequence?): FilterResults {
                val newFilteredList = if (query.toString().isEmpty()) students else students.filter {
                    it.name.contains(
                        query!!,
                        true
                    )
                }.sortedWith(
                    CompareStudents.Companion
                )
                val filterResults = Filter.FilterResults()
                filterResults.values = newFilteredList
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, results: FilterResults?) {
                searchQuery.value = p0
                studentsLiveData.value = results?.values as List<Student>
            }
        }
    }

    fun resetFilter() {
        searchQuery.value = ""
        studentsLiveData.value = students
    }
}

class CompareStudents {
    companion object : Comparator<Student> {
        override fun compare(a: Student, b: Student): Int {
            return when {
                a.mark > b.mark -> -1
                b.mark > a.mark -> 1
                a.name > b.name -> 1
                else -> -1
            }
        }
    }
}

enum class Status { LOADING, SUCCESS, ERROR, EMPTY }
sealed class Result(val status: Status, val error: Throwable?) {
    class Loading : Result(Status.LOADING, null)
    class Success : Result(Status.SUCCESS, null)
    class Error(e: Throwable) : Result(Status.ERROR, e)
    class Empty : Result(Status.EMPTY, null)
}