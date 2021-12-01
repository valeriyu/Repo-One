package com.valeriyu.backgroundwork.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.content.*
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.AudioManager.OnAudioFocusChangeListener
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.media.MediaBrowserServiceCompat
import androidx.media.session.MediaButtonReceiver
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSourceFactory
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.extractor.ExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.TransferListener
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.android.exoplayer2.util.Util
import com.valeriyu.backgroundwork.MainActivity
import com.valeriyu.backgroundwork.notifications.NotificationChannels
import com.valeriyu.backgroundwork.R
import com.valeriyu.backgroundwork.data.Station
import com.valeriyu.playsound.service.MediaStyleHelper
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import timber.log.Timber
import java.io.File


class MediaBrowserPlayerService : MediaBrowserServiceCompat() {

    private val stationsList = listOf<Station>(
        Station(
            url = "http://chillout.zone/chillout_plus",
            title = "Fréquence 3 Rock/Electronic"
        ),
        Station(
            url =
            "http ://hd.stream.frequence3.net/frequence3.flac",
            title = "Hi On Line Radio Pop"
        ),
        Station(
            url = "http://mscp2.live-streams.nl:8100/flac.flac",
            title = "Intense Radio Electronic"
        ),

        Station(
            url = "http://secure.live-streams.nl/flac.flac",
            title = "Jukebox Radio Rock/Eclectic"
        ),
        Station(
            url = "http://199.189.87.9:10999/flac",
            title = "Radio Paradise Main Mix Eclectic/Rock"
        ),
        Station(
            url = "http://stream.radioparadise.com/flac",
            title = "Radio Paradise Mellow Mix Rock"
        ),
        Station(
            url = "http://stream.radioparadise.com/mellow-flac",
            title = "SECTOR Classical"
        ),
        Station(
            url = "http://89.223.45.5:8000/nota-flac",
            title = "SECTOR Progressive"
        ),
        Station(
            url = "http://89.223.45.5:8000/progressive-flac",
            title = "SECTOR Space Electronic"
        ),
        Station(
            url = "http://89.223.45.5:8000/space-flac",
            title = "SECTOR 80s"
        ),
        Station(
            url = "http://89.223.45.5:8000/geny-flac",
            title = "SECTOR 90s"
        ),
        Station(
            url = "http://89.223.45.5:8000/next-flac",
            title = "Sing Sing Eclectic"
        ),
        Station(
            url = "http://stream.sing-sing-bis.org:8000/singsingFlac",
            title = "The Cheese 80’s, 90’s, 00’s Hits"
        ),
        Station(
            url = "http://thecheese.ddns.net:8004/stream",
            title = "thecheese"
        )
    )

    var currentItemIndex = 0

    private val metadataBuilder = MediaMetadataCompat.Builder()
    private val stateBuilder = PlaybackStateCompat.Builder().setActions(
        PlaybackStateCompat.ACTION_PLAY
                or PlaybackStateCompat.ACTION_STOP
                or PlaybackStateCompat.ACTION_PAUSE
                or PlaybackStateCompat.ACTION_PLAY_PAUSE
                or PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
    )
    private var mediaSession: MediaSessionCompat? = null
    private var audioManager: AudioManager? = null
    private var audioFocusRequest: AudioFocusRequest? = null
    private var audioFocusRequested = false
    private var exoPlayer: SimpleExoPlayer? = null
    private var extractorsFactory: ExtractorsFactory? = null
    private var dataSourceFactory: DataSource.Factory? = null


    @SuppressLint("WrongConstant")
    override fun onCreate() {
        super.onCreate()
        Timber.d("MediaBrowserPlayerService -> onCreate ${this}")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()

            audioFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                .setOnAudioFocusChangeListener(audioFocusChangeListener)
                .setAcceptsDelayedFocusGain(false)
                .setWillPauseWhenDucked(true)
                .setAudioAttributes(audioAttributes)
                .build()
        }

        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager

        mediaSession = MediaSessionCompat(this, "MediaBrowserPlayerService")
        mediaSession!!.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)
        mediaSession!!.setCallback(mediaSessionCallback)
        setSessionToken(mediaSession!!.sessionToken)


        val activityIntent = Intent(applicationContext, MainActivity::class.java).apply {
            action = applicationContext.packageName + ".RADIO_ACTIVE"
        }
        mediaSession!!.setSessionActivity(
            PendingIntent.getActivity(
                applicationContext,
                0,
                activityIntent,
                0
            )
        )

        val mediaButtonIntent =
            Intent(
                Intent.ACTION_MEDIA_BUTTON,
                null,
                applicationContext,
                MediaButtonReceiver::class.java
            )
        mediaSession!!.setMediaButtonReceiver(
            PendingIntent.getBroadcast(
                applicationContext,
                0,
                mediaButtonIntent,
                0
            )
        )

        exoPlayer = ExoPlayerFactory.newSimpleInstance(
            this,
            DefaultRenderersFactory(this),
            DefaultTrackSelector(),
            DefaultLoadControl()
        ).apply {
            addListener(exoPlayerListener)
        }

        val httpDataSourceFactory: DataSource.Factory = OkHttpDataSourceFactory(
            OkHttpClient(),
            Util.getUserAgent(this, getString(R.string.app_name)),
            null as TransferListener?
        )

        val cache = SimpleCache(
            File(this.cacheDir.absolutePath + "/exoplayer"),
            LeastRecentlyUsedCacheEvictor(1024 * 1024 * 100)
        ) // 100 Mb max
        dataSourceFactory = CacheDataSourceFactory(
            cache,
            httpDataSourceFactory,
            CacheDataSource.FLAG_BLOCK_ON_CACHE or CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR
        )
        extractorsFactory = DefaultExtractorsFactory()
  }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        MediaButtonReceiver.handleIntent(mediaSession, intent)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaSession!!.release()
        exoPlayer!!.release()
        dataSourceFactory = null
        extractorsFactory = null
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {
        if (clientPackageName.trim() == applicationContext.packageName) {
            return BrowserRoot("Root", null)
        } else return null
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<List<MediaBrowserCompat.MediaItem>>
    ) {
        val data = mutableListOf<MediaBrowserCompat.MediaItem>()
        val descriptionBuilder = MediaDescriptionCompat.Builder()
        for (i in 0 until stationsList.size - 1) {
            val station = stationsList[i]
            val description = descriptionBuilder
                .setDescription(station.title)
                .setTitle(station.title)
                .setSubtitle(station.title)
                .setMediaId(Integer.toString(i))
                .build()
            data.add(
                MediaBrowserCompat.MediaItem(
                    description,
                    MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
                )
            )
        }
        result.sendResult(data)
    }

    private val mediaSessionCallback: MediaSessionCompat.Callback =
        object : MediaSessionCompat.Callback() {
            private var currentUri: Uri? = null
            var currentState = PlaybackStateCompat.STATE_STOPPED

            override fun onPlayFromMediaId(mediaId: String, extras: Bundle) {
                playTrack(stationsList[mediaId.toInt()])
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onPlay() {
                startForegroundService(
                    Intent(
                        applicationContext,
                        MediaBrowserPlayerService::class.java
                    )
                )
                Timber.d("onPlay Thread -> ${Thread.currentThread().name}")
                playTrack(stationsList[currentItemIndex])
            }

            private fun playTrack(station: Station) {
                updateMetadataFromTrack(stationsList[currentItemIndex])
                prepareToPlay(Uri.parse(station.url))
                if (!exoPlayer!!.playWhenReady) {
                    if (!audioFocusRequested) {
                        audioFocusRequested = true
                        val audioFocusResult: Int
                        audioFocusResult = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            audioManager!!.requestAudioFocus(audioFocusRequest!!)
                        } else {
                            audioManager!!.requestAudioFocus(
                                audioFocusChangeListener,
                                AudioManager.STREAM_MUSIC,
                                AudioManager.AUDIOFOCUS_GAIN
                            )
                        }
                        if (audioFocusResult != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) return
                    }

                    mediaSession!!.isActive = true // Сразу после получения фокуса
                    registerReceiver(
                        becomingNoisyReceiver,
                        IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
                    )

                    exoPlayer!!.playWhenReady = true
                }
                mediaSession!!.setPlaybackState(
                    stateBuilder.setState(
                        PlaybackStateCompat.STATE_PLAYING,
                        PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN,
                        1f
                    ).build()
                )
                currentState = PlaybackStateCompat.STATE_PLAYING
                refreshNotificationAndForegroundStatus(currentState)
            }

            private fun prepareToPlay(uri: Uri) {
                if (uri != currentUri) {
                    currentUri = uri
                    val mediaSource =
                        ExtractorMediaSource(uri, dataSourceFactory, extractorsFactory, null, null)
                    exoPlayer!!.prepare(mediaSource)
                }
            }

            override fun onPause() {
                if (exoPlayer!!.playWhenReady) {
                    exoPlayer!!.playWhenReady = false
                    unregisterReceiver(becomingNoisyReceiver)
                }
                mediaSession!!.setPlaybackState(
                    stateBuilder.setState(
                        PlaybackStateCompat.STATE_PAUSED,
                        PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN,
                        1f
                    ).build()
                )
                currentState = PlaybackStateCompat.STATE_PAUSED
                refreshNotificationAndForegroundStatus(currentState)
            }

            override fun onStop() {
                if (exoPlayer!!.playWhenReady) {
                    exoPlayer!!.playWhenReady = false
                    unregisterReceiver(becomingNoisyReceiver)
                }
                if (audioFocusRequested) {
                    audioFocusRequested = false
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        audioManager!!.abandonAudioFocusRequest(audioFocusRequest!!)
                    } else {
                        audioManager!!.abandonAudioFocus(audioFocusChangeListener)
                    }
                }
                mediaSession!!.isActive = false
                mediaSession!!.setPlaybackState(
                    stateBuilder.setState(
                        PlaybackStateCompat.STATE_STOPPED,
                        PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN,
                        1f
                    ).build()
                )
                currentState = PlaybackStateCompat.STATE_STOPPED
                refreshNotificationAndForegroundStatus(currentState)
                stopForeground(true)
                stopSelf()
            }

            override fun onSkipToNext() {
                val station = nextStation()
                updateMetadataFromTrack(station)
                refreshNotificationAndForegroundStatus(currentState)
                prepareToPlay(Uri.parse(station.url))
            }

            override fun onSkipToPrevious() {
                val station = previousStation()
                updateMetadataFromTrack(station)
                refreshNotificationAndForegroundStatus(currentState)
                prepareToPlay(Uri.parse(station.url))
            }

            private fun updateMetadataFromTrack(station: Station) {
                //metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_TITLE, station.title)
                //metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_ALBUM, station.title)
                metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, station.title)
                //metadataBuilder.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, 0)
                mediaSession!!.setMetadata(metadataBuilder.build())
            }
        }

    private val audioFocusChangeListener = OnAudioFocusChangeListener { focusChange ->
        when (focusChange) {
            AudioManager.AUDIOFOCUS_GAIN -> mediaSessionCallback.onPlay() // Не очень красиво
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> mediaSessionCallback.onPause()
            else -> mediaSessionCallback.onPause()
        }
    }

    private val becomingNoisyReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Disconnecting headphones - stop playback
            if (AudioManager.ACTION_AUDIO_BECOMING_NOISY == intent.action) {
                mediaSessionCallback.onPause()
            }
        }
    }
    private val exoPlayerListener: ExoPlayer.EventListener = object : ExoPlayer.EventListener {
        fun onTimelineChanged(timeline: Timeline?, manifest: Any?) {}
        override fun onTracksChanged(
            trackGroups: TrackGroupArray,
            trackSelections: TrackSelectionArray
        ) {
        }

        override fun onLoadingChanged(isLoading: Boolean) {}
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            if (playWhenReady && playbackState == ExoPlayer.STATE_ENDED) {
                mediaSessionCallback.onSkipToNext()
            }
        }

        override fun onPlayerError(error: ExoPlaybackException) {
            Timber.d("onPlayerError -> ${error.message}")
            Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            mediaSessionCallback.onPause()
        }

        fun onPositionDiscontinuity() {}
        override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {}
    }

    /* override fun onBind(intent: Intent): IBinder? {
         return if (SERVICE_INTERFACE == intent.action) {
             super.onBind(intent)
         } else {
             PlayerServiceBinder()
         }
     }*/

    /* inner class PlayerServiceBinder : Binder() {
         val mediaSessionToken: MediaSessionCompat.Token
             get() = mediaSession!!.sessionToken
     }*/

    private fun refreshNotificationAndForegroundStatus(playbackState: Int) {
        when (playbackState) {
            PlaybackStateCompat.STATE_PLAYING -> {
                startForeground(
                    NotificationChannels.RADIO_NOTIFICATION_ID,
                    getNotification(playbackState)
                )
            }
            PlaybackStateCompat.STATE_PAUSED -> {
                NotificationManagerCompat.from(this@MediaBrowserPlayerService)
                    .notify(
                        NotificationChannels.RADIO_NOTIFICATION_ID,
                        getNotification(playbackState)
                    )
                //stopForeground(false)
                startForeground(
                    NotificationChannels.RADIO_NOTIFICATION_ID,
                    getNotification(playbackState)
                )
            }
            else -> {
                //stopForeground(true)
                startForeground(
                    NotificationChannels.RADIO_NOTIFICATION_ID,
                    getNotification(playbackState)
                )
            }
        }
    }


    private fun getNotification(playbackState: Int): Notification {
        val builder = MediaStyleHelper.from(this, mediaSession!!)
        with(builder) {
            addAction(
                NotificationCompat.Action(
                    android.R.drawable.ic_media_previous,
                    getString(R.string.previous),
                    MediaButtonReceiver.buildMediaButtonPendingIntent(
                        applicationContext,
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                    )
                )
            )
            if (playbackState == PlaybackStateCompat.STATE_PLAYING) builder.addAction(
                NotificationCompat.Action(
                    android.R.drawable.ic_media_pause,
                    getString(R.string.pause),
                    MediaButtonReceiver.buildMediaButtonPendingIntent(
                        applicationContext,
                        PlaybackStateCompat.ACTION_PLAY_PAUSE
                    )
                )
            ) else builder.addAction(
                NotificationCompat.Action(
                    android.R.drawable.ic_media_play,
                    getString(R.string.play),
                    MediaButtonReceiver.buildMediaButtonPendingIntent(
                        applicationContext,
                        PlaybackStateCompat.ACTION_PLAY_PAUSE
                    )
                )
            )
            addAction(
                NotificationCompat.Action(
                    android.R.drawable.ic_media_next,
                    getString(R.string.next),
                    MediaButtonReceiver.buildMediaButtonPendingIntent(
                        applicationContext,
                        PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                    )
                )
            )
            setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(1)
                    .setShowCancelButton(true)
                    .setCancelButtonIntent(
                        MediaButtonReceiver.buildMediaButtonPendingIntent(
                            applicationContext,
                            PlaybackStateCompat.ACTION_STOP
                        )
                    )
                    .setMediaSession(mediaSession!!.sessionToken)
            ) // setMediaSession требуется для Android Wear
            setSmallIcon(R.drawable.ic_baseline_radio_24)
            color = ContextCompat.getColor(
                applicationContext,
                R.color.design_default_color_error
            ) // The whole background (in MediaStyle), not just icon background
            setShowWhen(false)
            priority = NotificationCompat.PRIORITY_HIGH
            setOnlyAlertOnce(true)
            setChannelId(NotificationChannels.NOTIFICATION_RADIO_CHANNEL_ID)
        }
        return builder.build()
    }

    private fun nextStation(): Station {
        if (currentItemIndex == stationsList.size - 1) currentItemIndex = 0
        else currentItemIndex++
        return stationsList[currentItemIndex]
    }

    private fun previousStation(): Station {
        if (currentItemIndex == 0) currentItemIndex = stationsList.size - 1
        else currentItemIndex--
        return stationsList[currentItemIndex]
    }
}