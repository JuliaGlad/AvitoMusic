package myapplication.android.core_ui.custom_view

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import myapplication.android.feature_tracks_launch_ui.R

class RoundedImageView @JvmOverloads constructor(
    context: Context,
    defAttrs: AttributeSet? = null,
    defStyle: Int = 0
) : AppCompatImageView(context, defAttrs, defStyle) {

    init {
        clipToOutline = true
        setBackgroundResource(R.drawable.bg_round_image)
        scaleType = ScaleType.CENTER_CROP
    }

    fun setImage(uri: Uri){
        Glide.with(context)
            .load(uri)
            .into(this)
    }

    fun setImage(drawable: Int){
        setImageDrawable(ResourcesCompat.getDrawable(resources, drawable, context.theme))
    }

}