package myapplication.android.musicavito.data.api

import myapplication.android.musicavito.data.api.model.QueryTrackList
import myapplication.android.musicavito.data.api.model.TrackList
import retrofit2.http.GET
import retrofit2.http.Query

interface TracksApi {

    @GET("chart")
    suspend fun getTracks(): TrackList

    @GET("search")
    suspend fun getTracksByQuery(@Query("q") query: String): QueryTrackList

}