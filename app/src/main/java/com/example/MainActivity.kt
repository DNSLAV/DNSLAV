package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Flare
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppScreen
import com.example.ui.MainViewModel
import com.example.ui.screens.DivinationScreen
import com.example.ui.screens.GrimoireScreen
import com.example.ui.screens.JinnAudienceScreen
import com.example.ui.screens.SummoningChamberScreen
import com.example.ui.theme.JinnSummonerTheme
import com.example.ui.theme.ObsidianCardBorder
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ObsidianVoid
import com.example.ui.theme.ParchmentLight
import com.example.ui.theme.ParchmentMuted
import com.example.ui.theme.SmokelessFlameBlue
import com.example.ui.theme.SolomonGold
import com.example.ui.theme.SolomonGoldBright

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            JinnSummonerTheme {
                val currentScreen by viewModel.currentScreen.collectAsState()

                // BackHandler returns to the primary Summoning Circle if on secondary screens
                if (currentScreen != AppScreen.SUMMONING) {
                    BackHandler {
                        viewModel.navigateTo(AppScreen.SUMMONING)
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = ObsidianVoid,
                    bottomBar = {
                        JinnBottomNavigationBar(
                            currentScreen = currentScreen,
                            onScreenSelected = { viewModel.navigateTo(it) }
                        )
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        Crossfade(
                            targetState = currentScreen,
                            animationSpec = tween(300),
                            label = "screen_crossfade"
                        ) { screen ->
                            when (screen) {
                                AppScreen.SUMMONING -> SummoningChamberScreen(viewModel = viewModel)
                                AppScreen.AUDIENCE -> JinnAudienceScreen(viewModel = viewModel)
                                AppScreen.GRIMOIRE -> GrimoireScreen(viewModel = viewModel)
                                AppScreen.DIVINATION -> DivinationScreen(viewModel = viewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun JinnBottomNavigationBar(
    currentScreen: AppScreen,
    onScreenSelected: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .border(1.dp, ObsidianCardBorder)
            .navigationBarsPadding(),
        containerColor = ObsidianSurface,
        contentColor = SolomonGoldBright
    ) {
        val items = listOf(
            Triple(AppScreen.SUMMONING, Icons.Default.Flare, "Circle"),
            Triple(AppScreen.AUDIENCE, Icons.Default.AutoAwesome, "Audience"),
            Triple(AppScreen.GRIMOIRE, Icons.AutoMirrored.Filled.MenuBook, "Grimoire"),
            Triple(AppScreen.DIVINATION, Icons.Default.NightsStay, "Divination")
        )

        items.forEach { (screen, icon, label) ->
            val isSelected = currentScreen == screen
            NavigationBarItem(
                selected = isSelected,
                onClick = { onScreenSelected(screen) },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 11.sp
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = ObsidianVoid,
                    selectedTextColor = SolomonGoldBright,
                    indicatorColor = SolomonGold,
                    unselectedIconColor = ParchmentMuted,
                    unselectedTextColor = ParchmentMuted
                ),
                modifier = Modifier.testTag("nav_item_${screen.name.lowercase()}")
            )
        }
    }
}
