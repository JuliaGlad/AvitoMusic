package myapplication.android.musicavito.ui.downloaded_tracks.mvi

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import myapplication.android.core_mvi.MviActor
import myapplication.android.core_mvi.asyncAwait
import myapplication.android.core_mvi.runCatchingNonCancellation
import myapplication.android.musicavito.domain.usecases.DeleteTrackFromLocalDbUseCase
import myapplication.android.musicavito.domain.usecases.GetLocalTracksUseCase
import myapplication.android.musicavito.ui.mapper.toLocalUi

class DownloadedTracksActor(
    private val getLocalTracksUseCase: GetLocalTracksUseCase,
    private val deleteTrackFromLocalDbUseCase: DeleteTrackFromLocalDbUseCase
) : MviActor<
        DownloadedTracksPartialState,
        DownloadedTracksIntent,
        DownloadedTracksState,
        DownloadedTracksEffect>() {

    override fun resolve(
        intent: DownloadedTracksIntent,
        state: DownloadedTracksState
    ): Flow<DownloadedTracksPartialState> =
        when (intent) {
            is DownloadedTracksIntent.FilterTracks -> filterTracksByQuery(intent.query)
            DownloadedTracksIntent.GetLocalTracks -> loadTracks()
            is DownloadedTracksIntent.DeleteTrackFromLocal -> deleteTrackFromLocalDb(
                intent.trackId, intent.track
            )
        }

    private fun filterTracksByQuery(query: String) =
        flow { emit(DownloadedTracksPartialState.FilterData(query)) }

    private fun deleteTrackFromLocalDb(trackId: Long, track: String) =
        flow {
            kotlin.runCatching {
                deleteTrackFromLocalDbUseCase.invoke(trackId)
            }.fold(
                onSuccess = { emit(DownloadedTracksPartialState.TrackDeleted(track))},
                onFailure = { throwable -> emit(DownloadedTracksPartialState.Error(throwable))}
            )
        }

    private fun loadTracks() =
        flow {
            kotlin.runCatching {
                getTracks()
            }.fold(
                onSuccess = { data ->
                    emit(DownloadedTracksPartialState.DataLoaded(data))
                },
                onFailure = { throwable ->
                    emit(DownloadedTracksPartialState.Error(throwable))
                }
            )
        }

    private suspend fun getTracks() =
        runCatchingNonCancellation {
            asyncAwait(
                { getLocalTracksUseCase.invoke() }
            ) { data ->
                data.toLocalUi()
            }
        }.getOrThrow()
}