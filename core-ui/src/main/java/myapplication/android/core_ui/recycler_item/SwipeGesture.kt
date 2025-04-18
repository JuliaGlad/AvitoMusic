package myapplication.android.core_ui.recycler_item

import android.content.Context
import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import myapplication.android.feature_tracks_launch_ui.R

abstract class SwipeGesture(private val context: Context): ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {

        RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            .addSwipeLeftBackgroundColor(context.getColor(R.color.light_red))
            .addSwipeLeftActionIcon(R.drawable.ic_delete)
            .addSwipeLeftLabel(context.getString(R.string.delete))
            .setSwipeLeftLabelColor(context.getColor(R.color.white))

            .setSwipeLeftActionIconTint(context.getColor(R.color.white))
            .create()
            .decorate()

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

}