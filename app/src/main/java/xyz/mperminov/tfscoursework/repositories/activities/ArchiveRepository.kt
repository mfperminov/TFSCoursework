package xyz.mperminov.tfscoursework.repositories.activities

import android.content.SharedPreferences
import androidx.room.TypeConverter
import dagger.Lazy
import io.reactivex.Single
import xyz.mperminov.tfscoursework.TFSCourseWorkApp
import xyz.mperminov.tfscoursework.repositories.lectures.db.HomeworkDatabase
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ArchiveRepository @Inject constructor(database: HomeworkDatabase, private val preferences: SharedPreferences) {
    private val archiveDao = database.archiveDao()
    @Inject
    lateinit var networkRepository: Lazy<ActivitiesNetworkRepository>

    init {
        TFSCourseWorkApp.userComponent.inject(this)
    }

    fun getArchive(): Single<List<Archive>> {
        return archiveDao.getCount()
            .flatMap { count -> if ((count > 0) && !needUpdate()) archiveDao.getArchive() else updateArchive() }
    }

    private fun updateArchive(): Single<List<Archive>> {
        return networkRepository.get().getArchive()
            .flatMapCompletable { archiveDao.deleteAll().andThen(archiveDao.insertAll(it)) }
            .andThen(archiveDao.getArchive()).doOnSuccess { recordTimestamp() }
    }

    private fun recordTimestamp() {
        preferences.edit().putLong(TIMESTAMP_ARG, System.currentTimeMillis()).apply()
    }

    // older then minute
    private fun needUpdate(): Boolean {
        return TimeUnit.SECONDS.convert(
            System.currentTimeMillis() -
                    preferences.getLong(TIMESTAMP_ARG, 1000 * 60 * 10),
            TimeUnit.MILLISECONDS
        ) > 60
    }

    companion object {
        const val TIMESTAMP_ARG = "timestamp_archive_updated"
    }
}

class DateTypeConverter() {
    @TypeConverter
    fun toDate(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun toLong(value: Date): Long? {
        return value.time
    }
}

class EventTypeConverter() {
    @TypeConverter
    fun toString(value: EventType): String {
        return value.toString()
    }

    @TypeConverter
    fun toEventType(value: String): EventType {
        return EventType.valueOf(value)
    }
}