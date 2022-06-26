package com.example.systemoverlaycomposesample

import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.view.View
import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.lifecycle.ViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.example.systemoverlaycomposesample.ui.theme.Typography

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

    private val savedStateRegistryController = SavedStateRegistryController.create(this)

    override val savedStateRegistry: SavedStateRegistry
        get() = savedStateRegistryController.savedStateRegistry

    override fun onCreate() {
        super.onCreate()
        savedStateRegistryController.performRestore(null)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            START_OVERLAY -> handleStartOverlay()
            STOP_OVERLAY -> handleStopOverlay()
            else -> {}
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private val composeView: View by lazy {
        ComposeView(this).apply {
            setViewTreeSavedStateRegistryOwner(this@OverlayService)
            setContent {
                Text(
                    text = "Hello, World!",
                    color = Color.Black,
                    fontSize = 50.sp,
                    modifier = Modifier.wrapContentSize(),
                    style = Typography.headlineLarge
                )
            }
        }
    }

    private val windowManager: WindowManager by lazy {
        getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    private fun handleStartOverlay() {
        ViewTreeLifecycleOwner.set(composeView, this)
        ViewTreeViewModelStoreOwner.set(composeView) { ViewModelStore() }
        windowManager.addView(composeView, overlayParams)
    }

    private fun handleStopOverlay() {
        windowManager.removeView(composeView)
    }
}
