package com.inspiredandroid.kai.ui.settings

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.inspiredandroid.kai.ui.AccentPrimary
import com.inspiredandroid.kai.ui.BorderSubtle
import com.inspiredandroid.kai.ui.CardFill
import com.inspiredandroid.kai.ui.handCursor
import com.inspiredandroid.kai.ui.isOledFlavor
import kai.composeapp.generated.resources.Res
import kai.composeapp.generated.resources.settings_tab_agent
import kai.composeapp.generated.resources.settings_tab_general
import kai.composeapp.generated.resources.settings_tab_integrations
import kai.composeapp.generated.resources.settings_tab_sandbox
import kai.composeapp.generated.resources.settings_tab_services
import kai.composeapp.generated.resources.settings_tab_tools
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.stringResource

@Composable
fun SettingsNavRail(
    tabs: ImmutableList<SettingsTab>,
    currentTab: SettingsTab,
    onSelectTab: (SettingsTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isOled = MaterialTheme.colorScheme.isOledFlavor
    val railBg = if (isOled) CardFill else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
    val railBorder = if (isOled) BorderSubtle else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)

    Surface(
        modifier = modifier
            .fillMaxHeight()
            .width(116.dp)
            .padding(12.dp),
        color = railBg,
        shape = SettingsShapes.panel,
        border = androidx.compose.foundation.BorderStroke(1.dp, railBorder),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .padding(vertical = 14.dp, horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            tabs.forEach { tab ->
                val isSelected = currentTab == tab
                val label = when (tab) {
                    SettingsTab.General -> stringResource(Res.string.settings_tab_general)
                    SettingsTab.Agent -> stringResource(Res.string.settings_tab_agent)
                    SettingsTab.Services -> stringResource(Res.string.settings_tab_services)
                    SettingsTab.Tools -> stringResource(Res.string.settings_tab_tools)
                    SettingsTab.Integrations -> stringResource(Res.string.settings_tab_integrations)
                    SettingsTab.Sandbox -> stringResource(Res.string.settings_tab_sandbox)
                    SettingsTab.Data -> "Data Control"
                    SettingsTab.Account -> "Account & Plan"
                }

                val bgColor by animateColorAsState(
                    targetValue = if (isSelected) AccentPrimary.copy(alpha = 0.16f) else Color.Transparent,
                    animationSpec = tween(durationMillis = 200),
                    label = "railBg",
                )
                val textColor by animateColorAsState(
                    targetValue = if (isSelected) AccentPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                    animationSpec = tween(durationMillis = 200),
                    label = "railText",
                )
                val borderColor by animateColorAsState(
                    targetValue = if (isSelected) AccentPrimary.copy(alpha = 0.42f) else Color.Transparent,
                    animationSpec = tween(durationMillis = 200),
                    label = "railBorder",
                )

                Box(
                    modifier = Modifier
                        .width(88.dp)
                        .height(74.dp)
                        .clip(SettingsShapes.row)
                        .background(bgColor)
                        .border(
                            width = if (isSelected) 1.dp else 0.dp,
                            color = borderColor,
                            shape = SettingsShapes.row,
                        )
                        .handCursor()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) { onSelectTab(tab) },
                    contentAlignment = Alignment.Center,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                            color = textColor,
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                        )
                        if (isSelected) {
                            Spacer(Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .size(4.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.radialGradient(
                                            colors = listOf(AccentPrimary, AccentPrimary.copy(alpha = 0.4f)),
                                        ),
                                    ),
                            )
                        }
                    }
                }
            }
        }
    }
}
