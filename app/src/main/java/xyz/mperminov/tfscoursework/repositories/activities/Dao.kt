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