package com.jeelpatel.mytodo.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import android.support.v4.media.session.MediaSessionCompat
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MusicService : Service(), Player.Listener {


    @Inject
    lateinit var player: ExoPlayer


    @Inject
    lateinit var notificationHelper: NotificationHelper


    lateinit var mediaSessionCompat: MediaSessionCompat

    override fun onCreate() {
        super.onCreate()

        player.addListener(this)

        notificationHelper.createChannel()

        mediaSessionCompat = MediaSessionCompat(this, "MusicService")
        mediaSessionCompat.setActive(true)

    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val notification = notificationHelper.baseNotification(
            title = "Playing…",
            mediaSession = mediaSessionCompat
        )
        startForeground(1, notification)


        player.playWhenReady = true

        return START_STICKY

    }


    override fun onDestroy() {
        super.onDestroy()
        player.release()
        mediaSessionCompat.release()
    }


    override fun onBind(intent: Intent?): IBinder? = null
}