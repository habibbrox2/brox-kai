package com.inspiredandroid.kai.ui.chat

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Centralized adaptive policy for the chat workspace (Phase 2 of the chat UI
 * upgrade plan). Compact / medium / expanded decisions are derived here so that
 * the shell, timeline and composer all react to a single source of truth.
 *
 * Breakpoints follow the Material 3 window size classes:
 *  - Compact  : width < 600dp  (phones)
 *  - Medium   : 600dp <= width < 840dp (tablets / small desktop)
 *  - Expanded : width >= 840dp (desktop / large tablets)
 */
enum class ChatWindowSizeClass { Compact, Medium, Expanded }

fun chatWindowSizeClass(width: Dp): ChatWindowSizeClass = when {
    width < 600.dp -> ChatWindowSizeClass.Compact
    width < 840.dp -> ChatWindowSizeClass.Medium
    else -> ChatWindowSizeClass.Expanded
}

object ChatAdaptivePolicy {
    /** Conversation column is centered and capped on expanded windows. */
    val conversationColumnMax: Dp = 760.dp

    /** Whether a persistent conversation rail should be shown. */
    fun showConversationRail(sizeClass: ChatWindowSizeClass): Boolean = sizeClass == ChatWindowSizeClass.Expanded

    /** Whether the sandbox/tool panel is shown side-by-side instead of as an overlay. */
    fun showSidePanel(sizeClass: ChatWindowSizeClass): Boolean = sizeClass == ChatWindowSizeClass.Expanded

    /** Whether history is presented as a bottom sheet (compact) vs. a side rail. */
    fun historyAsBottomSheet(sizeClass: ChatWindowSizeClass): Boolean = sizeClass != ChatWindowSizeClass.Expanded
}
