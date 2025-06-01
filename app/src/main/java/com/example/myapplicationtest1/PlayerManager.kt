package com.example.myapplicationtest1

import android.media.MediaPlayer
import android.util.Log
import com.example.myapplicationtest1.model.resp.Song
import com.example.myapplicationtest1.network.SongApiService
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
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
    private var changeSong: Boolean = false

    fun updateSongList(songList: List<Song>) {
        _playerState.value = _playerState.value.copy(songList = songList)
    }

    fun updateCurrentSong(song: Song) {
        if (_playerState.value.currentSong == song) {
            changeSong = false
            return
        }
        changeSong = true
        _playerState.value = _playerState.value.copy(currentSong = song)
    }

    fun playOrStart() {
        mediaPlayer?.let {
            if (it.isPlaying) it.pause() else it.start()
            _playerState.value = _playerState.value.copy(isPlaying = !_playerState.value.isPlaying)
        }
    }

    fun isPlaying() = _playerState.value.isPlaying


    private fun readyForNext() {
        mediaPlayer?.apply {
            progressJob?.cancel()
            progressJob = null
            reset()
        }

    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun play() {
        val song = _playerState.value.currentSong
        Log.d(TAG, "$song")
        val url: String = songApiService.getSongUrl(song.id, "standard").data[0].url


        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(url)
                prepareAsync()
                setOnPreparedListener {
                    it.start()
                    _playerState.value = _playerState.value.copy(
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
                    readyForNext()
                    playNext()
                }
            }
        } else {
            if (changeSong) {
                mediaPlayer?.reset()
                mediaPlayer?.setDataSource(url)
                mediaPlayer?.prepareAsync()
            }

        }

    }

    private fun playNext() {

    }

    private fun getNextSong() {
        val state = _playerState.value
        val index = state.songList.indexOfFirst { it.id == state.currentSong.id }
        var nextIndex: Int
        when (state.playMode) {
            PlayMode.Random.code -> {
            }
        }
    }

    fun jump(position: Float) {
        mediaPlayer?.seekTo((position * mediaPlayer?.duration!!).toInt())
    }


}

data class PlayerState(
    val url: String = "",
    val isPlaying: Boolean = false,
    val duration: Long = 0L,
    val progress: Float = 0f,
    val current: Long = 0L,
    val playMode: Int = PlayMode.Order.code,
    val nextSongId: String = "",

    val currentSong: Song = Song(),
    val songList: List<Song> = emptyList(),

    )

enum class PlayMode(val code: Int, val desc: String) {
    Random(1, "random"),
    Circle(2, "circle"),
    Order(3, "order")
}