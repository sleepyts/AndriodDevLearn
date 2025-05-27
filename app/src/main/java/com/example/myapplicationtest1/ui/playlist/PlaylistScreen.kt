package com.example.myapplicationtest1.ui.playlist

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapplicationtest1.model.resp.Song
import com.example.myapplicationtest1.ui.navigation.LocalNavController

@Composable
fun PlaylistScreen(
    playlistId: String,
    navController: NavController,
    viewModel: PlaylistViewModel = hiltViewModel(),
    modify: Modifier = Modifier
) {
    val uiState = viewModel.uiState.collectAsState()

    LaunchedEffect(true) {
        viewModel.getSongs(playlistId)
    }

    Scaffold(
        topBar = { PlaylistTooBar() }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SongList(songs = uiState.value.songs)
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
fun SongList(
    songs: List<Song>,
) {
    LazyColumn(
        Modifier.padding(12.dp)
    ) {
        songs.forEach { it ->
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    horizontalArrangement = Arrangement.Start,

                    ) {
                    AsyncImage(
                        model = it.al.picUrl,
                        contentDescription = null,
                        Modifier.size(48.dp)

                    )
                    Spacer(Modifier.width(12.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceAround,

                        ) {
                        Text(
                            text = it.name,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = String.format(
                                "%s - %s",
                                it.ar.joinToString("/") { it.name },
                                it.al.name
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodySmall
                        )

                    }
                }
                Spacer(Modifier.height(12.dp))

            }
        }
    }
}