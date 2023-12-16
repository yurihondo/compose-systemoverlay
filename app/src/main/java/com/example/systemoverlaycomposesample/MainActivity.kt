package com.example.systemoverlaycomposesample

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.systemoverlaycomposesample.ui.theme.SystemOverlayComposeSampleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SystemOverlayComposeSampleTheme {
                // A surface container using the 'background' color from the theme
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.background)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Button(onClick = ::startOverlay) {
                        Text(text = "Start overlay")
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(onClick = ::stopOverlay) {
                        Text(text = "Stop overlay")
                    }
                }
            }
        }
    }

    private fun startOverlay() {
        val overlayPermissionIntent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:${packageName}"))

        if (!Settings.canDrawOverlays(this)) {
            startActivity(overlayPermissionIntent)
        } else {
            startService(OverlayService.createStartIntent(applicationContext))
        }
    }

    private fun stopOverlay() {
        startService(OverlayService.createStopIntent(applicationContext))
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SystemOverlayComposeSampleTheme {
        Greeting("Android")
    }
}