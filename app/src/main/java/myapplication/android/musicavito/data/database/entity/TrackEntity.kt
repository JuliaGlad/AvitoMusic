package myapplication.android.musicavito.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks")
class TrackEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val trackId: Long,
    val title: String,
    val audio: String,
    val albumTitle: String?,
    val image: String?,
    val artist: String
)