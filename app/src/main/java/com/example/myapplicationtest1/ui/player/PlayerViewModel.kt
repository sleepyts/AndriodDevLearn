package com.example.myapplicationtest1.ui.player

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplicationtest1.PlayerManager
import com.example.myapplicationtest1.PlayerState
import com.example.myapplicationtest1.model.resp.Song
import com.example.myapplicationtest1.network.SongApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "PlayerViewModel"

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val playerManager: PlayerManager
) : ViewModel() {

    val playerState: StateFlow<PlayerState> = playerManager.playerState

    override fun onCleared() {
        Log.d(TAG, "onCleared")
    }

    fun play() {
        viewModelScope.launch {
            playerManager.play()
        }
    }

    fun seekTo(position: Float) {
        playerManager.jump(position)
    }

    fun getSongTrack(): Pair<String, String> {
        val current = playerState.value.current
        val duration = playerState.value.duration


        return Pair(formatDuration(current), formatDuration(duration))
    }

    fun playNextSong() {
        playerManager.playNext()
    }

    fun playBeforeSong() {
        playerManager.playBefore()
    }

    fun pauseOrStart() {
        playerManager.playOrStart()
    }


    private fun formatDuration(durationMs: Long): String {
        val totalSeconds = durationMs / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }


}

