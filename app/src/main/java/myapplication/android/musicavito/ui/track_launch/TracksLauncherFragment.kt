package myapplication.android.musicavito.ui.track_launch

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Build.VERSION
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.activity.OnBackPressedCallback
import androidx.annotation.OptIn
import androidx.core.content.res.ResourcesCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import jp.wasabeef.glide.transformations.BlurTransformation
import myapplication.android.musicavito.R
import myapplication.android.musicavito.databinding.FragmentTracksLaunchBinding
import myapplication.android.musicavito.ui.model.TrackUi
import myapplication.android.musicavito.ui.service.MusicPlayerService
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
    private var userInitiatedScroll: Boolean = false
    private var startService: Boolean = true

    private val handler = Handler(Looper.getMainLooper())
    private val updateSeekRunnable = object : Runnable {
        override fun run() {
            binding.seekBar.progress = player.currentPosition.toInt()
            binding.currentTime.text = formatTime(player.currentPosition)
            binding.duration.text = "-${formatTime(player.duration - player.currentPosition)}"
            handler.postDelayed(this, 500)
        }
    }

    @OptIn(UnstableApi::class)
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

    @OptIn(UnstableApi::class)
    override fun onResume() {
        super.onResume()
        if (checkForegroundServiceRunning()) {
            activity?.intent?.let {
                currentPosition = it.getIntExtra(CURRENT_POSITION, -1)
                val seekTo = it.getLongExtra(SEEK_TO, 0L)
                val isPlaying = it.getBooleanExtra(IS_PLAYING, true)
                playCurrentTrack(isPlaying, seekTo)
            }
            val stopIntent = Intent(requireContext(), MusicPlayerService::class.java)
            requireContext().stopService(stopIntent)
        }
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
        player = ExoPlayer.Builder(requireContext()).build()
        initViewPager()
        initButtonBack()
        playCurrentTrack()
        initControllers()
        handleBackButtonPressed()
        initSeekbar()
        addPlayerListener()
    }

    private fun addPlayerListener() {
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
                        playCurrentTrack()
                    }
                }
            }
        })
    }

    private fun initViewPager() {
        with(binding.viewPager) {
            adapter = CoverAdapter(covers, this)
            currentPosition?.let { setCurrentItem(it, false) }
            setPageTransformer(ZoomOutPageTransformer())
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrollStateChanged(state: Int) {
                    if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                        userInitiatedScroll = true
                    } else if (state == ViewPager2.SCROLL_STATE_IDLE) {
                        userInitiatedScroll = false
                    }
                }

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if (userInitiatedScroll && position != currentPosition) {
                        currentPosition = position
                        playCurrentTrack()
                    }
                }
            })
        }
    }

    private fun initButtonBack() {
        binding.close.setOnClickListener { finishActivity() }
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

    @OptIn(UnstableApi::class)
    private fun playCurrentTrack(isPlaying: Boolean? = null, seekTo: Long = 0L) {
        val currentTrack: TrackUi? = currentPosition?.let {
            tracks[it]
        }
        player.stop()
        player.clearMediaItems()
        val mediaItem = currentTrack?.let {
            MediaItem.fromUri(it.audio.toUri())
        }
        mediaItem?.let { player.setMediaItem(it) }
        player.seekTo(seekTo)
        player.prepare()
        val playerIcon : Int
        if (isPlaying == null || isPlaying == true) {
            player.play()
            playerIcon = R.drawable.ic_pause
        }
        else {
            player.pause()
            playerIcon = R.drawable.ic_play
        }
        binding.pausePlay.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                playerIcon,
                context?.theme
            )
        )
        binding.trackTitle.text = currentTrack?.title
        binding.trackArtist.text = currentTrack?.artist?.name
        currentPosition?.let {
            binding.viewPager.post {
                binding.viewPager.setCurrentItem(it, true)
            }
        }
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


    private fun finishActivity() {
        requireActivity().finish()
        startService = false
    }

    private fun handleBackButtonPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishActivity()
            }
        })
    }

    @OptIn(UnstableApi::class)
    private fun checkForegroundServiceRunning(): Boolean {
        val manager = requireContext().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (MusicPlayerService::class.java.name == service.service.className) {
                if (service.foreground) {
                    return true
                }
            }
        }
        return false
    }

    @OptIn(UnstableApi::class)
    override fun onStop() {
        super.onStop()
        if (::player.isInitialized && currentPosition in tracks.indices && startService) {
            val resumePosition = player.currentPosition

            val intent = Intent(requireContext(), MusicPlayerService::class.java).apply {
                action = null
                putExtra(TRACKS_LIST, Gson().toJson(tracks))
                putExtra(CURRENT_POSITION, currentPosition)
                putExtra(SEEK_TO, resumePosition)
                putExtra(IS_PLAYING, player.isPlaying)
            }
            if (VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                requireContext().startForegroundService(intent)
            } else {
                requireContext().startService(intent)
            }
            player.stop()
        }
    }

    @OptIn(UnstableApi::class)
    override fun onDestroy() {
        super.onDestroy()
        player.release()
        handler.removeCallbacks(updateSeekRunnable)
        _binding = null
    }

    companion object {
        const val TRACKS_EXTRA = "TracksExtra"
        const val CURRENT_POSITION = "CurrentPosition"
        const val TRACKS_LIST = "TracksList"
        const val SEEK_TO = "SeekTo"
        const val IS_PLAYING = "IsPlaying"
    }

}