package myapplication.android.musicavito.domain.usecases

import myapplication.android.musicavito.data.repository.TracksRepository
import javax.inject.Inject

class AddTracksToLocalUseCase @Inject constructor(
    private val tracksRepository: TracksRepository
) {
    suspend fun invoke(
        trackId: Long,
        title: String,
        audio: String,
        albumTitle: String?,
        image: String?,
        artist: String
    ) {
        tracksRepository.addTrackToLocalDb(
            trackId = trackId,
            title = title,
            audio = audio,
            albumTitle = albumTitle,
            image = image,
            artist = artist
        )
    }
}