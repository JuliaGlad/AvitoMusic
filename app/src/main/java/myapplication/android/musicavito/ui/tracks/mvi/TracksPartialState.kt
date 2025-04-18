package myapplication.android.musicavito.ui.tracks.mvi

import myapplication.android.core_mvi.MviPartialState
import myapplication.android.musicavito.ui.model.TracksUiList

sealed interface TracksPartialState: MviPartialState {

    data object Loading: TracksPartialState

    class DataLoaded(val tracks: TracksUiList): TracksPartialState

    class TrackAddedToLocalDb(val track: String, val trackId: Long): TracksPartialState

    class Error(val throwable: Throwable): TracksPartialState

}