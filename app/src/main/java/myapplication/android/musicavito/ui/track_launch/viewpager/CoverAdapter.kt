package myapplication.android.musicavito.ui.track_launch.viewpager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import myapplication.android.feature_tracks_launch_ui.R
import myapplication.android.musicavito.databinding.ItemCoverBinding
import kotlin.random.Random

class CoverAdapter(
    private val covers: List<String?>,
    private val viewPager: ViewPager2
) : RecyclerView.Adapter<CoverAdapter.CoverViewHolder>() {

    private var previousPosition = -1

    inner class CoverViewHolder(val binding: ItemCoverBinding) :
        RecyclerView.ViewHolder(binding.root)

    init {
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (previousPosition != -1) notifyItemChanged(previousPosition)
                notifyItemChanged(position)
                previousPosition = position
            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoverViewHolder {
        val binding = ItemCoverBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CoverViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CoverViewHolder, position: Int) {
        val image = covers[position]
        if (image != null) {
            holder.binding.coverImage.setImage(image.toUri())
        } else {
            val imageResources = intArrayOf(
                R.drawable.blue_note,
                R.drawable.green_note,
                R.drawable.violet_note,
                R.drawable.red_note
            )
            val randomIndex = Random.nextInt(imageResources.size)
            val imageId = imageResources[randomIndex]
            holder.binding.coverImage.setImage(imageId)
        }

        val currentItem = viewPager.currentItem
        val scale = if (position == currentItem) 1.15f else 0.85f
        holder.binding.coverImage.scaleX = scale
        holder.binding.coverImage.scaleY = scale
    }

    override fun getItemCount(): Int = covers.size
}
