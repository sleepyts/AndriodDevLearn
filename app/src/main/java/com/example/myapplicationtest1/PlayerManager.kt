package com.example.myapplicationtest1

import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.myapplicationtest1.model.resp.Song
import com.example.myapplicationtest1.network.SongApiService
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "PlayerManager"

@Singleton
class PlayerManager @Inject constructor(
    val songApiService: SongApiService
) {

    private var mediaPlayer: MediaPlayer? = null

    private val _playerState = MutableStateFlow(PlayerState())
    val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()

    private var progressJob: Job? = null
    suspend fun getSongUrlAndPlay(id: String) {
        if (isCurrentSong(id)) {
            Log.d(TAG, "当前歌曲已播放")
            return
        }

        try {
            val url = withContext(Dispatchers.IO) {
                songApiService.getSongUrl(id, "standard").data[0].url
            }
            play(url, id)
        } catch (e: Exception) {
            Log.e(TAG, "播放失败：${e.message}")
        }
    }

    fun isCurrentSong(id: String) = _playerState.value.id == id

    private fun stop() {
        mediaPlayer?.apply {
            setOnPreparedListener(null)
            setOnCompletionListener(null)
            setOnErrorListener(null)

            if (isPlaying) {
                stop()
            }
            release()
        }
        progressJob?.cancel()
        progressJob = null
        mediaPlayer = null
        _playerState.value = PlayerState()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun play(url: String, id: String) {
        stop()

        mediaPlayer = MediaPlayer().apply {
            setDataSource(url)
            prepareAsync()
            setOnPreparedListener {
                it.start()
                _playerState.value = PlayerState(
                    id = id,
                    url = url,
                    isPlaying = true,
                    duration = it.duration.toLong()
                )
                progressJob?.cancel()
                progressJob = GlobalScope.launch {
                    withContext(Dispatchers.Default) {
                        while (mediaPlayer != null && mediaPlayer!!.isPlaying) {
                            val currentPosition = mediaPlayer?.currentPosition ?: 0

                            val duration = mediaPlayer?.duration ?: 1

                            _playerState.value = _playerState.value.copy(
                                progress = currentPosition.toFloat() / duration,
                                current = currentPosition.toLong(),
                            )

                            delay(1000L)
                        }
                    }
                }
            }

            setOnCompletionListener {
                it.stop()
            }
        }

    }

    fun jump(position: Float) {
        mediaPlayer?.seekTo((position * mediaPlayer?.duration!!).toInt())
    }


}

data class PlayerState(
    var id: String = "",
    var url: String = "",
    var isPlaying: Boolean = false,
    var duration: Long = 0L,
    var progress: Float = 0f,
    var current: Long = 0L

)