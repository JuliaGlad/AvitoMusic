package myapplication.android.musicavito.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class TrackList(
    val tracks: TracksData
)

@Serializable
class TracksData(
    val data: List<Track>
)

@Serializable
class Track(
    val id: Long,
    val title: String,
    @SerialName("preview") val audio: String,
    val album: Album?,
    val artist: Artist
)