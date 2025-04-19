package myapplication.android.musicavito.ui.mapper

import myapplication.android.core_ui.recycler_item.TrackModel
import myapplication.android.musicavito.ui.model.TrackUi
import myapplication.android.musicavito.ui.model.TracksUiList
import java.util.stream.Collectors

fun TracksUiList.toRecyclerItems(
    onItemClicked: (Int) -> Unit,
    onIconClicked: (TrackUi) -> Unit,
    isVisible: Boolean = true
): MutableList<TrackModel> = tracks.stream()
    .map {
        it.toRecyclerItem(
            tracks.indexOf(it),
            onItemClicked,
            onIconClicked,
            isVisible
        )
    }.collect(Collectors.toList())

fun List<TrackUi>.toRecyclerItems(
    onItemClicked: (Int) -> Unit,
    onIconClicked: ((TrackUi) -> Unit)? = null,
    isVisible: Boolean = true
): MutableList<TrackModel> = stream()
    .map {
        it.toRecyclerItem(
            indexOf(it),
            onItemClicked,
            onIconClicked,
            isVisible
        )
    }.collect(Collectors.toList())

fun TrackUi.toRecyclerItem(
    index: Int,
    onItemClicked: (Int) -> Unit,
    onIconClicked: ((TrackUi) -> Unit)?,
    isVisible: Boolean
) = TrackModel(
    id = index,
    trackId = id,
    title = title,
    image = album?.image,
    artist = artist.name,
    isDownloaded = isDownloaded,
    onItemClicked = { onItemClicked(index) },
    onIconClicked = {
        if (onIconClicked != null) {
            onIconClicked(this)
        }
    },
    isVisible = isVisible
)