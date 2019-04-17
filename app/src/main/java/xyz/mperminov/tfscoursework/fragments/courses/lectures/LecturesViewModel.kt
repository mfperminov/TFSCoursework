package xyz.mperminov.tfscoursework.fragments.courses.lectures

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import xyz.mperminov.tfscoursework.repositories.lectures.LecturesRepository

class LecturesViewModel : ViewModel() {
    private var lecturesDisposable: Disposable? = null
    private val repository = LecturesRepository()
    val lecturesLiveData: MutableLiveData<List<LectureModelView>> = MutableLiveData()
    val result: MutableLiveData<Result> = MutableLiveData()
    fun updateLectures() {
        lecturesDisposable = repository.getLectures().doOnSubscribe { result.value = Result.Loading() }.map {
            it.map { lecture ->
                LectureModelView(
                    lecture.id,
                    lecture.title
                )
            }
        }.observeOn(AndroidSchedulers.mainThread())
            .subscribe({ lecturesModel ->
                if (lecturesModel.isEmpty())
                    result.value = Result.Empty()
                else
                    result.value = Result.Success()
                lecturesLiveData.value = lecturesModel
            }, { e -> Result.Error(e) })
    }

    override fun onCleared() {
        lecturesDisposable?.dispose()
        lecturesDisposable = null
        super.onCleared()
    }
}

enum class Status { LOADING, SUCCESS, ERROR, EMPTY }
sealed class Result(val status: Status, val error: Throwable?) {
    class Loading : Result(Status.LOADING, null)
    class Success : Result(Status.SUCCESS, null)
    class Error(e: Throwable) : Result(Status.ERROR, e)
    class Empty : Result(Status.EMPTY, null)
}
