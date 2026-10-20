package com.inspiredandroid.kai.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList

/**
 * Vertical navigation rail used on expanded windows (Plan 2, Phase 2). Mirrors
 * the existing [SettingsTabSelector] but laid out vertically so desktop settings
 * use the available width instead of a stretched phone tab strip.
 */
@Composable
fun SettingsNavRail(
    tabs: ImmutableList<SettingsTab>,
    currentTab: SettingsTab,
    onSelectTab: (SettingsTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationRail(
        modifier = modifier
            .fillMaxHeight()
            .width(104.dp),
    ) {
        Spacer(Modifier.height(8.dp))
        tabs.forEach { tab ->
            val isSelected = currentTab == tab
            NavigationRailItem(
                selected = isSelected,
                onClick = { onSelectTab(tab) },
                icon = {
                    // Icon placeholder kept minimal; labels carry the meaning.
                },
                label = {
                    Text(
                        text = when (tab) {
                            SettingsTab.General -> "General"
                            SettingsTab.Model -> "Model & API Key"
                            SettingsTab.Data -> "Data Control"
                            SettingsTab.Account -> "Account & Plan"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    )
                },
            )
        }
    }
}
