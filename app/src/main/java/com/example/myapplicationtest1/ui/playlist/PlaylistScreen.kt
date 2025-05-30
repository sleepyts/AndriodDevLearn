package com.example.myapplicationtest1.ui.playlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapplicationtest1.model.resp.Al
import com.example.myapplicationtest1.model.resp.Ar
import com.example.myapplicationtest1.model.resp.Playlist
import com.example.myapplicationtest1.model.resp.Song
import com.example.myapplicationtest1.ui.navigation.LocalNavController
import com.example.myapplicationtest1.ui.navigation.Routes

@Composable
fun PlaylistScreen(
    navController: NavController,
    viewModel: PlaylistViewModel = hiltViewModel(),
    modify: Modifier = Modifier
) {
    val uiState = viewModel.uiState.collectAsState()

    val playlist =
        navController.previousBackStackEntry?.savedStateHandle?.get<Playlist>("current_playlist")!!
    LaunchedEffect(true) {
        viewModel.getSongs(playlist.id)
    }

    Scaffold(
        topBar = { PlaylistTooBar() }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LazyColumn {
                item {
                    PlaylistProfile(playlist)
                    Spacer(Modifier.height(24.dp))

                }
                items(uiState.value.songs.size) { index ->
                    val song = uiState.value.songs[index]
                    SongItem(song)
                }
            }

        }
    }

}

@Composable
fun PlaylistProfile(
    playlist: Playlist
) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(120.dp),
        horizontalArrangement = Arrangement.Start,
    ) {
        AsyncImage(
            model = playlist.coverImgUrl,
            contentDescription = null,
            Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(6.dp))
        )
        Spacer(Modifier.width(12.dp))
        Column(
            Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = playlist.name,
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = String.format("by - %s      %d次播放", "Missday", playlist.playCount),
                style = MaterialTheme.typography.bodySmall
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistTooBar(
) {
    val current = LocalNavController.current
    TopAppBar(
        title = { Text("") },
        navigationIcon = {
            IconButton(onClick = {
                current.popBackStack()
            }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            }
        }
    )
}

@Composable
fun SongItem(
    song: Song

) {
    val navController = LocalNavController.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickable(onClick = {
                navController.currentBackStackEntry?.savedStateHandle?.set("song", song)
                navController.navigate(Routes.PLAYER)
            }),
        horizontalArrangement = Arrangement.Start,

        ) {
        AsyncImage(
            model = song.al.picUrl,
            contentDescription = null,
            Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(6.dp))

        )
        Spacer(Modifier.width(12.dp))
        Column(
            modifier = Modifier
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceAround,

            ) {
            Text(
                text = song.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = String.format(
                    "%s - %s",
                    song.ar.joinToString("/") { it.name },
                    song.al.name
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodySmall
            )

        }
    }
    Spacer(Modifier.height(12.dp))
}


@Preview
@Composable
fun SongsPreview() {
    SongItem(

        Song(
            "Hello",
            "123",
            listOf(
                Ar(
                    "12312",
                    "123123"
                )
            ),
            Al(
                "123",
                "wqe",
                "https://p1.music.126.net/2BSOJnjjI2fOJgkrqEoClg==/109951164136313679.jpg"
            ),
            "123",
            "123213"
        )

    )
}