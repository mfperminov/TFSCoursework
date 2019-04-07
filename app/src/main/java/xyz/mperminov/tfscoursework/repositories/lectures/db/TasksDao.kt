package xyz.mperminov.tfscoursework.repositories.lectures.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.reactivex.Completable
import io.reactivex.Single
import xyz.mperminov.tfscoursework.repositories.lectures.network.Task

@Dao
interface TasksDao {
    @Insert
    fun saveHomeworks(homeworks: List<Task>): Completable

    @Query("SELECT * FROM tasks WHERE lecturesId=:id")
    fun getHomeworkByLectureId(id: Int): Single<List<Task>>

    @Update
    fun updateTasks(homeworks: List<Task>): Completable
}