package com.example.systemoverlaycomposesample

import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.lifecycle.ViewTreeViewModelStoreOwner
import androidx.navigation.compose.rememberNavController
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.example.systemoverlaycomposesample.ui.theme.NavGraph
import com.example.systemoverlaycomposesample.ui.theme.SystemOverlayComposeSampleTheme

class OverlayService : LifecycleService(), SavedStateRegistryOwner, ViewModelStoreOwner {

    companion object {
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

    override val savedStateRegistry: SavedStateRegistry
        get() = savedStateRegistryController.savedStateRegistry

    private val savedStateRegistryController = SavedStateRegistryController.create(this)

    private val store: ViewModelStore = ViewModelStore()

    private val overlayParams = WindowManager.LayoutParams(
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
        PixelFormat.TRANSLUCENT
    )

    private var currentView: View? = null

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
        ComposeView(applicationContext).apply {
            setViewTreeSavedStateRegistryOwner(this@OverlayService)
            ViewTreeViewModelStoreOwner.set(this, this@OverlayService)
            setContent {
                SystemOverlayComposeSampleTheme {
                    NavGraph(
                        navController = rememberNavController().apply {
                            setViewModelStore(viewModelStore)
                        },
                        onSwitchLegacy = { changeRootView(legacyView) }
                    )
                }
            }
        }
    }

    private val legacyView: View by lazy {
        val inflater = LayoutInflater.from(applicationContext)
        inflater.inflate(R.layout.view_legacy, null).apply {
            findViewById<Button>(R.id.back_to_the_feature_button).setOnClickListener {
                changeRootView(composeView)
            }
        }
    }

    private val windowManager: WindowManager by lazy {
        getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    private fun handleStartOverlay() {
        ViewTreeLifecycleOwner.set(composeView, this)
        changeRootView(composeView)
    }

    private fun handleStopOverlay() {
        windowManager.removeView(currentView)
    }

    private fun changeRootView(view: View) {
        currentView?.run { windowManager.removeView(this) }
        windowManager.addView(view, overlayParams)
        currentView = view
    }

    override fun getViewModelStore(): ViewModelStore {
        return store
    }
}
