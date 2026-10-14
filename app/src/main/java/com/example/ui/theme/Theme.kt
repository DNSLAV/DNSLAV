package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val JinnDarkColorScheme = darkColorScheme(
    primary = SolomonGold,
    onPrimary = ObsidianVoid,
    primaryContainer = SolomonGoldDark,
    onPrimaryContainer = SolomonGoldBright,
    secondary = SmokelessFlameBlue,
    onSecondary = ObsidianVoid,
    secondaryContainer = CosmicAzure,
    onSecondaryContainer = EtherealCyan,
    tertiary = AstralPurple,
    onTertiary = ObsidianVoid,
    tertiaryContainer = OccultViolet,
    onTertiaryContainer = ParchmentLight,
    background = ObsidianVoid,
    onBackground = ParchmentLight,
    surface = ObsidianSurface,
    onSurface = ParchmentLight,
    surfaceVariant = ObsidianCard,
    onSurfaceVariant = ParchmentMuted,
    outline = ObsidianCardBorder,
    outlineVariant = SolomonGoldDark
)

@Composable
fun JinnSummonerTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = JinnDarkColorScheme,
        typography = AppTypography,
        content = content
    )
}
