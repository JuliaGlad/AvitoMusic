package myapplication.android.musicavito.ui.downloaded_tracks.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DownloadedTracksStoreFactory(
    private val actor: DownloadedTracksActor,
    private val reducer: DownloadedTracksReducer
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DownloadedTracksStore(actor, reducer) as T
    }
}