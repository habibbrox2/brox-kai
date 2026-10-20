package com.inspiredandroid.kai.ui.chat.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.inspiredandroid.kai.ui.chat.ChatSpacing
import com.inspiredandroid.kai.ui.handCursor

/**
 * Consistent message action row (Copy / Retry / Share / Speech) used by assistant
 * messages (Phase 3 of the chat UI upgrade plan). Reuses [ChatActionButton] so
 * every action keeps a 48dp touch target and a content description (Phase 5).
 */
@Composable
fun MessageActionRow(
    onCopy: () -> Unit,
    onRetry: (() -> Unit)? = null,
    onShare: (() -> Unit)? = null,
    onSpeak: (() -> Unit)? = null,
    onThumbsUp: (() -> Unit)? = null,
    onThumbsDown: (() -> Unit)? = null,
    thumbsUpSelected: Boolean = false,
    thumbsDownSelected: Boolean = false,
    onFlag: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(ChatSpacing.xxs),
    ) {
        ChatActionButton(
            icon = Icons.Default.ContentCopy,
            contentDescription = "Copy message",
            onClick = onCopy,
        )
        if (onSpeak != null) {
            ChatActionButton(
                icon = Icons.AutoMirrored.Filled.VolumeUp,
                contentDescription = "Read aloud",
                onClick = onSpeak,
            )
        }
        if (onThumbsUp != null) {
            ChatActionButton(
                icon = if (thumbsUpSelected) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                contentDescription = "Thumbs up",
                onClick = onThumbsUp,
            )
        }
        if (onThumbsDown != null) {
            ChatActionButton(
                icon = if (thumbsDownSelected) Icons.Filled.ThumbDown else Icons.Outlined.ThumbDown,
                contentDescription = "Thumbs down",
                onClick = onThumbsDown,
            )
        }
        if (onRetry != null) {
            ChatActionButton(
                icon = Icons.Default.Refresh,
                contentDescription = "Regenerate",
                onClick = onRetry,
            )
        }
        if (onFlag != null) {
            ChatActionButton(
                icon = Icons.Default.Flag,
                contentDescription = "Report",
                onClick = onFlag,
            )
        }
        if (onShare != null) {
            ChatActionButton(
                icon = Icons.Default.Share,
                contentDescription = "Share",
                onClick = onShare,
            )
        }
    }
}
