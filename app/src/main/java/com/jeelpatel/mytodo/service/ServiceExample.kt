package com.jeelpatel.mytodo.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.jeelpatel.mytodo.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ServiceExample : Service() {

    @Inject
    lateinit var player: ExoPlayer


    override fun onBind(intent: Intent?): IBinder? = null


    override fun onCreate() {
        super.onCreate()
        startForegroundService()
        Toast.makeText(this, "Service Created", Toast.LENGTH_SHORT).show()
    }


    private fun startForegroundService() {
        val channelId = "service_channel"

        // Create notification channel for Android 8+
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Service Channel",
                NotificationManager.IMPORTANCE_LOW
            )

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Service Running")
            .setContentText("Media playing...")
            .setSmallIcon(R.drawable.to_do_24)
            .build()

        startForeground(1, notification)
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val media =
            MediaItem.fromUri("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3")
        player.setMediaItem(media)
        player.prepare()
        player.play()

        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show()
        return START_STICKY
    }


    override fun onDestroy() {
        super.onDestroy()
        player.stop()
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_SHORT).show()
    }
}
