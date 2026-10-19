package com.inspiredandroid.kai.ui.chat

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
 * Shared design tokens for the Brox Kai chat workspace.
 *
 * Phase 1 of the chat UI upgrade plan introduces these tokens so that later
 * phases (responsive shell, timeline, composer) can consume a single source of
 * truth for spacing, shapes, surfaces and status colors instead of hard-coding
 * values inside individual composables.
 *
 * Nothing here moves business logic into the UI layer: these are pure
 * presentation constants and theme-aware color resolvers.
 */
object ChatSpacing {
    val xxs: Dp = 2.dp
    val xs: Dp = 4.dp
    val sm: Dp = 8.dp
    val md: Dp = 12.dp
    val lg: Dp = 16.dp
    val xl: Dp = 24.dp
    val xxl: Dp = 32.dp
    val xxxl: Dp = 48.dp

    /** Width of the centered conversation column on expanded windows. */
    val conversationColumnMax: Dp = 760.dp

    /** Minimum touch target enforced across the chat UI. */
    val minTouchTarget: Dp = 48.dp
}

object ChatShapes {
    /** User message bubble. */
    val userBubble = RoundedCornerShape(
        topStart = 20.dp,
        topEnd = 8.dp,
        bottomStart = 20.dp,
        bottomEnd = 20.dp,
    )

    /** Assistant message surface. */
    val assistantSurface = RoundedCornerShape(16.dp)

    /** Status card (streaming / tool / fallback / error). */
    val statusCard = RoundedCornerShape(12.dp)

    /** Quick-start card on the empty state. */
    val quickStartCard = RoundedCornerShape(16.dp)

    /** Composer container. */
    val composer = RoundedCornerShape(24.dp)

    /** Compact pill (status pills, chips). */
    val pill = RoundedCornerShape(999.dp)

    /** Panel / sheet heading container. */
    val panel = RoundedCornerShape(20.dp)
}

/**
 * Status colors used by timeline cards and status pills. These are brand-aligned
 * and chosen for contrast on both light and dark surfaces.
 */
object ChatStatusColors {
    val streaming = broxCyan
    val toolActivity = Color(0xFF168BDB)
    val fallback = Color(0xFFB388FF)
    val error = Color(0xFFFF5252)
    val retry = Color(0xFFFFB74D)
    val success = Color(0xFF4CAF50)
}

/**
 * Surface resolvers. Kept as functions (rather than constants) so they can react
 * to the active theme and OLED flavor.
 */
object ChatSurfaces {
    @Composable
    fun page(): Color = if (MaterialTheme.colorScheme.background.luminance() > 0.5f) {
        Color(0xFFFFFFFF)
    } else {
        broxDeepNavy
    }

    @Composable
    fun elevated(): Color = if (MaterialTheme.colorScheme.background.luminance() > 0.5f) {
        Color(0xFFF2F2F2)
    } else {
        broxNavy
    }

    @Composable
    fun border(): Color = if (MaterialTheme.colorScheme.background.luminance() > 0.5f) {
        Color(0xFFDCE3EE)
    } else {
        Color(0xFF1F3355)
    }

    @Composable
    fun assistantMessage(): Color = if (MaterialTheme.colorScheme.background.luminance() > 0.5f) {
        Color(0xFFF5F8FC)
    } else {
        Color(0xFF112240)
    }

    @Composable
    fun composer(): Color = if (MaterialTheme.colorScheme.background.luminance() > 0.5f) {
        Color(0xFFFFFFFF)
    } else {
        Color(0xFF0E1C36)
    }
}
