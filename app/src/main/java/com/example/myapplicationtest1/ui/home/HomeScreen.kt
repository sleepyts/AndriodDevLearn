package com.example.myapplicationtest1.ui.home


import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.myapplicationtest1.R
import com.example.myapplicationtest1.model.resp.Playlist
import com.example.myapplicationtest1.model.resp.UserPlaylistResp
import com.example.myapplicationtest1.ui.navigation.Routes
import com.example.myapplicationtest1.ui.playlist.PlaylistUiState
import com.example.myapplicationtest1.ui.playlist.PlaylistViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()




    Scaffold(
        topBar = { HomeTopBar() }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            UserProfile(uiState.value.userProfileUiState)
            Spacer(Modifier.height(24.dp))
            PlayList(uiState.value.userPlaylistUiState, navController)

        }
    }
}

@Composable
fun UserProfile(
    userProfileUiState: UserProfileUiState
) {
    AsyncImage(
        model = userProfileUiState.avatarUrl,
        contentDescription = "Image from URL",
        Modifier
            .size(120.dp)
            .border(2.dp, Color.Gray, CircleShape)
            .clip(CircleShape)
    )

    Spacer(Modifier.height(24.dp))
    Text(userProfileUiState.username)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayList(
    userPlaylistUiState: UserPlaylistUiState,
    navController: NavController,
) {
    LazyColumn(
        Modifier.padding(12.dp)
    ) {
        userPlaylistUiState.playlist.forEach {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clickable(onClick = {
                            navController.navigate("${Routes.PLAYLIST}/${it.id}")
                        }),
                    horizontalArrangement = Arrangement.Start,

                    ) {
                    AsyncImage(
                        model = it.coverImgUrl,
                        contentDescription = null,
                        Modifier.size(48.dp)

                    )
                    Spacer(Modifier.width(12.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceAround,

                        ) {
                        Text(text = it.name)
                        Text(
                            text = String.format("%d首 - %d次播放", it.trackCount, it.playCount),
                            style = MaterialTheme.typography.bodySmall
                        )

                    }
                }


            }
        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar() {
    TopAppBar(
        title = { Text("") },
        navigationIcon = {
            IconButton(onClick = {

            }) {
                Icon(Icons.Default.Menu, contentDescription = null)
            }
        }
    )
}


@Preview
@Composable
fun HomePreview() {
}