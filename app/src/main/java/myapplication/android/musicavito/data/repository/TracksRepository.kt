package myapplication.android.musicavito.data.repository

import myapplication.android.musicavito.data.repository.dto.TracksDtoList

interface TracksRepository {

    suspend fun getTracks(): TracksDtoList

    suspend fun getTracksByQuery(query: String): TracksDtoList

    suspend fun getLocalTracks(): TracksDtoList

    suspend fun addTrackToLocalDb(
        trackId: Long,
        title: String,
        audio: String,
        albumTitle: String?,
        image: String?,
        artist: String
    )
}