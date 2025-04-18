package myapplication.android.musicavito.ui.mapper

import myapplication.android.musicavito.domain.model.ArtistDomain
import myapplication.android.musicavito.ui.model.ArtistUi

fun ArtistDomain.toUi() = ArtistUi(name = name)