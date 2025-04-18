package myapplication.android.musicavito.ui.tracks.mvi

import myapplication.android.musicavito.data.repository.TracksRepository
import myapplication.android.musicavito.domain.usecases.AddTracksToLocalUseCase
import myapplication.android.musicavito.domain.usecases.GetTracksByQueryUseCase
import myapplication.android.musicavito.domain.usecases.GetTracksUseCase
import javax.inject.Inject

class TracksLocalDi @Inject constructor(
    tracksRepository: TracksRepository
) {

    private val getTracksUseCase by lazy { GetTracksUseCase(tracksRepository) }

    private val getTracksByQueryUseCase by lazy { GetTracksByQueryUseCase(tracksRepository) }

    private val addTracksToLocalUseCase by lazy { AddTracksToLocalUseCase(tracksRepository) }

    val actor by lazy { TracksActor(getTracksUseCase, getTracksByQueryUseCase, addTracksToLocalUseCase) }

    val reducer by lazy { TracksReducer() }
}