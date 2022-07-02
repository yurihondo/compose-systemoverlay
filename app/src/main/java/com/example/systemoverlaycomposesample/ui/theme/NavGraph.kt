package com.example.systemoverlaycomposesample.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope

enum class Destination(val id: String) {
    HELLO_WORLD("hello_world"),
    VIEW_1("view_1"),
    LEGACY_VIEW("legacy_view"),
}

@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    startDestination: String = "hello_world",
    onSwitchLegacy: (Destination) -> Unit = {}
) {
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentNavBackStackEntry?.destination?.route ?: startDestination

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(
            route = Destination.HELLO_WORLD.id,
        ) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.wrapContentSize(),
                    text = "Hello, World!",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = Typography.headlineLarge,
                )
                Spacer(modifier = Modifier.height(50.dp))
                Button(
                    onClick = { navController.navigate(Destination.VIEW_1.id) }) {
                    Text(text = "Next")
                }
            }
        }
        composable(
            route = Destination.VIEW_1.id
        ) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.wrapContentSize(),
                    text = "How's it going?",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = Typography.headlineLarge,
                )
                Spacer(modifier = Modifier.height(50.dp))
                Row {
                    Button(onClick = { navController.navigate(Destination.HELLO_WORLD.id) }) {
                        Text(text = "Back")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(onClick = { onSwitchLegacy(Destination.LEGACY_VIEW) }) {
                        Text(text = "Go to legacy")
                    }
                }
            }
        }
    }
}