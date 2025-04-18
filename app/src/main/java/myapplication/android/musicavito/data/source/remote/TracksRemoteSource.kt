package myapplication.android.musicavito.data.source.remote

import myapplication.android.musicavito.data.api.model.QueryTrackList
import myapplication.android.musicavito.data.api.model.TrackList

interface TracksRemoteSource {

    suspend fun getTracks(): TrackList

    suspend fun getTracksByQuery(query: String): QueryTrackList
}