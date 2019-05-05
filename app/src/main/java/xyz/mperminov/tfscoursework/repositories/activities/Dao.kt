package xyz.mperminov.tfscoursework.repositories.activities

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface ArchiveDao {
    @Query("SELECT * FROM archive")
    fun getArchive(): Single<List<Archive>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(lectures: List<Archive>): Completable

    @Query("SELECT count(*) FROM archive")
    fun getCount(): Single<Int>

    @Query("DELETE FROM archive")
    fun deleteAll(): Completable
}

@Dao
interface ActiveDao {
    @Query("SELECT * FROM active")
    fun getActive(): Single<List<Active>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(lectures: List<Active>): Completable

    @Query("SELECT count(*) FROM active")
    fun getCount(): Single<Int>

    @Query("DELETE FROM active")
    fun deleteAll(): Completable
}