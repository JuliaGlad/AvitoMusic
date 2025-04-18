package myapplication.android.musicavito.ui.tracks.mvi

import myapplication.android.core_mvi.LceState
import myapplication.android.core_mvi.MviReducer
import myapplication.android.musicavito.ui.model.TracksUiList

class TracksReducer : MviReducer<
        TracksPartialState,
        TracksState> {
    override fun reduce(
        prevState: TracksState,
        partialState: TracksPartialState
    ): TracksState =
        when (partialState) {
            is TracksPartialState.DataLoaded -> updateDataLoaded(prevState, partialState.tracks)
            is TracksPartialState.Error -> updateError(prevState, partialState.throwable)
            TracksPartialState.Loading -> updateLoading(prevState)
            is TracksPartialState.TrackAddedToLocalDb -> updateTrackAddedToLocalDb(prevState, partialState.track, partialState.trackId)
        }

    private fun updateTrackAddedToLocalDb(prevState: TracksState, track: String, trackId: Long) =
        prevState.copy(newLocalTrack = Pair(trackId, track))

    private fun updateDataLoaded(prevState: TracksState, tracks: TracksUiList) =
        prevState.copy(
            content = LceState.Content(data = tracks),
            newLocalTrack = null
        )

    private fun updateError(prevState: TracksState, throwable: Throwable) =
        prevState.copy(
            content = LceState.Error(throwable),
            newLocalTrack = null
        )

    private fun updateLoading(prevState: TracksState) =
        prevState.copy(
            content = LceState.Loading,
            newLocalTrack = null
        )
}