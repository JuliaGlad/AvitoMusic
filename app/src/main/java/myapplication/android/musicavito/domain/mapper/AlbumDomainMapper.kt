package myapplication.android.musicavito.domain.mapper

import myapplication.android.musicavito.data.repository.dto.AlbumDto
import myapplication.android.musicavito.domain.model.AlbumDomain

fun AlbumDto.toDomain() = AlbumDomain(
    title = title,
    image = image
)