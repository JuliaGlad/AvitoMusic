package myapplication.android.musicavito.ui.tracks.mvi

import myapplication.android.core_mvi.MviIntent

sealed interface TracksIntent: MviIntent {

    data object GetTracks: TracksIntent

    class SearchTracks(val query: String): TracksIntent

    class AddTrackToLocalDb(
       val trackId: Long,
       val title: String,
       val audio: String,
       val albumTitle: String?,
       val image: String?,
       val artist: String
    ): TracksIntent

}