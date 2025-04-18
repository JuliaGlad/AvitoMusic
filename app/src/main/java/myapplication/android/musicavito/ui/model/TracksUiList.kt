package myapplication.android.musicavito.ui.model

class TracksUiList(
    val tracks: List<TrackUi>
)

class TrackUi(
    val id: Long,
    val title: String,
    val audio: String,
    val album: AlbumUi?,
    val artist: ArtistUi,
    val isDownloaded: Boolean
)