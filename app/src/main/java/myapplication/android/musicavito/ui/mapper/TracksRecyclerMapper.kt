package myapplication.android.musicavito.ui.mapper

import myapplication.android.core_ui.recycler_item.TrackModel
import myapplication.android.musicavito.ui.model.TrackUi
import myapplication.android.musicavito.ui.model.TracksUiList
import java.util.stream.Collectors

fun TracksUiList.toRecyclerItems(
    onItemClicked: (String) -> Unit,
    onIconClicked: (TrackUi) -> Unit
): MutableList<TrackModel> = tracks.stream()
    .map {
        it.toRecyclerItem(
            tracks.indexOf(it),
            onItemClicked, onIconClicked
        )
    }.collect(Collectors.toList())

fun List<TrackUi>.toRecyclerItems(
    onItemClicked: (String) -> Unit,
    onIconClicked: ((TrackUi) -> Unit)? = null
): MutableList<TrackModel> = stream()
    .map {
        it.toRecyclerItem(
            indexOf(it),
            onItemClicked, onIconClicked
        )
    }.collect(Collectors.toList())

fun TrackUi.toRecyclerItem(
    index: Int,
    onItemClicked: (String) -> Unit,
    onIconClicked: ((TrackUi) -> Unit)?
) = TrackModel(
    id = index,
    trackId = id,
    title = title,
    image = album?.image,
    artist = artist.name,
    isDownloaded = isDownloaded,
    onItemClicked = { onItemClicked(audio) },
    onIconClicked = {
        if (onIconClicked != null) {
            onIconClicked(this)
        }
    }
)