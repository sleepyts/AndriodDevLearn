package com.example.myapplicationtest1.ui.home


import android.R.attr.data
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.myapplicationtest1.ui.componet.navdrawer.DrawerHeader
import com.example.myapplicationtest1.ui.componet.navdrawer.DrawerMenuItem
import com.example.myapplicationtest1.ui.navigation.Routes
import com.example.myapplicationtest1.ui.playlist.PlaylistUiState
import com.example.myapplicationtest1.ui.playlist.PlaylistViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()

    var drawerState = rememberDrawerState(DrawerValue.Closed)

    var scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Surface(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(280.dp),
            ) {
                Column {
                    // 头部区域
                    DrawerHeader()

                    Divider(color = Color.Gray.copy(alpha = 0.3f))

                    // 菜单列表
                    DrawerMenuItem(icon = Icons.Default.Home, label = "首页") {
                        // 点击事件
                    }
                    DrawerMenuItem(icon = Icons.Default.Settings, label = "设置") { }
                    DrawerMenuItem(icon = Icons.Default.Info, label = "关于") { }

                    Spacer(modifier = Modifier.weight(1f))

                    // 底部内容
                    Text(
                        text = "版本 1.0.0",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                }
            }
        }
    ) {

        Scaffold(
            topBar = {
                HomeTopBar(
                    onMenuClick = {
                        scope.launch { drawerState.open() }
                    }
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(8.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                UserProfile(uiState.value.userProfileUiState)
                Spacer(Modifier.height(24.dp))
                PlayList(uiState.value.userPlaylistUiState, navController)

            }
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
    Text(
        userProfileUiState.username,
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.onBackground
    )
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
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                "current_playlist",
                                it
                            )
                            navController.navigate("${Routes.PLAYLIST}/${it.id}")
                        }),
                    horizontalArrangement = Arrangement.Start,

                    ) {
                    AsyncImage(
                        model = it.coverImgUrl,
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
                            text = it.name,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "${it.trackCount}首 · ${it.playCount}次播放",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )

                    }
                }


            }
        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    onMenuClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "首页",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "打开菜单",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        },
    )
}

@Preview
@Composable
fun HomePreview() {
}