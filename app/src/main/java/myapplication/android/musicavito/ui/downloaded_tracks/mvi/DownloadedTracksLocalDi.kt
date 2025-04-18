package myapplication.android.musicavito.ui.downloaded_tracks.mvi

import myapplication.android.musicavito.data.repository.TracksRepository
import myapplication.android.musicavito.domain.usecases.GetLocalTracksUseCase
import javax.inject.Inject

class DownloadedTracksLocalDi @Inject constructor(
    tracksRepository: TracksRepository
) {
    private val getLocalTracksUseCase by lazy { GetLocalTracksUseCase(tracksRepository) }

    val actor by lazy { DownloadedTracksActor(getLocalTracksUseCase) }

    val reducer by lazy { DownloadedTracksReducer() }
}