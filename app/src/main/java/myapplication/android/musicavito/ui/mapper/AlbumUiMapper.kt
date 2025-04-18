package myapplication.android.musicavito.ui.mapper

import myapplication.android.musicavito.domain.model.AlbumDomain
import myapplication.android.musicavito.ui.model.AlbumUi

fun AlbumDomain.toUi() =
    AlbumUi(
        title = title,
        image = image
    )