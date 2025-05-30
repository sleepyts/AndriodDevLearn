package com.example.myapplicationtest1

import android.media.MediaPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object PlayerManager {

    private var mediaPlayer: MediaPlayer? = null

    private val _playerState = MutableStateFlow(PlayerState())
    val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()

    fun play(url: String) {
        stop() // 停止已有播放器

        mediaPlayer = MediaPlayer().apply {
            setDataSource(url)
            prepareAsync()
            setOnPreparedListener {
                it.start()
                _playerState.value = _playerState.value.copy(isPlaying = true, url = url)
            }
            setOnCompletionListener {
                _playerState.value = _playerState.value.copy(isPlaying = false)

            }
        }
    }


    fun stop() {
        mediaPlayer?.release()
        mediaPlayer = null
        _playerState.value = _playerState.value.copy(isPlaying = false, url = "")
    }
}

data class PlayerState(
    val url: String = "",
    val isPlaying: Boolean = false,
    val duration: Float = 0f,
    val progress: Float = 0f

)