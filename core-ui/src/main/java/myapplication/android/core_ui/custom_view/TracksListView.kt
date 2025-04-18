package myapplication.android.core_ui.custom_view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import myapplication.android.core_ui.recycler_item.TrackAdapter
import myapplication.android.core_ui.recycler_item.TrackModel
import myapplication.android.feature_tracks_launch_ui.R
import myapplication.android.feature_tracks_launch_ui.databinding.RecyclerViewSearchBinding

class TracksListView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: RecyclerViewSearchBinding = RecyclerViewSearchBinding.inflate(
        LayoutInflater.from(context), this, true)

    private val recyclerItems: MutableList<TrackModel> = mutableListOf()

    val error: ErrorView
        get() = binding.error

    val loading: LoadingView
        get() = binding.loading

    private val recyclerView: RecyclerView
        get() = binding.recyclerView

    val searchView: EditText
        get() = binding.searchEditText

    val emptyView: EmptyListView
        get() = binding.emptyList

    private val adapter: TrackAdapter = TrackAdapter()

    init {
        orientation = VERTICAL
        recyclerView.adapter = adapter
    }

    fun isEmpty() = recyclerItems.isEmpty() && emptyView.visibility == GONE

    fun updateItem(id: Long, isDownloaded: Boolean) {
        for (i in recyclerItems){
            if (i.trackId == id){
                val index = recyclerItems.indexOf(i)
                recyclerItems[index].isDownloaded = isDownloaded
                adapter.notifyItemChanged(index)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearItems() {
        recyclerItems.clear()
        adapter.notifyDataSetChanged()
    }

    fun setItems(tracks: List<TrackModel>){
        recyclerItems.addAll(tracks)
        adapter.submitList(recyclerItems)
    }

}