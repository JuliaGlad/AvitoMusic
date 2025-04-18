package myapplication.android.core_ui.recycler_item

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.text.TextPaint
import android.util.Log
import android.util.TypedValue
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewSwipeDecorator private constructor() {
    private var canvas: Canvas? = null
    private var recyclerView: RecyclerView? = null
    private var viewHolder: RecyclerView.ViewHolder? = null
    private var dX = 0f
    private var dY = 0f
    private var actionState = 0
    private var isCurrentlyActive = false

    private var swipeLeftBackgroundColor = 0
    private var swipeLeftActionIconId = 0
    private var swipeLeftActionIconTint: Int? = null

    private var iconHorizontalMargin = 0

    private var mSwipeLeftText: String? = null
    private var mSwipeLeftTextSize = 14f
    private var mSwipeLeftTextUnit = TypedValue.COMPLEX_UNIT_SP
    private var mSwipeLeftTextColor = Color.DKGRAY
    private var mSwipeLeftTypeface: Typeface = Typeface.SANS_SERIF

    private var mSwipeLeftCornerRadius = 0f

    private var mSwipeLeftPadding: IntArray = intArrayOf(0, 0, 0)

    constructor(
        canvas: Canvas?,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder?,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) : this() {
        this.canvas = canvas
        this.recyclerView = recyclerView
        this.viewHolder = viewHolder
        this.dX = dX
        this.dY = dY
        this.actionState = actionState
        this.isCurrentlyActive = isCurrentlyActive
        this.iconHorizontalMargin = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            16f,
            recyclerView.context.resources.displayMetrics
        ).toInt()
    }

    fun setSwipeLeftBackgroundColor(swipeLeftBackgroundColor: Int) {
        this.swipeLeftBackgroundColor = swipeLeftBackgroundColor
    }

    fun setSwipeLeftActionIconId(swipeLeftActionIconId: Int) {
        this.swipeLeftActionIconId = swipeLeftActionIconId
    }

    fun setSwipeLeftActionIconTint(color: Int) {
        swipeLeftActionIconTint = color
    }

    fun setSwipeLeftLabel(label: String?) {
        mSwipeLeftText = label
    }

    fun setSwipeLeftTextColor(color: Int) {
        mSwipeLeftTextColor = color
    }

    fun decorate() {
        try {
            if (actionState != ItemTouchHelper.ACTION_STATE_SWIPE) return

           if (dX < 0) {
                canvas!!.clipRect(
                    viewHolder!!.itemView.right + dX.toInt(),
                    viewHolder!!.itemView.top,
                    viewHolder!!.itemView.right,
                    viewHolder!!.itemView.bottom
                )
                if (swipeLeftBackgroundColor != 0) {
                    if (mSwipeLeftCornerRadius != 0f) {
                        val background = GradientDrawable()
                        background.setColor(swipeLeftBackgroundColor)
                        background.setBounds(
                            viewHolder!!.itemView.right + dX.toInt(),
                            viewHolder!!.itemView.top + mSwipeLeftPadding[0],
                            viewHolder!!.itemView.right - mSwipeLeftPadding[1],
                            viewHolder!!.itemView.bottom - mSwipeLeftPadding[2]
                        )
                        background.cornerRadii = floatArrayOf(
                            0f,
                            0f,
                            mSwipeLeftCornerRadius,
                            mSwipeLeftCornerRadius,
                            mSwipeLeftCornerRadius,
                            mSwipeLeftCornerRadius,
                            0f,
                            0f
                        )
                        background.draw(canvas!!)
                    } else {
                        val background = ColorDrawable(swipeLeftBackgroundColor)
                        background.setBounds(
                            viewHolder!!.itemView.right + dX.toInt(),
                            viewHolder!!.itemView.top + mSwipeLeftPadding[0],
                            viewHolder!!.itemView.right - mSwipeLeftPadding[1],
                            viewHolder!!.itemView.bottom - mSwipeLeftPadding[2]
                        )
                        background.draw(canvas!!)
                    }
                }

                var iconSize = 0
                var imgLeft = viewHolder!!.itemView.right
                if (swipeLeftActionIconId != 0 && dX < -iconHorizontalMargin) {
                    val icon = ContextCompat.getDrawable(
                        recyclerView!!.context, swipeLeftActionIconId
                    )
                    if (icon != null) {
                        iconSize = icon.intrinsicHeight
                        val halfIcon = iconSize / 2
                        val top =
                            viewHolder!!.itemView.top + ((viewHolder!!.itemView.bottom - viewHolder!!.itemView.top) / 2 - halfIcon)
                        imgLeft =
                            viewHolder!!.itemView.right - iconHorizontalMargin - mSwipeLeftPadding[1] - halfIcon * 2
                        icon.setBounds(
                            imgLeft,
                            top,
                            viewHolder!!.itemView.right - iconHorizontalMargin - mSwipeLeftPadding[1],
                            top + icon.intrinsicHeight
                        )
                        if (swipeLeftActionIconTint != null) icon.setColorFilter(
                            swipeLeftActionIconTint!!, PorterDuff.Mode.SRC_IN
                        )
                        icon.draw(canvas!!)
                    }
                }

                if (mSwipeLeftText != null && mSwipeLeftText!!.isNotEmpty() && dX < -iconHorizontalMargin - mSwipeLeftPadding[1] - iconSize) {
                    val textPaint = TextPaint()
                    textPaint.isAntiAlias = true
                    textPaint.textSize = TypedValue.applyDimension(
                        mSwipeLeftTextUnit,
                        mSwipeLeftTextSize,
                        recyclerView!!.context.resources.displayMetrics
                    )
                    textPaint.color = mSwipeLeftTextColor
                    textPaint.setTypeface(mSwipeLeftTypeface)

                    val width = textPaint.measureText(mSwipeLeftText)
                    val textTop =
                        (viewHolder!!.itemView.top + ((viewHolder!!.itemView.bottom - viewHolder!!.itemView.top) / 2.0) + textPaint.textSize / 2).toInt()
                    canvas!!.drawText(
                        mSwipeLeftText!!,
                        imgLeft - width - (if (imgLeft == viewHolder!!.itemView.right) iconHorizontalMargin else iconHorizontalMargin / 2),
                        textTop.toFloat(),
                        textPaint
                    )
                }
            }
        } catch (e: Exception) {
            Log.e(javaClass.name, e.message!!)
        }
    }

    class Builder(
        canvas: Canvas?,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder?,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        private val mDecorator: RecyclerViewSwipeDecorator = RecyclerViewSwipeDecorator(
            canvas,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )

        fun addSwipeLeftBackgroundColor(color: Int): Builder {
            mDecorator.setSwipeLeftBackgroundColor(color)
            return this
        }

        fun addSwipeLeftActionIcon(drawableId: Int): Builder {
            mDecorator.setSwipeLeftActionIconId(drawableId)
            return this
        }

        fun setSwipeLeftActionIconTint(color: Int): Builder {
            mDecorator.setSwipeLeftActionIconTint(color)
            return this
        }

        fun addSwipeLeftLabel(label: String?): Builder {
            mDecorator.setSwipeLeftLabel(label)
            return this
        }

        fun setSwipeLeftLabelColor(color: Int): Builder {
            mDecorator.setSwipeLeftTextColor(color)
            return this
        }

        fun create(): RecyclerViewSwipeDecorator {
            return mDecorator
        }
    }
}