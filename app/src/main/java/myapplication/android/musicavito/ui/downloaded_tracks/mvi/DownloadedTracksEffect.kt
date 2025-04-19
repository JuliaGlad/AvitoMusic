package myapplication.android.musicavito.ui.downloaded_tracks.mvi

import myapplication.android.core_mvi.MviEffect

sealed interface DownloadedTracksEffect: MviEffect {

    class ShowTrackDeletedSnackBar(val track: String): DownloadedTracksEffect

    class PlayTrack(val currentPosition: Int): DownloadedTracksEffect

}