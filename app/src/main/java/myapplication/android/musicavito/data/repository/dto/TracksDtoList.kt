package myapplication.android.musicavito.data.repository.dto

class TracksDtoList(
    val tracks: List<TrackDto>
)

class TrackDto(
    val id: Long,
    val title: String,
    val audio: String,
    val album: AlbumDto?,
    val artist: ArtistDto,
    var isDownloaded: Boolean = false
)