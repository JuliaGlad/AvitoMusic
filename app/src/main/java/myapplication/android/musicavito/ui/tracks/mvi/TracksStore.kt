package myapplication.android.musicavito.ui.tracks.mvi

import myapplication.android.core_mvi.LceState
import myapplication.android.core_mvi.MviStore
import myapplication.android.musicavito.ui.tracks.mvi.TracksActor
import myapplication.android.musicavito.ui.tracks.mvi.TracksEffect
import myapplication.android.musicavito.ui.tracks.mvi.TracksIntent
import myapplication.android.musicavito.ui.tracks.mvi.TracksPartialState
import myapplication.android.musicavito.ui.tracks.mvi.TracksReducer
import myapplication.android.musicavito.ui.tracks.mvi.TracksState

class TracksStore(
    actor: TracksActor,
    reducer: TracksReducer
) : MviStore<
        TracksPartialState,
        TracksIntent,
        TracksState,
        TracksEffect>(reducer, actor) {
    override fun initialStateCreator(): TracksState = TracksState(LceState.Loading)
}