package myapplication.android.musicavito.data.mapper.dto

import myapplication.android.musicavito.data.api.model.Artist
import myapplication.android.musicavito.data.repository.dto.ArtistDto

fun Artist.toDto() = ArtistDto(name = name)