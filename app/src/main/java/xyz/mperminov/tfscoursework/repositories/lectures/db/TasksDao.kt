package xyz.mperminov.tfscoursework.repositories.lectures.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single
import xyz.mperminov.tfscoursework.repositories.models.Task

@Dao
interface TasksDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveHomeworks(homeworks: List<Task>): Completable

    @Query("SELECT * FROM tasks WHERE lecturesId=:id")
    fun getHomeworkByLectureId(id: Int): Single<List<Task>>

    @Query("SELECT * FROM tasks")
    fun getAllTasks(): Single<List<Task>>

    @Query("DELETE FROM tasks")
    fun deleteAll(): Completable
}