package myapplication.android.musicavito.data.mapper.dto

import myapplication.android.musicavito.data.api.model.Album
import myapplication.android.musicavito.data.repository.dto.AlbumDto

fun Album.toDto() =
    AlbumDto(
        title = title,
        image = image
    )