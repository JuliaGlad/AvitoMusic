package myapplication.android.musicavito.domain.usecases

import myapplication.android.musicavito.data.repository.TracksRepository
import myapplication.android.musicavito.domain.mapper.toDomain
import myapplication.android.musicavito.domain.model.TracksDomainList
import javax.inject.Inject

class GetLocalTracksUseCase @Inject constructor(
    private val tracksRepository: TracksRepository
) {
    suspend fun invoke(): TracksDomainList =
        tracksRepository.getLocalTracks().toDomain()
}