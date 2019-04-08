package xyz.mperminov.tfscoursework.repositories.lectures.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import xyz.mperminov.tfscoursework.repositories.models.Lecture

@Dao
interface LectureDao {
    @Query("SELECT * FROM lectures")
    fun getLectures(): Observable<List<Lecture>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(lectures: List<Lecture>): Completable

    @Query("SELECT count(*) FROM lectures")
    fun getCount(): Single<Int>

    @Query("DELETE FROM lectures")
    fun deleteAll(): Completable
}