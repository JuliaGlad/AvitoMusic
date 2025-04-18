package myapplication.android.core_ui.recycler_item

import androidx.recyclerview.widget.DiffUtil

class TrackCallback: DiffUtil.ItemCallback<TrackModel>() {
    override fun areItemsTheSame(oldItem: TrackModel, newItem: TrackModel): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: TrackModel, newItem: TrackModel): Boolean =
        oldItem == newItem
}