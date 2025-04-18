package myapplication.android.musicavito.ui.downloaded_tracks.mvi

import myapplication.android.core_mvi.LceState
import myapplication.android.core_mvi.MviStore

class DownloadedTracksStore(
    actor: DownloadedTracksActor,
    reducer: DownloadedTracksReducer
): MviStore<
        DownloadedTracksPartialState,
        DownloadedTracksIntent,
        DownloadedTracksState,
        DownloadedTracksEffect>(reducer, actor) {
    override fun initialStateCreator(): DownloadedTracksState =
        DownloadedTracksState(content = LceState.Loading)
}