package com.example.myapplicationtest1.ui

import androidx.lifecycle.ViewModel
import com.example.myapplicationtest1.PlayerManager
import com.example.myapplicationtest1.PlayerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PlayerManngerViewModel @Inject constructor(
    private val playerManager: PlayerManager
) : ViewModel() {
    val playerState: StateFlow<PlayerState> = playerManager.playerState


    fun pauseOrStart() {
        playerManager.playOrStart()
    }
}