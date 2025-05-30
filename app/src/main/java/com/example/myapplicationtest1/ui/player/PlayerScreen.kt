package com.example.myapplicationtest1.ui.player

import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplicationtest1.model.resp.Song
import com.example.myapplicationtest1.ui.navigation.LocalNavController


@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel = hiltViewModel(),
) {
    val navController = LocalNavController.current
    val context = LocalContext.current

    val song = navController.previousBackStackEntry?.savedStateHandle?.get<Song>("song")
    val uiState = viewModel.uiState.collectAsState()

    if (song != null) {
        // 请求播放链接
        LaunchedEffect(song.id) {
            viewModel.getSongUrl(song.id)
        }
    }

    Button(onClick = {
    }) { }

}