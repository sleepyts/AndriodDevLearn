package com.example.myapplicationtest1.ui.player

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplicationtest1.network.SongApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.http.Tag
import javax.inject.Inject

private const val TAG = "PlayerViewModel"

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val songApiService: SongApiService
) : ViewModel() {
    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState: StateFlow<PlayerUiState> = _uiState.asStateFlow()


    fun getSongUrl(id: String) {
        viewModelScope.launch {
            try {
                songApiService.getSongUrl(id, "standard")
                setUrl(songApiService.getSongUrl(id, "standard").data[0].url)
            } catch (e: Exception) {
                Log.e(TAG, "getSongUrl: ${e.message}")
            }
        }
    }

    private fun setUrl(url: String) {
        _uiState.value = _uiState.value.copy(url = url)
    }
}

data class PlayerUiState(
    val url: String = ""
)