package myapplication.android.musicavito.data.source.local

import myapplication.android.musicavito.data.database.entity.TrackEntity

interface TracksLocalSource {

    fun insertTracksToLocalDb(
        trackId: Long,
        title: String,
        audio: String,
        albumTitle: String?,
        image: String?,
        artist: String
    )

    fun getLocalTracksId(): List<Long>

    fun getTracksFromLocalDb(): List<TrackEntity>

    fun deleteTrackFromLocalDb(trackId: Long)

}