package myapplication.android.musicavito.ui.track_launch

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.res.ResourcesCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import jp.wasabeef.glide.transformations.BlurTransformation
import myapplication.android.musicavito.R
import myapplication.android.musicavito.databinding.FragmentTracksLaunchBinding
import myapplication.android.musicavito.ui.model.TrackUi
import myapplication.android.musicavito.ui.track_launch.viewpager.CoverAdapter
import myapplication.android.musicavito.ui.track_launch.viewpager.ZoomOutPageTransformer
import java.util.Locale
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors


class TracksLauncherFragment : Fragment() {

    private lateinit var player: ExoPlayer

    private var _binding: FragmentTracksLaunchBinding? = null
    private val binding get() = _binding!!

    private var _tracks: List<TrackUi>? = null
    private val tracks get() = _tracks!!

    private var _covers: List<String?>? = null
    private val covers: List<String?> get() = _covers!!

    private var currentPosition: Int? = -1

    private val handler = Handler(Looper.getMainLooper())
    private val updateSeekRunnable = object : Runnable {
        override fun run() {
            binding.seekBar.progress = player.currentPosition.toInt()
            binding.currentTime.text = formatTime(player.currentPosition)
            handler.postDelayed(this, 500)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentPosition = activity?.intent?.getIntExtra(CURRENT_POSITION, -1)
        _tracks = Gson().fromJson(
            activity?.intent?.getStringExtra(TRACKS_EXTRA),
            object : TypeToken<List<TrackUi>>() {}.type
        )
        _covers = tracks.stream()
            .map { it.album?.image }
            .collect(Collectors.toList())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTracksLaunchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.viewPager) {
            adapter = CoverAdapter(covers, this)
            currentPosition?.let { setCurrentItem(it, false) }
            setPageTransformer(ZoomOutPageTransformer())
        }

        player = ExoPlayer.Builder(requireContext()).build()
        initButtonBack()
        playCurrentTrack()
        initControllers()
        initSeekbar()
        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                if (playbackState == Player.STATE_READY) {
                    binding.seekBar.max = player.duration.toInt()
                    binding.duration.text = formatTime(player.duration)
                    handler.post(updateSeekRunnable)
                } else if (playbackState == Player.STATE_ENDED) {
                    currentPosition?.let {
                        currentPosition = (it + 1) % tracks.size
                        binding.viewPager.setCurrentItem(it, true)
                        playCurrentTrack()
                    }
                }
            }
        })
    }

    private fun initButtonBack() {
        binding.close.setOnClickListener { requireActivity().finish() }
    }

    private fun initSeekbar() {
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) player.seekTo(progress.toLong())
            }

            override fun onStartTrackingTouch(sb: SeekBar?) {}

            override fun onStopTrackingTouch(sb: SeekBar?) {}
        })
    }

    private fun initControllers() {
        with(binding.pausePlay) {
            setOnClickListener {
                if (player.isPlaying) {
                    player.pause()
                    setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.ic_play,
                            context?.theme
                        )
                    )
                } else {
                    player.play()
                    setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.ic_pause,
                            context?.theme
                        )
                    )
                }
            }
        }
        binding.playNext.setOnClickListener {
            currentPosition?.let {
                currentPosition = (it + 1) % tracks.size
                binding.viewPager.setCurrentItem(it, true)
                playCurrentTrack()
            }
        }
        binding.playPrevious.setOnClickListener {
            currentPosition?.let {
                currentPosition = if (it - 1 < 0) tracks.size - 1
                else it - 1
            }
            playCurrentTrack()
        }
    }

    private fun playCurrentTrack() {
        val currentTrack: TrackUi? = currentPosition?.let {
            tracks[it]
        }
        player.stop()
        player.clearMediaItems()
        val mediaItem = currentTrack?.let {
            MediaItem.fromUri(it.audio.toUri())
        }
        mediaItem?.let { player.setMediaItem(it) }
        player.prepare()
        player.play()
        binding.pausePlay.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_pause,
                context?.theme
            )
        )
        binding.trackTitle.text = currentTrack?.title
        binding.trackArtist.text = currentTrack?.artist?.name
        currentPosition?.let { binding.viewPager.setCurrentItem(it, true) }
        Glide.with(this)
            .load(currentTrack?.album?.image)
            .apply(RequestOptions.bitmapTransform(BlurTransformation(32, 3)))
            .into(binding.background)
        binding.background.alpha = 0.8f
    }

    private fun formatTime(ms: Long): String {
        val minutes = TimeUnit.MICROSECONDS.toMinutes(ms)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(ms)
        return String.format(Locale.getDefault(), "%d:%02d", minutes, seconds)
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
        _binding = null
    }

    companion object {
        const val TRACKS_EXTRA = "TracksExtra"
        const val CURRENT_POSITION = "CurrentPosition"
    }

}