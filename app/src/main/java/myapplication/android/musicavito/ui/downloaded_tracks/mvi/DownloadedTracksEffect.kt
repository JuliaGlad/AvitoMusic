package myapplication.android.musicavito.ui.downloaded_tracks.mvi

import myapplication.android.core_mvi.MviEffect

sealed interface DownloadedTracksEffect: MviEffect {

    class DeleteTrack(val trackId: Long): DownloadedTracksEffect

    class PlayTrack(val audio: String): DownloadedTracksEffect

}