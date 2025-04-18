package myapplication.android.musicavito.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import myapplication.android.musicavito.data.database.entity.TrackEntity

@Dao
interface TrackDao {
    @Query("SELECT * FROM tracks")
    fun getTracks(): List<TrackEntity>

    @Insert
    fun insertTrack(track: TrackEntity)

    @Query("DELETE FROM tracks")
    fun deleteAll()

    @Delete
    fun deleteTrack(track: TrackEntity)
}