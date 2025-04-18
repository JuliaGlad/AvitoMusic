package myapplication.android.musicavito.ui.downloaded_tracks.mvi

import myapplication.android.core_mvi.MviIntent

sealed interface DownloadedTracksIntent: MviIntent {

    data object GetLocalTracks: DownloadedTracksIntent

    class FilterTracks(val query: String): DownloadedTracksIntent

}