package myapplication.android.core_ui.custom_view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.content.withStyledAttributes
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import myapplication.android.core_ui.recycler_item.SwipeGesture
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
        LayoutInflater.from(context), this, true
    )

    private val recyclerItems: MutableList<TrackModel> = mutableListOf()

    var onSwipeToDeleteClickListener: ((TrackModel) -> Unit)? = null

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
        context.withStyledAttributes(attrs, R.styleable.TracksListView) {
            val swipeToDelete = getBoolean(R.styleable.TracksListView_swipeToDelete, false)
            if (swipeToDelete) {
                val swipeGesture = object : SwipeGesture(context) {
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        when (direction) {
                            ItemTouchHelper.LEFT -> {
                                val position = viewHolder.adapterPosition
                                val deleted = getItem(position)
                                recyclerItems.removeAt(position)
                                adapter.notifyItemRemoved(position)
                                onSwipeToDeleteClickListener?.invoke(deleted)
                                if (recyclerItems.isEmpty()) showEmptyLayout()
                            }
                        }
                    }
                }
                val touchHelper = ItemTouchHelper(swipeGesture)
                touchHelper.attachToRecyclerView(recyclerView)
            }
        }
    }

    fun getItem(position: Int): TrackModel = recyclerItems[position]

    fun isEmpty(): Boolean = recyclerItems.isEmpty() && emptyView.visibility == GONE

    fun updateItemDownloaded(id: Long, isDownloaded: Boolean) {
        for (i in recyclerItems) {
            if (i.trackId == id) {
                val index = recyclerItems.indexOf(i)
                recyclerItems[index].apply {
                    this.isDownloaded = isDownloaded
                    isLoading = false
                }
                adapter.notifyItemChanged(index)
            }
        }
    }

    private fun showEmptyLayout(){
        binding.emptyList.visibility = VISIBLE
        binding.emptyList.setTitle(
            context.getString(R.string.looks_like_you_dont_have_any_tracks_yet)
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearItems() {
        recyclerItems.clear()
        adapter.notifyDataSetChanged()
    }

    fun setItems(tracks: List<TrackModel>) {
        recyclerItems.addAll(tracks)
        adapter.submitList(recyclerItems)
    }

}