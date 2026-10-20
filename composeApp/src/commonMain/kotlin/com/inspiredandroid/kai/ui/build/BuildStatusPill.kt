package com.inspiredandroid.kai.ui.build

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Terminal-style status pill (Plan 3, Phase 1). Small colored dot plus a label so
 * session tabs, the workspace header, and the setup flow all announce status the
 * same way. The dot is decorative; the text carries meaning for screen readers.
 */
@Composable
fun BuildStatusPill(
    label: String,
    kind: BuildStatusKind,
    modifier: Modifier = Modifier,
) {
    val color = BuildStatusColors.forKind(kind)
    Surface(
        modifier = modifier.semantics {
            contentDescription = "Status: $label"
        },
        shape = BuildShapes.pill,
        color = color.copy(alpha = 0.14f),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = BuildSpacing.sm, vertical = BuildSpacing.xs),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(BuildSpacing.xs),
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(color, CircleShape),
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = color,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

/**
 * Step progress block (Plan 3, Phase 1). Shows the current install step, an
 * optional determinate progress bar, and the running/success status so setup and
 * agent installation progress read in plain language.
 */
@Composable
fun BuildProgressBlock(
    title: String,
    kind: BuildStatusKind,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    progress: Float? = null,
) {
    val color = BuildStatusColors.forKind(kind)
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = BuildShapes.card,
        color = color.copy(alpha = 0.08f),
    ) {
        Column(
            modifier = Modifier.padding(BuildSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(BuildSpacing.sm),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(BuildSpacing.sm),
            ) {
                if (kind == BuildStatusKind.Running) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .width(16.dp)
                            .height(16.dp),
                        color = color,
                        trackColor = BuildSurfaces.progressTrack,
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(color, CircleShape),
                    )
                }
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f),
                )
                BuildStatusPill(label = kind.name.lowercase().replaceFirstChar { it.uppercase() }, kind = kind)
            }
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            if (progress != null) {
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp),
                    color = color,
                    trackColor = BuildSurfaces.progressTrack,
                )
            }
        }
    }
}
