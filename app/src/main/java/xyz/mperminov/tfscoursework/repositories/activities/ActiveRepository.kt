package xyz.mperminov.tfscoursework.repositories.activities

import android.content.SharedPreferences
import dagger.Lazy
import io.reactivex.Single
import xyz.mperminov.tfscoursework.TFSCourseWorkApp
import xyz.mperminov.tfscoursework.repositories.lectures.db.HomeworkDatabase
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ActiveRepository @Inject constructor(database: HomeworkDatabase, private val preferences: SharedPreferences) {
    private val activeDao = database.activeDao()
    @Inject
    lateinit var networkRepository: Lazy<ActivitiesNetworkRepository>

    init {
        TFSCourseWorkApp.userComponent.inject(this)
    }

    fun getActive(): Single<List<Active>> {
        return activeDao.getCount()
            .flatMap { count -> if ((count > 0) && !needUpdate()) activeDao.getActive() else updateActive() }
    }

    private fun updateActive(): Single<List<Active>> {
        return networkRepository.get().getActive()
            .flatMapCompletable { activeDao.deleteAll().andThen(activeDao.insertAll(it)) }
            .andThen(activeDao.getActive()).doOnSuccess { recordTimestamp() }
    }

    private fun recordTimestamp() {
        preferences.edit().putLong(TIMESTAMP_ARG_ACTIVE, System.currentTimeMillis()).apply()
    }

    // older then minute
    private fun needUpdate(): Boolean {
        return TimeUnit.SECONDS.convert(
            System.currentTimeMillis() -
                    preferences.getLong(TIMESTAMP_ARG_ACTIVE, 1000 * 60 * 10),
            TimeUnit.MILLISECONDS
        ) > 60
    }

    companion object {
        const val TIMESTAMP_ARG_ACTIVE = "timestamp_active_updated"
    }
}