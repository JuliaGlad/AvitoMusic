package myapplication.android.musicavito.domain.usecases

import myapplication.android.musicavito.data.repository.TracksRepository
import javax.inject.Inject

class DeleteTrackFromLocalDbUseCase @Inject constructor(
    private val tracksRepository: TracksRepository
) {
    suspend fun invoke(trackId: Long) = tracksRepository.deleteTrackFromLocalDb(trackId)
}