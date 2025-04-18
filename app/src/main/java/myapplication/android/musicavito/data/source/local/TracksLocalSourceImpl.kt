package myapplication.android.musicavito.data.source.local

import myapplication.android.musicavito.data.database.entity.TrackEntity
import myapplication.android.musicavito.data.database.provider.TracksProvider
import javax.inject.Inject

class TracksLocalSourceImpl @Inject constructor(): TracksLocalSource {
    override fun insertTracksToLocalDb(
        trackId: Long,
        title: String,
        audio: String,
        albumTitle: String?,
        image: String?,
        artist: String
    ) {
        TracksProvider().insertTrack(
            trackId = trackId,
            title = title,
            audio = audio,
            albumTitle = albumTitle,
            image = image,
            artist = artist
        )
    }

    override fun getLocalTracksId(): List<Long> = TracksProvider().getTrackIds()

    override fun getTracksFromLocalDb(): List<TrackEntity> = TracksProvider().getTracks()

    override fun deleteTrackFromLocalDb(trackId: Long) {
        TracksProvider().deleteTrack(trackId)
    }
}