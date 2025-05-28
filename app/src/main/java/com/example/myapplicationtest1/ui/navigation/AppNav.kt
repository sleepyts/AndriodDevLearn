package com.example.myapplicationtest1.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myapplicationtest1.ui.home.HomeScreen
import com.example.myapplicationtest1.ui.player.PlayerScreen
import com.example.myapplicationtest1.ui.playlist.PlaylistScreen
import com.example.myapplicationtest1.ui.playlist.PlaylistViewModel

val LocalNavController = staticCompositionLocalOf<NavController> {
    error("NavController not provided")
}

@Composable
fun AppNav(navController: NavHostController) {

    CompositionLocalProvider(LocalNavController provides navController) {
        NavHost(
            navController = navController,
            startDestination = Routes.HOME
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