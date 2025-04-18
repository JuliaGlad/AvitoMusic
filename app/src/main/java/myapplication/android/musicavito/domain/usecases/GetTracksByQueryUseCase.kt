package myapplication.android.musicavito.domain.usecases

import myapplication.android.musicavito.domain.model.TracksDomainList
import myapplication.android.musicavito.data.repository.TracksRepository
import myapplication.android.musicavito.domain.mapper.toDomain
import javax.inject.Inject

class GetTracksByQueryUseCase @Inject constructor(
    private val repository: TracksRepository
) {
    suspend fun invoke(query: String): TracksDomainList =
        repository.getTracksByQuery(query).toDomain()
}