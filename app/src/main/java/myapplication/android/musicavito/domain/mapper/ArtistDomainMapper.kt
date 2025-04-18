package myapplication.android.musicavito.domain.mapper

import myapplication.android.musicavito.data.repository.dto.ArtistDto
import myapplication.android.musicavito.domain.model.ArtistDomain

fun ArtistDto.toDomain() = ArtistDomain(name)