package myapplication.android.musicavito.data.repository

import myapplication.android.musicavito.data.source.local.TracksLocalSource
import myapplication.android.musicavito.data.source.remote.TracksRemoteSource
import myapplication.android.musicavito.data.mapper.dto.toDto
import myapplication.android.musicavito.data.repository.dto.TracksDtoList
import javax.inject.Inject

class TracksRepositoryImpl @Inject constructor(
    private val localSource: TracksLocalSource,
    private val remoteSource: TracksRemoteSource
): TracksRepository {
    override suspend fun getTracks(): TracksDtoList {
        val dtos = remoteSource.getTracks().toDto()
        val downloadedIds = localSource.getLocalTracksId()
        for (dto in dtos.tracks){
            if (downloadedIds.contains(dto.id)){
                dto.isDownloaded = true
            }
        }
        return dtos
    }

    override suspend fun getTracksByQuery(query: String): TracksDtoList {
        val dtos = remoteSource.getTracksByQuery(query).toDto()
        val downloadedIds = localSource.getLocalTracksId()
        for (dto in dtos.tracks){
            if (downloadedIds.contains(dto.id)){
                dto.isDownloaded = true
            }
        }
        return dtos
    }

    override suspend fun getLocalTracks(): TracksDtoList =
        localSource.getTracksFromLocalDb().toDto()

    override suspend fun addTrackToLocalDb(
        trackId: Long,
        title: String,
        audio: String,
        albumTitle: String?,
        image: String?,
        artist: String
    ) {
       localSource.insertTracksToLocalDb(
           trackId = trackId,
           title = title,
           audio = audio,
           albumTitle = albumTitle,
           image = image,
           artist = artist
       )
    }
}