package com.example.myapplicationtest1

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplicationtest1.ui.theme.MyApplicationTest1Theme

class MainActivity : ComponentActivity() {


    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTest1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    TestImage(stringResource(R.string.card_text), Modifier)
                }
            }
        }
    }
}

@Composable
fun TestText(message: String, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
    ) {
        Text(
            text = message,
            modifier=Modifier.align(alignment = Alignment.CenterHorizontally).clickable {
                println("Clicked")
            },
            textAlign = TextAlign.Center
        )
        Text(
            text = "message",
            modifier=Modifier.align(alignment = Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun TestImage(info: String, modifier: Modifier = Modifier) {
    val image = painterResource(R.drawable.androidparty);
    Box(modifier) {
        Image(
            painter = image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alpha = 0.5F
        )
        TestText(
            info, modifier = Modifier
                .padding(24.dp)
                .fillMaxSize()
        )
    }
}

@Preview(showBackground = true, name = "test")
@Composable
fun Preview1() {
    MyApplicationTest1Theme {
        TestImage("Yes this is a gift woooooooooooooooooooooooooooooooooooooooooooooooooo")
    }
}



