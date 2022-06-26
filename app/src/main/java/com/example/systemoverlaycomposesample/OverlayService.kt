package com.example.systemoverlaycomposesample

import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.lifecycle.ViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryOwner

class OverlayService : LifecycleService(), SavedStateRegistryOwner {

    companion object Action {
        private val overlayParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        private const val START_OVERLAY = "start_overlay"
        private const val STOP_OVERLAY = "stop_overlay"

        fun createStartIntent(context: Context): Intent {
            return Intent(context, OverlayService::class.java).apply {
                action = START_OVERLAY
            }
        }

        fun createStopIntent(context: Context): Intent {
            return Intent(context, OverlayService::class.java).apply {
                action = STOP_OVERLAY
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            START_OVERLAY -> {}
            STOP_OVERLAY -> {}
            else -> {}
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun handleStartOverlay() {
        val composeView = ComposeView(this)
        composeView.setContent {
            Text(
                text = "Hello",
                color = Color.Black,
                fontSize = 50.sp,
                modifier = Modifier
                    .wrapContentSize()
                    .background(Color.Green)
            )
        }
        ViewTreeLifecycleOwner.set(composeView, this)
        ViewTreeViewModelStoreOwner.set(composeView) {ViewModelStore()}
        ViewTreeSavedStateRegistryOwner.set(composeView) {SavedStateRegistry()}
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

    }

    override val savedStateRegistry: SavedStateRegistry
        get() = TODO("Not yet implemented")
}