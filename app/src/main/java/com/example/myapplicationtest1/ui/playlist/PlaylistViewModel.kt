package com.example.myapplicationtest1.ui.playlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplicationtest1.model.resp.Playlist
import com.example.myapplicationtest1.model.resp.Song
import com.example.myapplicationtest1.network.PlaylistApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


private const val TAG = "PlaylistViewModel"

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val playlistApiService: PlaylistApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlaylistUiState())
    val uiState = _uiState.asStateFlow()


    fun getSongs(id: String) {
        viewModelScope.launch {
            try {
                val playlistTracks =
                    playlistApiService.getPlaylistTracks(id)
                Log.d(TAG, "$playlistTracks")
                updateSongs(playlistTracks.songs)
            } catch (e: Exception) {
                Log.e(TAG, "$e")
            }
        }

    }


    fun updateSongs(songs: List<Song>) {
        _uiState.value = _uiState.value.copy(songs = songs)
    }


}


data class PlaylistUiState(
    val songs: List<Song> = emptyList()
)