package myapplication.android.core_ui.recycler_item

data class TrackModel(
    val id: Int,
    val trackId: Long,
    val title: String,
    val image: String?,
    val artist: String,
    var isDownloaded: Boolean,
    var isLoading: Boolean = false,
    val onItemClicked: () -> Unit,
    val onIconClicked: (() -> Unit)? = null,
    val isVisible: Boolean = true
)