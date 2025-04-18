package myapplication.android.musicavito.ui.downloaded_tracks.mvi

import myapplication.android.core_mvi.LceState
import myapplication.android.core_mvi.MviReducer
import myapplication.android.musicavito.ui.downloaded_tracks.model.LocalTracksList
import myapplication.android.musicavito.ui.model.TrackUi

class DownloadedTracksReducer: MviReducer<
        DownloadedTracksPartialState,
        DownloadedTracksState> {
    override fun reduce(
        prevState: DownloadedTracksState,
        partialState: DownloadedTracksPartialState
    ): DownloadedTracksState =
        when(partialState){
            is DownloadedTracksPartialState.DataLoaded -> updateDataLoaded(prevState, partialState.tracks)
            is DownloadedTracksPartialState.Error -> updateError(prevState, partialState.throwable)
            is DownloadedTracksPartialState.FilterData -> updateFilterData(prevState, partialState.query)
            DownloadedTracksPartialState.Loading -> updateLoading(prevState)
            is DownloadedTracksPartialState.TrackDeleted -> updateTrackDeleted(prevState, partialState.track)
        }

    private fun updateTrackDeleted(prevState: DownloadedTracksState, track: String): DownloadedTracksState =
        prevState.copy(deletedTrack = track)

    private fun updateFilterData(prevState: DownloadedTracksState, query: String): DownloadedTracksState {
        val primeTracks = (prevState.content as LceState.Content).data.tracks
        val filteredTracks = mutableListOf<TrackUi>()
        val lowerQuery = query.lowercase()
        primeTracks.forEach { item ->
            if (item.title.lowercase().contains(lowerQuery) || item.artist.name.lowercase().contains(lowerQuery)){
                filteredTracks.add(item)
            }
        }
        return prevState.copy(
            content = LceState.Content(
                LocalTracksList(
                    tracks = primeTracks,
                    filteredTracks = filteredTracks
                )
            ),
            deletedTrack = null
        )
    }

    private fun updateDataLoaded(prevState: DownloadedTracksState, content: LocalTracksList): DownloadedTracksState =
        prevState.copy(
            content = LceState.Content(content),
            deletedTrack = null
        )

    private fun updateError(prevState: DownloadedTracksState, throwable: Throwable): DownloadedTracksState =
        prevState.copy(
            content = LceState.Error(throwable),
            deletedTrack = null
        )

    private fun updateLoading(prevState: DownloadedTracksState): DownloadedTracksState =
        prevState.copy(
            content = LceState.Loading,
            deletedTrack = null
        )
}