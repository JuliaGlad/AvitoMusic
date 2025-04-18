package myapplication.android.musicavito.ui.downloaded_tracks.mvi

import myapplication.android.musicavito.data.repository.TracksRepository
import myapplication.android.musicavito.domain.usecases.DeleteTrackFromLocalDbUseCase
import myapplication.android.musicavito.domain.usecases.GetLocalTracksUseCase
import javax.inject.Inject

class DownloadedTracksLocalDi @Inject constructor(
    tracksRepository: TracksRepository
) {
    private val getLocalTracksUseCase by lazy { GetLocalTracksUseCase(tracksRepository) }

    private val deleteTrackFromLocalDbUseCase by lazy { DeleteTrackFromLocalDbUseCase(tracksRepository) }

    val actor by lazy { DownloadedTracksActor(getLocalTracksUseCase, deleteTrackFromLocalDbUseCase) }

    val reducer by lazy { DownloadedTracksReducer() }
}