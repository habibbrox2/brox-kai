package com.inspiredandroid.kai.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.inspiredandroid.kai.ui.kaiAdaptiveCardBorder
import com.inspiredandroid.kai.ui.kaiAdaptiveCardColors
import com.inspiredandroid.kai.ui.isOledFlavor
import com.inspiredandroid.kai.ui.settings.SettingsSpacing

/**
 * Shared operational status vocabulary (Plan 4, Phase 1) so MCP, skills,
 * native tools, and sandbox setup all describe the same states the same way.
 */
enum class OperationalStatus {
    /** Live and reachable. */
    Connected,

    /** Handshake or install in flight. */
    Connecting,

    /** Present but switched off. */
    Disabled,

    /** Being installed or updated. */
    Installing,

    /** Usable only after a prerequisite (e.g. sandbox) is set up. */
    NeedsSetup,

    /** Attempted and failed; needs attention. */
    Error,

    /** On and participating right now (tool enabled, session running). */
    Active,

    /** Prerequisite satisfied and idle. */
    Ready,
}

/** One color ramp for every operational badge so status colors never diverge. */
object OperationalStatusColors {
    val connected: Color = Color(0xFF16C60C)
    val connecting: Color = Color(0xFF00BCD4)
    val disabled: Color = Color(0xFF8899A6)
    val installing: Color = Color(0xFF00BCD4)
    val needsSetup: Color = Color(0xFFFFB74D)
    val error: Color = Color(0xFFE74856)
    val active: Color = Color(0xFF16C60C)
    val ready: Color = Color(0xFF16C60C)

    fun forStatus(status: OperationalStatus): Color = when (status) {
        OperationalStatus.Connected -> connected
        OperationalStatus.Connecting -> connecting
        OperationalStatus.Disabled -> disabled
        OperationalStatus.Installing -> installing
        OperationalStatus.NeedsSetup -> needsSetup
        OperationalStatus.Error -> error
        OperationalStatus.Active -> active
        OperationalStatus.Ready -> ready
    }
}

/**
 * Status badge pill shared by MCP servers, skills, tools, and sandbox setup
 * (Plan 4, Phase 1). The dot is decorative — the label and the semantic
 * description carry the meaning for screen readers.
 */
@Composable
fun StatusBadge(
    label: String,
    status: OperationalStatus,
    modifier: Modifier = Modifier,
) {
    val color = OperationalStatusColors.forStatus(status)
    Row(
        modifier = modifier.semantics {
            contentDescription = "Status: $label"
        },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color),
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.Medium,
        )
    }
}

/**
 * Capability count card (Plan 4, Phase 1): one number plus a plain-language
 * label, used by the Tools overview to summarize enabled tools, connected MCP
 * servers, and installed skills at a glance.
 */
@Composable
fun CapabilityCountCard(
    label: String,
    count: String,
    modifier: Modifier = Modifier,
    status: OperationalStatus? = null,
) {
    val isOled = MaterialTheme.colorScheme.isOledFlavor
    val cardBg = if (isOled) {
        com.inspiredandroid.kai.ui.CardFill
    } else {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    }
    val cardBorder = if (isOled) {
        androidx.compose.foundation.BorderStroke(1.dp, com.inspiredandroid.kai.ui.BorderSoft)
    } else {
        androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    }
    val cardShape = com.inspiredandroid.kai.ui.settings.SettingsShapes.card

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = cardBorder,
        shape = cardShape,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(SettingsSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = count,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (status != null) {
                StatusBadge(
                    label = when (status) {
                        OperationalStatus.Error -> "Needs attention"
                        OperationalStatus.NeedsSetup -> "Needs setup"
                        OperationalStatus.Disabled -> "Some disabled"
                        OperationalStatus.Ready -> "Ready"
                        OperationalStatus.Active -> "Active"
                        OperationalStatus.Connected -> "Connected"
                        OperationalStatus.Connecting -> "Working"
                        OperationalStatus.Installing -> "Installing"
                    },
                    status = status,
                )
            }
        }
    }
}

/**
 * Dedicated empty state for a collection (Plan 4, Phase 1) instead of a blank
 * list: one line explaining what would appear here.
 */
@Composable
fun CollectionEmptyState(
    message: String,
    modifier: Modifier = Modifier,
) {
    val isOled = MaterialTheme.colorScheme.isOledFlavor
    val cardBg = if (isOled) {
        com.inspiredandroid.kai.ui.CardFill
    } else {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
    }
    val cardBorder = if (isOled) {
        androidx.compose.foundation.BorderStroke(1.dp, com.inspiredandroid.kai.ui.BorderSoft)
    } else {
        androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
    }
    val cardShape = com.inspiredandroid.kai.ui.settings.SettingsShapes.card

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(cardBg)
            .border(cardBorder, cardShape)
            .padding(SettingsSpacing.xl),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}
