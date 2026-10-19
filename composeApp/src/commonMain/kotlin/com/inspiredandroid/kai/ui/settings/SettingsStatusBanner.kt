package com.inspiredandroid.kai.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/** Severity of an inline settings status banner. */
enum class SettingsBannerSeverity {
    /** Neutral informational message. */
    Info,

    /** Something is progressing (install, sync, download). */
    Progress,

    /** Something needs the user's attention but is not fatal. */
    Warning,

    /** An action failed; the message explains what went wrong. */
    Error,
}

/**
 * Inline status banner shown inside a settings card (Plan 2, Phase 4).
 *
 * Makes progress, installation, sync, and error states explicit where they happen
 * rather than surfacing a transient snackbar, so the state stays attached to the
 * affected card until it resolves.
 */
@Composable
fun SettingsStatusBanner(
    message: String,
    severity: SettingsBannerSeverity,
    modifier: Modifier = Modifier,
    action: (@Composable () -> Unit)? = null,
) {
    val (container, content) = when (severity) {
        SettingsBannerSeverity.Info -> Color(0xFF2196F3) to Color(0xFF0D47A1)
        SettingsBannerSeverity.Progress -> Color(0xFF00BCD4) to Color(0xFF006064)
        SettingsBannerSeverity.Warning -> Color(0xFFFFB74D) to Color(0xFF5D4037)
        SettingsBannerSeverity.Error -> Color(0xFFF44336) to Color(0xFFB71C1C)
    }
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = SettingsShapes.row,
        color = container.copy(alpha = 0.14f),
    ) {
        Row(
            modifier = Modifier.padding(SettingsSpacing.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(SettingsSpacing.sm),
        ) {
            if (severity == SettingsBannerSeverity.Progress) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp,
                    color = container,
                )
            }
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = content,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f),
            )
            if (action != null) action()
        }
    }
}
