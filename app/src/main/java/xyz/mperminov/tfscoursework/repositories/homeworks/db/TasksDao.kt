package xyz.mperminov.tfscoursework.repositories.homeworks.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single
import xyz.mperminov.tfscoursework.repositories.homeworks.network.Task

@Dao
interface TasksDao {
    @Insert
    fun saveHomeworks(homeworks: List<Task>): Completable

    @Query("SELECT * FROM tasks WHERE lecturesId=:id")
    fun getHomeworkByLectureId(id: Int): Single<List<Task>>
}