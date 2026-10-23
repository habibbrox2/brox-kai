package com.inspiredandroid.kai.ui.settings

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.inspiredandroid.kai.ui.AccentGlow
import com.inspiredandroid.kai.ui.AccentPrimary
import com.inspiredandroid.kai.ui.BorderSoft
import com.inspiredandroid.kai.ui.CardFill
import com.inspiredandroid.kai.ui.DangerAlert
import com.inspiredandroid.kai.ui.OledBlack
import com.inspiredandroid.kai.ui.SuccessGreen
import com.inspiredandroid.kai.ui.SurfaceRaised
import com.inspiredandroid.kai.ui.TextPrimary
import com.inspiredandroid.kai.ui.TextSecondary
import com.inspiredandroid.kai.ui.WarningAmber

/**
 * Shared design tokens for the Brox Kai settings control center.
 * Updated for Futuristic Midnight Glassmorphism design system.
 */
object SettingsSpacing {
    val xxs: Dp = 2.dp
    val xs: Dp = 4.dp
    val sm: Dp = 8.dp
    val md: Dp = 12.dp
    val lg: Dp = 16.dp
    val xl: Dp = 24.dp
    val xxl: Dp = 32.dp

    val minTouchTarget: Dp = 48.dp
}

object SettingsShapes {
    val card = RoundedCornerShape(16.dp)
    val section = RoundedCornerShape(20.dp)
    val row = RoundedCornerShape(12.dp)
    val pill = RoundedCornerShape(999.dp)
    val input = RoundedCornerShape(12.dp)
    val button = RoundedCornerShape(999.dp)
}

object SettingsStatusColors {
    val connected = SuccessGreen
    val checking = WarningAmber
    val error = DangerAlert
    val unknown = TextSecondary
    val attention = WarningAmber
    val glow = AccentGlow
}

object SettingsSurfaces {
    /** Page background - OLED black in dark themes */
    @Composable
    fun page(): Color = when {
        MaterialTheme.colorScheme.background == OledBlack -> OledBlack
        MaterialTheme.colorScheme.background.luminance() > 0.5f -> Color(0xFFFFFFFF)
        else -> OledBlack
    }

    /** Glassmorphic card background with blur effect */
    @Composable
    fun card(): Color = CardFill

    /** Card border - subtle white with low opacity */
    @Composable
    fun border(): Color = BorderSoft

    /** Raised surface for floating elements */
    @Composable
    fun raised(): Color = SurfaceRaised

    /** Primary accent color for interactive elements */
    @Composable
    fun accent(): Color = AccentPrimary

    /** Text colors */
    @Composable
    fun textPrimary(): Color = TextPrimary
    @Composable
    fun textSecondary(): Color = TextSecondary

    /** Danger/destructive color */
    @Composable
    fun danger(): Color = DangerAlert
}
