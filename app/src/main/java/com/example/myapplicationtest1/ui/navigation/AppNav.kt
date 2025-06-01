package com.example.myapplicationtest1.ui.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import coil.compose.AsyncImage
import com.example.myapplicationtest1.PlayerState
import com.example.myapplicationtest1.ui.PlayerManngerViewModel
import com.example.myapplicationtest1.ui.home.HomeScreen
import com.example.myapplicationtest1.ui.player.PlayerScreen
import com.example.myapplicationtest1.ui.playlist.PlaylistScreen
import com.example.myapplicationtest1.ui.playlist.PlaylistViewModel

val LocalNavController = staticCompositionLocalOf<NavController> {
    error("NavController not provided")
}

@Composable
fun AppNav(
    navController: NavHostController,
    viewModel: PlayerManngerViewModel = hiltViewModel()
) {

    CompositionLocalProvider(LocalNavController provides navController) {
        val collectAsState = viewModel.playerState.collectAsState()
        Scaffold(
            bottomBar = {
                MiniPlayer(
                    collectAsState.value,
                    onTogglePlay = { viewModel.pauseOrStart() },
                    onClick = {
                        navController.navigate(Routes.PLAYER)
                    },
                    isShow = {
                        navController.currentBackStackEntry?.destination?.route != Routes.PLAYER
                    }
                )
            }
        ) { innerPadding ->

            NavHost(
                navController = navController,
                startDestination = Routes.HOME,
            ) {
                composable(Routes.HOME) {
                    HomeScreen(navController)
                }
                composable("${Routes.PLAYLIST}/{playlist}") {
                    PlaylistScreen(navController)
                }
                composable(Routes.PLAYER) {
                    PlayerScreen()
                }

            }
        }
    }

}

@Composable
fun MiniPlayer(
    playerState: PlayerState,
    onTogglePlay: () -> Unit,
    onClick: () -> Unit,
    isShow: () -> Boolean
) {
    if (playerState.currentSong.id.isEmpty() || !isShow()) return

    Surface(
        tonalElevation = 2.dp,
        shadowElevation = 6.dp,
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            // 封面图
            AsyncImage(
                model = playerState.currentSong.al.picUrl,
                contentDescription = "Album cover",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            // 歌曲信息
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = playerState.currentSong.name,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = playerState.currentSong.ar.joinToString("/") { it.name },
                    maxLines = 1,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                LinearProgressIndicator(
                    progress = playerState.progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                        .height(3.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // 播放/暂停按钮
            IconButton(onClick = onTogglePlay) {
                Icon(
                    imageVector = if (playerState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = "Play or Pause",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

