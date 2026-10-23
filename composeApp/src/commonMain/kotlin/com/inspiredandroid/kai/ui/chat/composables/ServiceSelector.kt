package com.inspiredandroid.kai.ui.chat.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import com.inspiredandroid.kai.data.ServiceEntry
import com.inspiredandroid.kai.ui.settings.SettingsModel
import com.inspiredandroid.kai.ui.handCursor
import com.inspiredandroid.kai.ui.isOledFlavor
import kai.composeapp.generated.resources.Res
import kai.composeapp.generated.resources.settings_model_label
import kai.composeapp.generated.resources.settings_model_loading
import kai.composeapp.generated.resources.settings_tab_services
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
internal fun ServiceSelector(
    services: ImmutableList<ServiceEntry>,
    onSelectService: (String) -> Unit,
    models: ImmutableList<SettingsModel> = persistentListOf(),
    isLoadingModels: Boolean = false,
    onSelectModel: (String) -> Unit = {},
    onLoadModels: () -> Unit = {},
    showLabel: Boolean = false,
) {
    if (services.isEmpty()) return

    val current = services.first()
    var expanded by remember { mutableStateOf(false) }
    // The model the chat will actually use: the effective id of the active instance, shown
    // under the name the provider gives it when the list is loaded.
    val currentModelLabel = remember(current.modelId, models) {
        models.firstOrNull { it.id == current.modelId }?.let { model ->
            model.displayName?.takeIf { it.isNotBlank() } ?: model.id
        } ?: current.modelId
    }

    // Ask for the provider's model list when the picker opens, so the choices are complete
    // without spending a request on every chat start.
    val currentOnLoadModels by rememberUpdatedState(onLoadModels)
    LaunchedEffect(expanded) {
        if (expanded) currentOnLoadModels()
    }

    Box {
        if (showLabel) {
            // Pill variant used by the Plan 7 top bar: icon + name + chevron.
            val isOled = MaterialTheme.colorScheme.isOledFlavor
            val pillBg = if (isOled) {
                com.inspiredandroid.kai.ui.SurfaceRaised.copy(alpha = 0.9f)
            } else {
                MaterialTheme.colorScheme.surfaceContainerHigh
            }
            val pillBorder = if (isOled) {
                com.inspiredandroid.kai.ui.BorderSoft
            } else {
                MaterialTheme.colorScheme.outlineVariant
            }

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(pillBg, RoundedCornerShape(50))
                    .border(androidx.compose.foundation.BorderStroke(1.dp, pillBorder), RoundedCornerShape(50))
                    .clickable { expanded = true }
                    .handCursor()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = vectorResource(current.icon),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = current.serviceName,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.widthIn(max = 120.dp),
                )
                if (currentModelLabel.isNotBlank()) {
                    Text(
                        text = " · $currentModelLabel",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.widthIn(max = 160.dp),
                    )
                }
                Spacer(Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = current.serviceName,
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        } else {
            val isOled = MaterialTheme.colorScheme.isOledFlavor
            val circleBg = if (isOled) {
                com.inspiredandroid.kai.ui.SurfaceRaised.copy(alpha = 0.9f)
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
            val circleBorder = if (isOled) {
                com.inspiredandroid.kai.ui.BorderSoft
            } else {
                MaterialTheme.colorScheme.outlineVariant
            }

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(circleBg, CircleShape)
                    .border(androidx.compose.foundation.BorderStroke(1.dp, circleBorder), CircleShape)
                    .clickable { expanded = true }
                    .handCursor(),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = vectorResource(current.icon),
                    contentDescription = current.serviceName,
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        if (expanded) {
            val spacingPx = with(LocalDensity.current) { 8.dp.roundToPx() }
            val isOled = MaterialTheme.colorScheme.isOledFlavor
            val popupBg = if (isOled) {
                com.inspiredandroid.kai.ui.CardFill
            } else {
                MaterialTheme.colorScheme.surface
            }
            val popupBorder = if (isOled) {
                androidx.compose.foundation.BorderStroke(1.dp, com.inspiredandroid.kai.ui.BorderSoft)
            } else {
                androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            }

            Popup(
                onDismissRequest = { expanded = false },
                properties = PopupProperties(focusable = false),
                popupPositionProvider = remember(spacingPx) { AnchorAbovePositionProvider(spacingPx) },
            ) {
                BoxWithConstraints {
                    val maxMenuHeight = maxHeight - 24.dp // keep a margin from screen edges
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = popupBg,
                        border = popupBorder,
                        tonalElevation = if (isOled) 3.dp else 0.dp,
                        shadowElevation = if (isOled) 8.dp else 0.dp,
                    ) {
                        Column(
                            modifier = Modifier
                                .heightIn(max = maxMenuHeight)
                                .verticalScroll(rememberScrollState())
                                .padding(vertical = 4.dp),
                        ) {
                            // Models of the active service: the model that is loaded and the
                            // alternatives the user can switch to.
                            if (models.isNotEmpty()) {
                                PickerSectionLabel(stringResource(Res.string.settings_model_label))
                                models.forEach { model ->
                                    val isCurrent = model.id == current.modelId
                                    ModelMenuItem(
                                        model = model,
                                        isCurrent = isCurrent,
                                        onClick = {
                                            expanded = false
                                            if (!isCurrent) onSelectModel(model.id)
                                        },
                                    )
                                }
                                HorizontalDivider(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.12f),
                                    modifier = Modifier.padding(vertical = 4.dp),
                                )
                            } else if (isLoadingModels) {
                                PickerSectionLabel(stringResource(Res.string.settings_model_label))
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(16.dp),
                                        strokeWidth = 2.dp,
                                        color = MaterialTheme.colorScheme.primary,
                                    )
                                    Text(
                                        text = stringResource(Res.string.settings_model_loading),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                            }

                            PickerSectionLabel(stringResource(Res.string.settings_tab_services))
                            services.forEach { entry ->
                                val isCurrent = entry.instanceId == current.instanceId
                                ServiceMenuItem(
                                    entry = entry,
                                    isCurrent = isCurrent,
                                    onClick = {
                                        expanded = false
                                        if (!isCurrent) {
                                            onSelectService(entry.instanceId)
                                        }
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Section heading inside the picker popup: "Model", "Services".
 */
@Composable
private fun PickerSectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
    )
}

/**
 * A model the active service can run. The loaded one is highlighted.
 */
@Composable
private fun ModelMenuItem(
    model: SettingsModel,
    isCurrent: Boolean,
    onClick: () -> Unit,
) {
    val isOled = MaterialTheme.colorScheme.isOledFlavor
    val rowBackground = if (isCurrent) {
        com.inspiredandroid.kai.ui.AccentPrimary.copy(alpha = 0.15f)
    } else {
        Color.Transparent
    }
    val textColor = if (isCurrent) {
        com.inspiredandroid.kai.ui.AccentPrimary
    } else {
        MaterialTheme.colorScheme.onSurface
    }
    val displayName = model.displayName?.takeIf { it.isNotBlank() && it != model.id }
    Row(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(rowBackground)
            .clickable(onClick = onClick)
            .handCursor()
            .padding(horizontal = 12.dp, vertical = 10.dp)
            .widthIn(min = 200.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(
                text = displayName ?: model.id,
                style = MaterialTheme.typography.bodyMedium,
                color = textColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (displayName != null) {
                Text(
                    text = model.id,
                    style = MaterialTheme.typography.bodySmall,
                    color = textColor.copy(alpha = 0.7f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun ServiceMenuItem(
    entry: ServiceEntry,
    isCurrent: Boolean,
    onClick: () -> Unit,
) {
    val isOled = MaterialTheme.colorScheme.isOledFlavor
    val rowBackground = if (isCurrent) {
        com.inspiredandroid.kai.ui.AccentPrimary.copy(alpha = 0.15f)
    } else {
        Color.Transparent
    }
    val textColor = if (isCurrent) {
        com.inspiredandroid.kai.ui.AccentPrimary
    } else {
        MaterialTheme.colorScheme.onSurface
    }
    val subTextColor = if (isCurrent) {
        com.inspiredandroid.kai.ui.AccentPrimary.copy(alpha = 0.7f)
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }
    Row(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(rowBackground)
            .clickable(onClick = onClick)
            .handCursor()
            .padding(horizontal = 12.dp, vertical = 10.dp)
            .widthIn(min = 200.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = vectorResource(entry.icon),
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = textColor,
        )
        Spacer(Modifier.width(12.dp))
        Column {
            Text(
                text = entry.serviceName,
                style = MaterialTheme.typography.bodyMedium,
                color = textColor,
            )
            if (entry.modelId.isNotEmpty()) {
                Text(
                    text = entry.modelId,
                    style = MaterialTheme.typography.bodySmall,
                    color = subTextColor,
                )
            }
        }
    }
}

private class AnchorAbovePositionProvider(
    private val verticalSpacing: Int,
) : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize,
    ): IntOffset {
        val maxX = (windowSize.width - popupContentSize.width).coerceAtLeast(0)
        val x = (anchorBounds.right - popupContentSize.width).coerceIn(0, maxX)
        val above = anchorBounds.top - popupContentSize.height - verticalSpacing
        val y = if (above >= 0) {
            above
        } else {
            val maxY = (windowSize.height - popupContentSize.height).coerceAtLeast(0)
            (anchorBounds.bottom + verticalSpacing).coerceAtMost(maxY)
        }
        return IntOffset(x, y)
    }
}
