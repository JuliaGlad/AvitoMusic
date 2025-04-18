package myapplication.android.musicavito.domain.model

class TracksDomainList(
    val tracks: List<TrackDomain>
)
class TrackDomain(
    val id: Long,
    val title: String,
    val audio: String,
    val album: AlbumDomain?,
    val artist: ArtistDomain,
    val isDownloaded: Boolean
)