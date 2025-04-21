package myapplication.android.musicavito.ui.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.media.session.MediaButtonReceiver
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import myapplication.android.musicavito.R
import myapplication.android.musicavito.ui.model.TrackUi
import myapplication.android.musicavito.ui.track_launch.TracksLauncherActivity
import java.util.Locale
import java.util.concurrent.TimeUnit

@UnstableApi
class MusicPlayerService : Service() {

    private lateinit var player: ExoPlayer
    private lateinit var mediaSession: MediaSessionCompat
    private var currentPosition: Int = -1
    private var resumePosition: Long = 0L
    private var tracks: List<TrackUi> = emptyList()
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var progressRunnable: Runnable

    override fun onCreate() {
        super.onCreate()
        player = ExoPlayer.Builder(this).build()
        createNotificationChannel()
        mediaSession = MediaSessionCompat(this, MUSIC_SESSION)
        mediaSession.isActive = true
        mediaSession.setCallback(object : MediaSessionCompat.Callback() {
            override fun onSeekTo(pos: Long) {
                player.seekTo(pos)
            }
        })
        player.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                showNotification(isPlaying)
            }
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                if (playbackState == Player.STATE_ENDED) skipToNextTrack(true)
            }
        })
        startProgressUpdates()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                MUSIC_SESSION,
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        MediaButtonReceiver.handleIntent(mediaSession, intent)
        intent?.let {
            when (it.action) {
                ACTION_NEXT -> skipToNextTrack(player.isPlaying)
                ACTION_PREVIOUS -> skipToPreviousTrack(player.isPlaying)
                ACTION_PLAY_PAUSE -> {
                    if (player.isPlaying) {
                        player.pause()
                    } else {
                        player.play()
                    }
                    showNotification()
                }
                else -> {
                    tracks = Gson().fromJson(
                        it.getStringExtra(TRACKS_LIST),
                        object : TypeToken<List<TrackUi>>() {}.type
                    )
                    currentPosition = it.getIntExtra(CURRENT_POSITION, -1)
                    resumePosition = it.getLongExtra(SEEK_TO, 0L)
                    val isPlayingDefault = it.getBooleanExtra(IS_PLAYING, true)
                    if (currentPosition in tracks.indices) {
                        playCurrentTrack(isPlayingDefault)
                    }
                }
            }
        }
        return START_STICKY
    }

    private fun playCurrentTrack(isPlayingDefault: Boolean? = null) {
        val track = tracks[currentPosition]
        val mediaItem = MediaItem.fromUri(track.audio)
        player.setMediaItem(mediaItem)
        player.seekTo(resumePosition)
        player.prepare()
        if (isPlayingDefault == true) player.play()
        else player.pause()

        mediaSession.setMetadata(
            MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, track.title)
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, track.artist.name)
                .build()
        )
        showNotification(isPlayingDefault)
    }

    private fun skipToPreviousTrack(isPlayingDefault: Boolean? = null) {
        currentPosition = if (currentPosition - 1 < 0) tracks.size - 1 else currentPosition - 1
        resumePosition = 0
        playCurrentTrack(isPlayingDefault)
    }

    private fun skipToNextTrack(isPlayingDefault: Boolean? = null) {
        currentPosition = (currentPosition + 1) % tracks.size
        resumePosition = 0
        playCurrentTrack(isPlayingDefault)
    }

    private fun showNotification(isPlayingDefault: Boolean? = null) {
        val track = tracks.getOrNull(currentPosition) ?: return
        val isPlaying = isPlayingDefault ?: player.isPlaying

        val playPauseIcon = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play
        val contentPendingIntent = createMainPendingIntent()
        val remoteView = initRemoteView(track, playPauseIcon, contentPendingIntent)
        val builder = createNotificationBuilder(remoteView)
        val notification = builder.build()
        startForeground(1, notification)
        loadTrackImage(track, remoteView, builder)
    }

    private fun createNotificationBuilder(
        remoteView: RemoteViews
    ): NotificationCompat.Builder {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_music_note)
            .setCustomBigContentView(remoteView)
            .setOnlyAlertOnce(true)
            .setShowWhen(false)
        return builder
    }

    private fun initRemoteView(
        track: TrackUi,
        playPauseIcon: Int,
        contentPendingIntent: PendingIntent?
    ): RemoteViews {
        val duration = player.duration
        val remaining = if (duration != C.TIME_UNSET && duration > 0)
            duration - player.currentPosition
        else
            0L
        val remoteView = RemoteViews(packageName, R.layout.music_notification).apply {
            setTextViewText(R.id.notification_track_title, track.title)
            setTextViewText(R.id.notification_track_artist, track.artist.name)
            setTextViewText(R.id.notification_current_time, formatTime(player.currentPosition))
            setTextViewText(
                R.id.notification_duration,
                "-${formatTime(remaining)}"
            )
            setImageViewResource(
                R.id.notification_pause_play,
                playPauseIcon
            )
            setProgressBar(
                R.id.notification_seekBar,
                1000,
                ((player.currentPosition.toFloat() / player.duration.toFloat()) * 1000).toInt(),
                false
            )
            setOnClickPendingIntent(R.id.notification_layout, contentPendingIntent)
            setOnClickPendingIntent(
                R.id.notification_play_previous,
                getActionIntent(ACTION_PREVIOUS)
            )
            setOnClickPendingIntent(
                R.id.notification_pause_play,
                getActionIntent(ACTION_PLAY_PAUSE)
            )
            setOnClickPendingIntent(
                R.id.notification_play_next,
                getActionIntent(ACTION_NEXT)
            )
        }
        return remoteView
    }

    private fun loadTrackImage(
        track: TrackUi,
        remoteView: RemoteViews,
        builder: NotificationCompat.Builder
    ) {
        Glide.with(this)
            .asBitmap()
            .load(track.album?.image)
            .transform(RoundedCorners(32))
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    remoteView.setImageViewBitmap(R.id.notification_track_image, resource)
                    val updatedNotification = builder.build()
                    val notificationManager =
                        getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.notify(1, updatedNotification)
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }

    private fun createMainPendingIntent(): PendingIntent? {
        val intent = Intent(this, TracksLauncherActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra(CURRENT_POSITION, currentPosition)
            putExtra(SEEK_TO, player.currentPosition)
            putExtra(IS_PLAYING, player.isPlaying)
        }

        val contentPendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        return contentPendingIntent
    }

    private fun updateNotificationProgress() {
        val remoteView = RemoteViews(packageName, R.layout.music_notification)
        val progress = if (player.duration > 0)
            ((player.currentPosition.toFloat() / player.duration) * 1000).toInt()
        else 0
        val duration = player.duration
        val remaining = if (duration != C.TIME_UNSET && duration > 0)
            duration - player.currentPosition
        else
            0L
        remoteView.apply{
            setProgressBar(R.id.notification_seekBar, 1000, progress, false)
            setTextViewText(R.id.notification_current_time, formatTime(player.currentPosition))
            setTextViewText(R.id.notification_duration, "-${formatTime(remaining)}")
        }
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_music_note)
            .setCustomContentView(remoteView)
            .setCustomBigContentView(remoteView)
            .setOnlyAlertOnce(true)
            .setShowWhen(false)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, builder.build())
    }

    private fun getActionIntent(action: String): PendingIntent {
        val intent = Intent(this, MusicPlayerService::class.java).apply { this.action = action }
        val requestCode = action.hashCode()
        return PendingIntent.getService(
            this,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun startProgressUpdates() {
        progressRunnable = object : Runnable {
            override fun run() {
                updateNotificationProgress()
                handler.postDelayed(this, 1000)
            }
        }
        handler.post(progressRunnable)
    }

    private fun formatTime(ms: Long): String {
        val minutes = TimeUnit.MICROSECONDS.toMinutes(ms)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(ms)
        return String.format(Locale.getDefault(), "%d:%02d", minutes, seconds)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(progressRunnable)
        mediaSession.release()
        player.release()
        stopForeground(true)
    }

    companion object {
        const val TRACKS_LIST = "TracksList"
        const val CHANNEL_ID = "ChannelId"
        const val CURRENT_POSITION = "CurrentPosition"
        const val MUSIC_SESSION = "MusicSession"
        const val ACTION_NEXT = "ActionNext"
        const val ACTION_PREVIOUS = "ActionPrevious"
        const val ACTION_PLAY_PAUSE = "ActionPlayPause"
        const val SEEK_TO = "SeekTo"
        const val IS_PLAYING = "IsPlaying"
    }
}