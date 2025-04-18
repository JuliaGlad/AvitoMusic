package myapplication.android.musicavito.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import myapplication.android.musicavito.data.database.dao.TrackDao
import myapplication.android.musicavito.data.database.entity.TrackEntity

@Database(
    entities = [
        TrackEntity::class
    ], version = 1
)
abstract class LocalDatabase: RoomDatabase() {

    abstract fun tracksDao(): TrackDao

}