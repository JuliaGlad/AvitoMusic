package myapplication.android.musicavito.data.mapper.dto

import myapplication.android.musicavito.data.api.model.QueryTrackList
import myapplication.android.musicavito.data.api.model.Track
import myapplication.android.musicavito.data.api.model.TrackList
import myapplication.android.musicavito.data.database.entity.TrackEntity
import myapplication.android.musicavito.data.repository.dto.AlbumDto
import myapplication.android.musicavito.data.repository.dto.ArtistDto
import myapplication.android.musicavito.data.repository.dto.TrackDto
import myapplication.android.musicavito.data.repository.dto.TracksDtoList
import java.util.stream.Collectors

fun TrackList.toDto() =
    TracksDtoList(
        tracks = tracks.data.stream()
            .map { it.toDto() }
            .collect(Collectors.toList())
    )

fun QueryTrackList.toDto() =
    TracksDtoList(
        tracks = data.stream()
            .map { it.toDto() }
            .collect(Collectors.toList())
    )

fun List<TrackEntity>.toDto() =
    TracksDtoList(
        tracks = stream()
            .map { it.toDto() }
            .collect(Collectors.toList())
    )

fun TrackEntity.toDto() =
    TrackDto(
        id = trackId,
        title = title,
        audio = audio,
        album = AlbumDto(title, image),
        artist = ArtistDto(artist)
    )

fun Track.toDto() =
    TrackDto(
        id = id,
        title = title,
        audio = audio,
        album = album?.toDto(),
        artist = artist.toDto()
    )