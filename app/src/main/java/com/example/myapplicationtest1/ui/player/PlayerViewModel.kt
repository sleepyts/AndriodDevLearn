package com.example.myapplicationtest1.ui.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplicationtest1.PlayerManager
import com.example.myapplicationtest1.network.SongApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val songApiService: SongApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState: StateFlow<PlayerUiState> = _uiState.asStateFlow()

    fun getSongUrl(id: String) {
        viewModelScope.launch {
            try {
                val url = songApiService.getSongUrl(id, "standard").data[0].url
                PlayerManager.play(url) // 播放交给全局管理器
                _uiState.value = _uiState.value.copy(url = url)
            } catch (e: Exception) {
                // log error
            }
        }
    }
}

data class PlayerUiState(
    val url: String = ""
)
