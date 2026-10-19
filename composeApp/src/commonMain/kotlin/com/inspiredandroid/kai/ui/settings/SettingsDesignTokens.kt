package com.inspiredandroid.kai.ui.settings

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.inspiredandroid.kai.ui.broxCyan
import com.inspiredandroid.kai.ui.broxDeepNavy
import com.inspiredandroid.kai.ui.broxMutedText
import com.inspiredandroid.kai.ui.broxNavy

/**
 * Shared design tokens for the Brox Kai settings control center (Plan 2, Phase 1).
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
}

object SettingsStatusColors {
    val connected = Color(0xFF4CAF50)
    val checking = Color(0xFFFF9800)
    val error = Color(0xFFF44336)
    val unknown = Color(0xFF9E9E9E)
    val attention = Color(0xFFFFB74D)
}

object SettingsSurfaces {
    @Composable
    fun page(): Color = if (MaterialTheme.colorScheme.background.luminance() > 0.5f) {
        Color(0xFFFFFFFF)
    } else {
        broxDeepNavy
    }

    @Composable
    fun card(): Color = if (MaterialTheme.colorScheme.background.luminance() > 0.5f) {
        Color(0xFFF5F8FC)
    } else {
        broxNavy
    }

    @Composable
    fun border(): Color = if (MaterialTheme.colorScheme.background.luminance() > 0.5f) {
        Color(0xFFDCE3EE)
    } else {
        Color(0xFF1F3355)
    }
}
