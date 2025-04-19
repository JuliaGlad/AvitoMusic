package myapplication.android.musicavito.ui.track_launch.viewpager

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

class ZoomOutPageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(view: View, position: Float) {
        val scale = if (position < -1 || position > 1) 0.85f else 0.85f + (1 - abs(position)) * 0.15f
        view.scaleX = scale
        view.scaleY = scale
        view.alpha = 0.5f + (scale - 0.85f) / 0.15f * 0.5f
    }
}