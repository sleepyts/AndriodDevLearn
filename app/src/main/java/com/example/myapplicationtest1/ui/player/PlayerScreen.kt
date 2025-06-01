package com.example.myapplicationtest1.ui.player

import android.R.attr.contentDescription
import android.R.attr.onClick
import android.R.attr.thumb
import android.view.RoundedCorner
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.myapplicationtest1.PlayMode
import com.example.myapplicationtest1.PlayerManager
import com.example.myapplicationtest1.model.resp.Song
import com.example.myapplicationtest1.ui.navigation.LocalNavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel = hiltViewModel(),
) {
    val navController = LocalNavController.current

    val song = navController.previousBackStackEntry?.savedStateHandle?.get<Song>("song")

    val playerState = viewModel.playerState.collectAsState().value
    if (song != null) {
        // 请求播放链接
        LaunchedEffect(song.id) {
            viewModel.getUrlAndPlay(song.id)
        }
    } else {
        return
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = song.name,
                            style = MaterialTheme.typography.titleSmall,
                            maxLines = 1
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(12.dp)
        ) {
            AsyncImage(
                model = song.al.picUrl,
                modifier = Modifier
                    .size(240.dp)
                    .padding(12.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .border(1.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.medium),
                contentDescription = null
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = song.name,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Start
            )
            Text(
                text = song.ar.joinToString("/") { it.name },
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(24.dp))
            Slider(
                valueRange = 0f..1f,
                value = playerState.progress,
                onValueChange = {
                    viewModel.seekTo(it)
                },
                thumb = {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(Color.Gray),
                    )
                },

                track = { sliderStates -> // sliderStates 包含了滑块和轨道的信息
                    val inactiveTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    val activeTrackColor = MaterialTheme.colorScheme.primary

                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp) // 设置 Canvas 的高度，从而影响轨道高度
                    ) {
                        val strokeWidth = 4.dp.toPx()
                        val centerY = -6.5f
                        val thumbRadius = 3.dp.toPx()

                        // 绘制非活动轨道
                        drawLine(
                            color = inactiveTrackColor,
                            start = Offset(0f, centerY),
                            end = Offset(size.width, centerY),
                            strokeWidth = strokeWidth,
                            cap = StrokeCap.Round
                        )

                        // 绘制活动轨道
                        drawLine(
                            color = activeTrackColor,
                            start = Offset(0f, centerY),
                            end = Offset(
                                (size.width * playerState.progress - thumbRadius),
                                centerY
                            ),
                            strokeWidth = strokeWidth,
                            cap = StrokeCap.Round
                        )
                    }
                }
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val songTrack = viewModel.getSongTrack()
                Text(
                    text = songTrack.first,
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = songTrack.second,
                    style = MaterialTheme.typography.labelSmall

                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                com.example.myapplicationtest1.ui.componet.IconButton(
                    onClick = { /*TODO*/ },
                    imageVector = when (playerState.playMode) {
                        PlayMode.Order.code -> Icons.Default.Repeat
                        PlayMode.Random.code -> Icons.Default.Shuffle
                        PlayMode.Circle.code -> Icons.Default.RepeatOne
                        else -> Icons.Default.Repeat
                    },
                )
                Spacer(modifier = Modifier.weight(1f))
                com.example.myapplicationtest1.ui.componet.IconButton(
                    onClick = { viewModel.playBeforeSong() },
                    imageVector = Icons.Default.ArrowBack,
                )
                com.example.myapplicationtest1.ui.componet.IconButton(
                    onClick = { viewModel.pauseOrStart() },
                    imageVector = if (playerState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                )
                com.example.myapplicationtest1.ui.componet.IconButton(
                    onClick = { viewModel.playNextSong() },
                    imageVector = Icons.Default.ArrowForward,
                )
                Spacer(modifier = Modifier.weight(1f))
                com.example.myapplicationtest1.ui.componet.IconButton(
                    onClick = { /*TODO*/ },
                    imageVector = Icons.Default.Menu,
                )
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun Sli() {

    // 获取密度，用于将 dp 转换为像素

    // 计算滑块的实际半径（从 Modifier.size(14.dp) 得到）
    Slider(
        valueRange = 0f..100f,
        value = 80f,
        onValueChange = {

        },
        thumb = {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(Color.Gray),
            )
        },
        track = { sliderStates -> // sliderStates 包含了滑块和轨道的信息
            val inactiveTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            val activeTrackColor = MaterialTheme.colorScheme.primary

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp) // 设置 Canvas 的高度，从而影响轨道高度
            ) {
                val strokeWidth = 2.dp.toPx()
                val centerY = -6.5f
                val thumbRadius = 3.dp.toPx()

                // 绘制非活动轨道
                drawLine(
                    color = inactiveTrackColor,
                    start = Offset(thumbRadius, centerY),
                    end = Offset(thumbRadius + size.width - 0f, centerY),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )

                // 绘制活动轨道
                drawLine(
                    color = activeTrackColor,
                    start = Offset(0f, centerY),
                    end = Offset(
                        (size.width - 2 * thumbRadius) * 0.8f,
                        centerY
                    ),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )
            }
        }


    )
}