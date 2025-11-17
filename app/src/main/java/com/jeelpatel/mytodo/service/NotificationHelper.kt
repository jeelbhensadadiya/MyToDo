package com.jeelpatel.mytodo.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.media.app.NotificationCompat.MediaStyle
import com.jeelpatel.mytodo.R
import com.jeelpatel.mytodo.ui.view.MainActivity
import com.jeelpatel.mytodo.utils.Config
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject

@ServiceScoped
class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {


    val channelId = Config.CHANNEL_ID


    fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Media Playback",
                NotificationManager.IMPORTANCE_LOW
            )

            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }


    fun baseNotification(title: String, mediaSession: MediaSessionCompat): Notification {

        // Open MainActivity when notification clicked
        val intent = Intent(context, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val playIntent = Intent("ACTION_PLAY")
        val playPendingIntent = PendingIntent.getBroadcast(
            context, 1, playIntent, PendingIntent.FLAG_IMMUTABLE
        )
        val playAction = NotificationCompat.Action(
            R.drawable.play_24,
            "Play",
            playPendingIntent
        )

        val pauseIntent = Intent("ACTION_PAUSE")
        val pausePendingIntent = PendingIntent.getBroadcast(
            context, 2, pauseIntent, PendingIntent.FLAG_IMMUTABLE
        )
        val pauseAction = NotificationCompat.Action(
            R.drawable.pause_24,
            "Pause",
            pausePendingIntent
        )

        return NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText("Playing music…")
            .setSmallIcon(R.drawable.play_24)
            .setContentIntent(pendingIntent)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .addAction(playAction)
            .addAction(pauseAction)
            .setStyle(
                MediaStyle()
                    .setMediaSession(mediaSession.sessionToken)
                    .setShowActionsInCompactView(0, 1)
            )
            .build()

    }
}