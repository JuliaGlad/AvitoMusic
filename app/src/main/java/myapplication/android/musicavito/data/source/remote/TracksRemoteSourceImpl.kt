package myapplication.android.musicavito.data.source.remote

import myapplication.android.musicavito.data.api.TracksApi
import myapplication.android.musicavito.data.api.model.QueryTrackList
import myapplication.android.musicavito.data.api.model.TrackList
import myapplication.android.musicavito.data.source.remote.TracksRemoteSource
import javax.inject.Inject

class TracksRemoteSourceImpl@Inject constructor(
    private val api: TracksApi
) : TracksRemoteSource {
    override suspend fun getTracks(): TrackList =
        api.getTracks()

    override suspend fun getTracksByQuery(query: String): QueryTrackList =
        api.getTracksByQuery(query)
}