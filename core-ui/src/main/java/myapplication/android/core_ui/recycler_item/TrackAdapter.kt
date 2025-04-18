package myapplication.android.core_ui.recycler_item

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import myapplication.android.feature_tracks_launch_ui.R
import myapplication.android.feature_tracks_launch_ui.databinding.RecyclerViewTrackItemBinding
import kotlin.random.Random

class TrackAdapter : ListAdapter<TrackModel, RecyclerView.ViewHolder>(TrackCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ViewHolder(
            RecyclerViewTrackItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(getItem(position))
    }

    class ViewHolder(
        private val binding: RecyclerViewTrackItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(track: TrackModel) {
            with(track) {
                binding.title.text = title
                binding.artist.text = artist
                if (image != null) {
                    binding.image.setImage(image.toUri())
                } else {
                    val imageResources = intArrayOf(
                        R.drawable.blue_note,
                        R.drawable.green_note,
                        R.drawable.violet_note,
                        R.drawable.red_note
                    )
                    val randomIndex = Random.nextInt(imageResources.size)
                    val imageId = imageResources[randomIndex]
                    binding.image.setImage(imageId)
                }
                if (isVisible) {
                    val iconId =
                        if (isDownloaded) R.drawable.ic_download_done
                        else R.drawable.ic_download
                    val icon = ResourcesCompat.getDrawable(
                        itemView.resources,
                        iconId,
                        itemView.context.theme
                    )
                    binding.icon.setImageDrawable(icon)
                } else binding.icon.visibility = GONE
                binding.item.setOnClickListener { onItemClicked() }
                binding.icon.setOnClickListener { onIconClicked?.let { action -> action() } }
            }
        }

    }
}