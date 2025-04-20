package myapplication.android.musicavito.ui.tracks.mvi

import myapplication.android.core_mvi.MviEffect
import myapplication.android.musicavito.ui.model.TrackUi

sealed interface TracksEffect: MviEffect {

    class PlayTrack(val trackPosition: Int): TracksEffect

    class DownloadTrack(val track: TrackUi): TracksEffect

    class ShowSnackBar(val text: String): TracksEffect

}