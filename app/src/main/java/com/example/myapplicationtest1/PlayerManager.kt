package com.example.myapplicationtest1

import android.media.MediaPlayer
import android.util.Log
import com.example.myapplicationtest1.model.resp.Song
import com.example.myapplicationtest1.network.SongApiService
import kotlinx.coroutines.CoroutineScope
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


    private val scope = CoroutineScope(Dispatchers.IO)

    fun updateSongList(songList: List<Song>) {
        _playerState.value = _playerState.value.copy(songList = songList)
    }

    fun updateCurrentSong(song: Song) {
        if (_playerState.value.currentSong == song) {
            changeSong = false
            return
        }
        changeSong = true
        _playerState.value = _playerState.value.copy(
            currentSong = song,
            currentSongIndex = _playerState.value.songList.indexOf(song)
        )
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

    fun playNext() {
        readyForNext()
        getNextSong()

        getUrlAndPlay()
    }

    fun playBefore() {
        readyForNext()
        getNextSong(false)

        getUrlAndPlay()
    }


    private fun getUrlAndPlay() {

        var url = ""
        scope.launch {
            url = songApiService.getSongUrl(
                _playerState.value.currentSong.id,
                "standard"
            ).data[0].url
            mediaPlayer?.reset()
            mediaPlayer?.setDataSource(url)
            mediaPlayer?.prepareAsync()
        }
    }

    private fun getNextSong(isNext: Boolean = true) {
        val state = _playerState.value
        var nextSong: Song = Song()
        var nextSongIndex: Int = state.currentSongIndex
        when (state.playMode) {
            PlayMode.Random.code -> {
                do {
                    nextSong = state.songList.random()
                } while (nextSong != state.currentSong)
            }

            PlayMode.Order.code -> {
                if (isNext) nextSongIndex++ else nextSongIndex--
                if (nextSongIndex >= state.songList.size) nextSongIndex = 0;
                if (nextSongIndex <= 0) nextSongIndex = state.songList.size - 1;
                nextSong = state.songList[nextSongIndex]
            }

            PlayMode.Circle.code -> {

            }

        }

        _playerState.value = _playerState.value.copy(
            currentSong = nextSong,
            currentSongIndex = nextSongIndex
        )
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
    val currentSongIndex: Int = 0,
    val songList: List<Song> = emptyList(),

    )

enum class PlayMode(val code: Int, val desc: String) {
    Random(1, "random"),
    Circle(2, "circle"),
    Order(3, "order")
}