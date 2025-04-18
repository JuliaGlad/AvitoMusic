package myapplication.android.musicavito.ui.downloaded_tracks.mvi

import myapplication.android.core_mvi.LceState
import myapplication.android.core_mvi.MviState
import myapplication.android.musicavito.ui.downloaded_tracks.model.LocalTracksList

data class DownloadedTracksState(val content: LceState<LocalTracksList>): MviState