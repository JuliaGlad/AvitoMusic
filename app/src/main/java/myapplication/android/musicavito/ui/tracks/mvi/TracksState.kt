package myapplication.android.musicavito.ui.tracks.mvi

import myapplication.android.core_mvi.LceState
import myapplication.android.core_mvi.MviState
import myapplication.android.musicavito.ui.model.TracksUiList

data class TracksState(val content: LceState<TracksUiList>, val newLocalTrack: Pair<Long, String>? = null): MviState