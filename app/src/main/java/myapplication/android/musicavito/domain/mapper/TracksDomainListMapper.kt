package myapplication.android.musicavito.domain.mapper

import myapplication.android.musicavito.data.repository.dto.TrackDto
import myapplication.android.musicavito.data.repository.dto.TracksDtoList
import myapplication.android.musicavito.domain.model.TrackDomain
import myapplication.android.musicavito.domain.model.TracksDomainList
import java.util.stream.Collectors

fun TracksDtoList.toDomain() =
    TracksDomainList(
        tracks = tracks.stream()
            .map { it.toDomain() }
            .collect(Collectors.toList())
    )

fun TrackDto.toDomain() =
    TrackDomain(
        id = id,
        title = title,
        audio = audio,
        album = album?.toDomain(),
        artist = artist.toDomain(),
        isDownloaded = isDownloaded
    )