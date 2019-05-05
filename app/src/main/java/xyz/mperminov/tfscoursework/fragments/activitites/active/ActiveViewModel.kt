package xyz.mperminov.tfscoursework.fragments.activitites.active

import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import xyz.mperminov.tfscoursework.TFSCourseWorkApp
import xyz.mperminov.tfscoursework.fragments.activitites.archive.Result
import xyz.mperminov.tfscoursework.fragments.base.BaseViewModel
import xyz.mperminov.tfscoursework.repositories.activities.Active
import xyz.mperminov.tfscoursework.repositories.activities.ActiveRepository
import javax.inject.Inject

class ActiveViewModel : BaseViewModel() {
    @Inject
    lateinit var repository: ActiveRepository
    val activitiesLiveData: MutableLiveData<Result<Active>> = MutableLiveData()

    init {
        TFSCourseWorkApp.userComponent.inject(this)
    }

    fun getActivities() {
        val d = repository.getActive().subscribeOn(Schedulers.io())
            .doOnSubscribe { activitiesLiveData.value = Result.Loading() }
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