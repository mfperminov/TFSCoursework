package xyz.mperminov.tfscoursework.repositories.lectures.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import xyz.mperminov.tfscoursework.repositories.lectures.network.Lecture


@Dao
interface LectureDao {
    @Query("SELECT * FROM lectures")
    fun getLectures(): Observable<List<Lecture>>

    @Update
    fun updateLectures(lectures: List<Lecture>): Completable

    @Insert
    fun insertAll(lectures: List<Lecture>): Completable

    @Query("SELECT count(*) FROM lectures")
    fun getCount(): Single<Int>
}