package myapplication.android.core_ui.custom_view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import myapplication.android.feature_tracks_launch_ui.databinding.ErrorLayoutBinding

class ErrorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding: ErrorLayoutBinding =
        ErrorLayoutBinding.inflate(LayoutInflater.from(context), this, true)

    var onTryAgainClickListener: (() -> Unit)? = null

    init {
        binding.tryAgain.setOnClickListener {
            onTryAgainClickListener?.invoke()
        }
    }
}