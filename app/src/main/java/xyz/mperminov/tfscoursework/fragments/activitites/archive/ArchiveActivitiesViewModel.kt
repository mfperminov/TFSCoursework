package xyz.mperminov.tfscoursework.fragments.activitites.archive

import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import xyz.mperminov.tfscoursework.TFSCourseWorkApp
import xyz.mperminov.tfscoursework.fragments.base.BaseViewModel
import xyz.mperminov.tfscoursework.repositories.activities.ActivitiesNetworkRepository
import xyz.mperminov.tfscoursework.repositories.activities.Archive
import javax.inject.Inject

class ArchiveActivitiesViewModel : BaseViewModel() {
    @Inject
    lateinit var repository: ActivitiesNetworkRepository
    val activitiesLiveData: MutableLiveData<Result<Archive>> = MutableLiveData()

    init {
        TFSCourseWorkApp.userComponent.inject(this)
    }

    fun getActivities() {
        val d = repository.getArchive().doOnSubscribe { activitiesLiveData.value = Result.Loading() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ list ->
                if (list.isEmpty()) {
                    activitiesLiveData.value = Result.Empty()
                } else {
                    activitiesLiveData.value = Result.Success(list)
                }
            }, { e -> activitiesLiveData.value = Result.Error(e) })
        compositeDisposable.add(d)
    }
}

enum class Status { SUCCESS, EMPTY, ERROR, LOADING }
sealed class Result<T>(val data: List<T>, val status: Status, val e: Throwable?) {
    class Success<T>(data: List<T>) : Result<T>(data, Status.SUCCESS, null)
    class Empty<T> : Result<T>(emptyList(), Status.EMPTY, null)
    class Error<T>(e: Throwable) : Result<T>(emptyList(), Status.ERROR, e)
    class Loading<T> : Result<T>(emptyList(), Status.LOADING, null)
}
