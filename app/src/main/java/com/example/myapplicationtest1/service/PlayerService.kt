package com.example.myapplicationtest1.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import androidx.core.app.NotificationCompat
import androidx.lifecycle.lifecycleScope
import com.example.myapplicationtest1.PlayerManager
import com.example.myapplicationtest1.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject


private const val TAG = "PlayerService"

@AndroidEntryPoint
class PlayerService : Service() {

    @Inject
    lateinit var playerManager: PlayerManager
    private lateinit var mediaSession: MediaSessionCompat
    private val CHANNEL_ID = "player_channel"
    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    override fun onCreate() {
        super.onCreate()
        mediaSession = MediaSessionCompat(this, "PlayerService")
        serviceScope.launch {
            playerManager.playerState
                .map { it.currentSong to it.isPlaying }
                .distinctUntilChanged()
                .collect {
                    updateNotification()
                }
        }
        createNotificationChannel()
    }

    companion object {
        const val ACTION_PLAY = "ACTION_PLAY"
        const val ACTION_PAUSE = "ACTION_PAUSE"
        const val ACTION_NEXT = "ACTION_NEXT"
        const val ACTION_PREVIOUS = "ACTION_PREVIOUS"
        const val NOTIFICATION_ID = 1234


    }

    fun updateNotification() {
        val notification = buildNotification()
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun buildNotification(): Notification {

        // 用你PlayerManager的状态构建Notification的内容
        val song = playerManager.playerState.value.currentSong
        val isPlaying = playerManager.isPlaying()

        // 播放/暂停的Intent
        val playPauseIntent = PendingIntent.getService(
            this, 1, Intent(this, PlayerService::class.java).apply {
                action = if (isPlaying) ACTION_PAUSE else ACTION_PLAY
            }, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val nextIntent = PendingIntent.getService(
            this, 2, Intent(this, PlayerService::class.java).apply {
                action = ACTION_NEXT
            }, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val titleSpannable = SpannableString(song.name).apply {
            setSpan(AbsoluteSizeSpan(18, true), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        val artistSpannable = SpannableString(song.ar.joinToString("/") { it.name }).apply {
            setSpan(AbsoluteSizeSpan(14, true), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(titleSpannable)
            .setContentText(artistSpannable)
            .setSmallIcon(R.drawable.play_arrow_24dp_1f1f1f_fill0_wght400_grad0_opsz24)
            .setOnlyAlertOnce(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .addAction(
                NotificationCompat.Action(
                    R.drawable.arrow_back_24dp_1f1f1f_fill0_wght400_grad0_opsz24, "Previous",
                    PendingIntent.getService(
                        this,
                        3,
                        Intent(this, PlayerService::class.java).apply {
                            action = ACTION_PREVIOUS
                        },
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                )
            )
            .addAction(
                NotificationCompat.Action(
                    if (isPlaying) R.drawable.pause_24dp_1f1f1f_fill0_wght400_grad0_opsz24 else R.drawable.play_arrow_24dp_1f1f1f_fill0_wght400_grad0_opsz24,
                    if (isPlaying) "Pause" else "Play",
                    playPauseIntent
                )
            )
            .addAction(
                NotificationCompat.Action(
                    R.drawable.arrow_forward_24dp_1f1f1f_fill0_wght400_grad0_opsz24,
                    "Next",
                    nextIntent
                )
            )
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSession.sessionToken)
                    .setShowActionsInCompactView(1) // 显示中间的播放/暂停按钮
            )
            .setPriority(NotificationCompat.PRIORITY_LOW)

        return builder.build()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Music Player",
            NotificationManager.IMPORTANCE_LOW
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.action?.let { action ->
            when (action) {
                ACTION_PLAY -> {
                    playerManager.playOrStart()
                }

                ACTION_PAUSE -> {
                    playerManager.playOrStart()
                }

                ACTION_NEXT -> {
                    playerManager.playNext()
                }

                ACTION_PREVIOUS -> {
                    playerManager.playBefore()
                }
            }
        }
        // 更新通知
        startForeground(
            NOTIFICATION_ID,
            buildNotification(),
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
        )
        return START_STICKY
    }


    override fun onDestroy() {
        mediaSession.release()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}