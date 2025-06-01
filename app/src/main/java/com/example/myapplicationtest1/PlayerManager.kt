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

    fun updateSongList(songList: List<Song>) {
        _playerState.value = _playerState.value.copy(songList = songList)
    }

    fun playOrStart() {
        mediaPlayer?.let {
            if (it.isPlaying) it.pause() else it.start()
            _playerState.value = _playerState.value.copy(isPlaying = !_playerState.value.isPlaying)
        }
    }

    fun isPlaying() = _playerState.value.isPlaying

    fun isCurrentSong(id: String) = _playerState.value.id == id

    private fun stopNow() {
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
        _playerState.value = _playerState.value.copy(
            id = "",
            url = "",
            isPlaying = false,
        )
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun play(url: String, id: String) {
        mediaPlayer = MediaPlayer().apply {
            setDataSource(url)
            prepareAsync()
            setOnPreparedListener {
                it.start()
                _playerState.value = _playerState.value.copy(
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
                stopNow()
            }
        }

    }

    fun jump(position: Float) {
        mediaPlayer?.seekTo((position * mediaPlayer?.duration!!).toInt())
    }


}

data class PlayerState(
    val id: String = "",
    val url: String = "",
    val isPlaying: Boolean = false,
    val duration: Long = 0L,
    val progress: Float = 0f,
    val current: Long = 0L,
    val playMode: Int = PlayMode.Order.code,
    val nextSongId: String = "",

    val songList: List<Song> = emptyList()
)

enum class PlayMode(val code: Int, val desc: String) {
    Random(1, "random"),
    Circle(2, "circle"),
    Order(3, "order")
}