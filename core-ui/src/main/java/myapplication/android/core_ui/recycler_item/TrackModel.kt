package myapplication.android.core_ui.recycler_item

import myapplication.android.core_ui.listener.ClickListener
import myapplication.android.feature_tracks_launch_ui.R

data class TrackModel(
    val id: Int,
    val trackId: Long,
    val title: String,
    val image: String?,
    val artist: String,
    var isDownloaded: Boolean,
    val onItemClicked: () -> Unit,
    val onIconClicked: (() -> Unit)? = null,
    val isVisible: Boolean = true
)