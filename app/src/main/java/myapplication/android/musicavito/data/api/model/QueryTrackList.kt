package myapplication.android.musicavito.data.api.model

import kotlinx.serialization.Serializable

@Serializable
class QueryTrackList(
    val data: List<Track>
)