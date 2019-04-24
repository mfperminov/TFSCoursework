package xyz.mperminov.tfscoursework.repositories.students

import android.content.SharedPreferences
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class UpdateTimeSaverImpl @Inject constructor(private val sharedPreferences: SharedPreferences) :
    StudentsRepository.UpdateTimeSaver {
    companion object {
        private const val ARG_TIME_UPDATE = "last_time_updated"
    }

    override fun saveUpdateTime(timestamp: Long) {
        sharedPreferences.edit().putLong(ARG_TIME_UPDATE, timestamp).apply()
    }

    override fun getTimeDiffInSeconds(currentTime: Long): Long {
        val lastTimeUpdate = sharedPreferences.getLong(ARG_TIME_UPDATE, 0)
        return TimeUnit.MILLISECONDS.toSeconds(currentTime - lastTimeUpdate)
    }
}