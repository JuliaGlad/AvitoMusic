package myapplication.android.musicavito.ui.tracks.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TracksStoreFactory(
    private val actor: TracksActor,
    private val reducer: TracksReducer
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TracksStore(actor, reducer) as T
    }
}