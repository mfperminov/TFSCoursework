package xyz.mperminov.tfscoursework.repositories.students.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface StudentsDao {
    @Query("SELECT * FROM students")
    fun getStudents(): Single<List<Student>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(lectures: List<Student>): Completable

    @Query("SELECT count(*) FROM students")
    fun getCount(): Single<Int>

    @Query("DELETE FROM students")
    fun deleteAll(): Completable
}