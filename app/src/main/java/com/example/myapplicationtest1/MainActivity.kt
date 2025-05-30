package com.example.myapplicationtest1

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplicationtest1.ui.theme.AppTheme
import com.example.myapplicationtest1.ui.home.HomeScreen
import com.example.myapplicationtest1.ui.navigation.AppNav
import com.example.myapplicationtest1.ui.navigation.Routes
import com.example.myapplicationtest1.ui.playlist.PlaylistScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            AppTheme {
                AppNav(navController)

            }
        }

    }
}




