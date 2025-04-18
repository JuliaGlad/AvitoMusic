package myapplication.android.musicavito.ui.tracks.mvi

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import myapplication.android.core_mvi.MviActor
import myapplication.android.core_mvi.asyncAwait
import myapplication.android.core_mvi.runCatchingNonCancellation
import myapplication.android.musicavito.domain.usecases.AddTracksToLocalUseCase
import myapplication.android.musicavito.domain.usecases.GetTracksByQueryUseCase
import myapplication.android.musicavito.domain.usecases.GetTracksUseCase
import myapplication.android.musicavito.ui.mapper.toUi

class TracksActor(
    private val getTracksUseCase: GetTracksUseCase,
    private val getTracksByQueryUseCase: GetTracksByQueryUseCase,
    private val addTracksToLocalUseCase: AddTracksToLocalUseCase
) : MviActor<
        TracksPartialState,
        TracksIntent,
        TracksState,
        TracksEffect>() {
    override fun resolve(
        intent: TracksIntent,
        state: TracksState
    ): Flow<TracksPartialState> =
        when (intent) {
            is TracksIntent.AddTrackToLocalDb -> addTracksToLocalDb(
                trackId = intent.trackId,
                title = intent.title,
                audio = intent.audio,
                albumTitle = intent.albumTitle,
                image = intent.image,
                artist = intent.artist
            )
            TracksIntent.GetTracks -> loadTracks()
            is TracksIntent.SearchTracks -> loadTracksByQuery(intent.query)
        }

    private fun addTracksToLocalDb(
        trackId: Long,
        title: String,
        audio: String,
        albumTitle: String?,
        image: String?,
        artist: String
    ) = flow {
        kotlin.runCatching {
            addTracksToLocalUseCase.invoke(
                trackId = trackId,
                title = title,
                audio = audio,
                albumTitle = albumTitle,
                image = image,
                artist = artist
            )
        }.fold(
            onSuccess = { emit(TracksPartialState.TrackAddedToLocalDb(title, trackId)) },
            onFailure = { throwable -> emit(TracksPartialState.Error(throwable)) }
        )
    }

    private fun loadTracks() =
        flow {
            kotlin.runCatching {
                getTracks()
            }.fold(
                onSuccess = { data ->
                    emit(TracksPartialState.DataLoaded(data))
                },
                onFailure = { throwable ->
                    emit(TracksPartialState.Error(throwable))
                }
            )
        }

    private fun loadTracksByQuery(query: String) =
        flow {
            kotlin.runCatching {
                getTracksByQuery(query)
            }.fold(
                onSuccess = { data ->
                    emit(TracksPartialState.DataLoaded(data))
                },
                onFailure = { throwable ->
                    emit(TracksPartialState.Error(throwable))
                }
            )
        }

    private suspend fun getTracksByQuery(query: String) =
        runCatchingNonCancellation {
            asyncAwait(
                { getTracksByQueryUseCase.invoke(query) }
            ) { data ->
                data.toUi()
            }
        }.getOrThrow()

    private suspend fun getTracks() =
        runCatchingNonCancellation {
            asyncAwait(
                { getTracksUseCase.invoke() }
            ) { data ->
                data.toUi()
            }
        }.getOrThrow()
}