package com.jeelpatel.mytodo.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.media3.exoplayer.ExoPlayer
import com.jeelpatel.mytodo.R
import com.jeelpatel.mytodo.utils.Config
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MusicService : Service() {


    @Inject
    lateinit var player: ExoPlayer


    private val channelId = "music_channel"


    override fun onBind(intent: Intent?): IBinder? = null


    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(1, buildNotification(isPlaying = false))
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {

            Config.ACTION_PLAY -> {
                player.play()
                updateNotification(true)
            }

            Config.ACTION_PAUSE -> {
                player.pause()
                updateNotification(false)
            }

            Config.ACTION_STOP -> {
                stopSelf()
            }
        }
        return START_STICKY
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Music Player",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }


    private fun buildNotification(isPlaying: Boolean): Notification {
        val playPauseAction = if (isPlaying) {
            NotificationCompat.Action(
                R.drawable.pause_24,
                "Pause",
                serviceAction(Config.ACTION_PAUSE)
            )
        } else {
            NotificationCompat.Action(
                R.drawable.play_24,
                "Play",
                serviceAction(Config.ACTION_PLAY)
            )
        }

        val stopAction = NotificationCompat.Action(
            R.drawable.trash_24,
            "Stop",
            serviceAction(Config.ACTION_STOP)
        )

        return NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.to_do_24)
            .setContentTitle("Music Player")
            .setContentText(if (isPlaying) "Playing" else "Paused")
            .addAction(playPauseAction)
            .addAction(stopAction)
            .setOnlyAlertOnce(true)
            .setOngoing(isPlaying)
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle())
            .setContentIntent(
                PendingIntent.getActivity(
                    this,
                    0,
                    Intent(),
                    PendingIntent.FLAG_IMMUTABLE
                )
            )
            .build()
    }


    private fun updateNotification(isPlaying: Boolean) {
        val manager = getSystemService(NotificationManager::class.java)
        manager.notify(1, buildNotification(isPlaying))
    }


    private fun serviceAction(action: String): PendingIntent {
        val intent = Intent(this, MusicService::class.java).apply {
            this.action = action
        }
        return PendingIntent.getService(
            this,
            action.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }


    override fun onDestroy() {
        super.onDestroy()
        player.pause()
    }
}