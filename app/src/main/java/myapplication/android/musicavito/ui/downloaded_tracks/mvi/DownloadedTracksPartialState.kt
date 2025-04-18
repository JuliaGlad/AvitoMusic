package myapplication.android.musicavito.ui.downloaded_tracks.mvi

import myapplication.android.core_mvi.MviPartialState
import myapplication.android.musicavito.ui.downloaded_tracks.model.LocalTracksList
import myapplication.android.musicavito.ui.model.TracksUiList

sealed interface DownloadedTracksPartialState: MviPartialState {

    data object Loading: DownloadedTracksPartialState

    class DataLoaded(val tracks: LocalTracksList): DownloadedTracksPartialState

    class FilterData(val query: String): DownloadedTracksPartialState

    class Error(val throwable: Throwable): DownloadedTracksPartialState

}