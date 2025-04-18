package myapplication.android.musicavito.domain.usecases

import myapplication.android.musicavito.domain.model.TracksDomainList
import myapplication.android.musicavito.data.repository.TracksRepository
import myapplication.android.musicavito.domain.mapper.toDomain
import javax.inject.Inject

class GetTracksUseCase @Inject constructor(
    private val repository: TracksRepository
) {
    suspend fun invoke(): TracksDomainList =
        repository.getTracks().toDomain()
}