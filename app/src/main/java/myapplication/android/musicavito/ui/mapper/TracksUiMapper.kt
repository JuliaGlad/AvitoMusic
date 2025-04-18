package myapplication.android.musicavito.ui.mapper

import myapplication.android.musicavito.domain.model.TrackDomain
import myapplication.android.musicavito.domain.model.TracksDomainList
import myapplication.android.musicavito.ui.downloaded_tracks.model.LocalTracksList
import myapplication.android.musicavito.ui.model.TrackUi
import myapplication.android.musicavito.ui.model.TracksUiList
import java.util.stream.Collectors


fun TracksDomainList.toUi() =
    TracksUiList(
        tracks = tracks.stream()
            .map { it.toUi() }
            .collect(Collectors.toList())
    )

fun TracksDomainList.toLocalUi() =
    LocalTracksList(
        tracks = tracks.stream()
            .map { it.toUi() }
            .collect(Collectors.toList()),
        filteredTracks = emptyList()
    )

fun TrackDomain.toUi() =
    TrackUi(
        id = id,
        title = title,
        audio = audio,
        album = album?.toUi(),
        artist = artist.toUi(),
        isDownloaded = isDownloaded
    )