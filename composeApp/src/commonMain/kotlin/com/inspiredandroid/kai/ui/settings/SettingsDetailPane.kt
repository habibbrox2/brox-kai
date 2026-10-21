package com.inspiredandroid.kai.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.inspiredandroid.kai.linux.LinuxDistro
import com.inspiredandroid.kai.ui.components.VerticalScrollbarForScroll

/**
 * Detail pane for the settings screen (Plan 2, Phase 2). Renders the scrollable
 * content for the currently selected tab. Extracted so the adaptive shell can
 * place it beside a navigation rail on expanded windows.
 */
@Composable
fun SettingsDetailPane(
    uiState: SettingsUiState,
    actions: SettingsActions,
    sandboxState: SandboxUiState,
    onToggleSandbox: (Boolean) -> Unit,
    onSelectDistro: (LinuxDistro) -> Unit,
    onSetupSandbox: () -> Unit,
    onCancelSandbox: () -> Unit,
    onResetSandbox: () -> Unit,
    onInstallPackages: () -> Unit,
    onMigrateHome: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val settingsScrollState = rememberScrollState()
    Box(modifier = modifier.fillMaxWidth()) {
        Column(
            Modifier.fillMaxWidth().verticalScroll(settingsScrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(16.dp))

            val maxContentWidth = when (uiState.currentTab) {
                SettingsTab.Model -> 500.dp
                else -> 900.dp
            }
            Column(
                Modifier.widthIn(max = maxContentWidth).fillMaxWidth().padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                when (uiState.currentTab) {
                    SettingsTab.General -> {
                        SettingsOverview(uiState = uiState, sandboxInstalled = sandboxState.sandboxInstalled)
                        Spacer(Modifier.height(SettingsSpacing.md))
                        GeneralContent(uiState = uiState, actions = actions)
                        Spacer(Modifier.height(SettingsSpacing.lg))
                        // Folded sub-sections (Plan 7): nothing is lost.
                        AgentContent(uiState = uiState, actions = actions)
                        Spacer(Modifier.height(SettingsSpacing.lg))
                        ToolsContent(
                            tools = uiState.tools,
                            onToggleTool = actions.onToggleTool,
                            mcpServers = uiState.mcpServers,
                            onAddMcpServer = actions.onAddMcpServer,
                            onRemoveMcpServer = actions.onRemoveMcpServer,
                            onToggleMcpServer = actions.onToggleMcpServer,
                            onRefreshMcpServer = actions.onRefreshMcpServer,
                            showAddMcpServerDialog = uiState.showAddMcpServerDialog,
                            onShowAddMcpServerDialog = actions.onShowAddMcpServerDialog,
                            onAddPopularMcpServer = actions.onAddPopularMcpServer,
                            skills = uiState.skills,
                            onUninstallSkill = actions.onUninstallSkill,
                            showAddSkillDialog = uiState.showAddSkillDialog,
                            onShowAddSkillDialog = actions.onShowAddSkillDialog,
                            onInstallGitHubSkill = actions.onInstallGitHubSkill,
                            onInstallBrowsedSkill = actions.onInstallBrowsedSkill,
                            isInstallingSkill = uiState.isInstallingSkill,
                            skillInstallError = uiState.skillInstallError,
                            browsableSkills = uiState.browsableSkills,
                            isBrowsingSkills = uiState.isBrowsingSkills,
                            browseSkillsFailed = uiState.browseSkillsFailed,
                            showSkills = sandboxState.showSandbox,
                            isSandboxInstalled = sandboxState.sandboxInstalled,
                            onNavigateToSandbox = { actions.onSelectTab(SettingsTab.General) },
                        )
                        Spacer(Modifier.height(SettingsSpacing.lg))
                        if (sandboxState.showSandbox) {
                            SandboxSettingsCard(
                                sandboxState = sandboxState,
                                onToggleSandbox = onToggleSandbox,
                                onSelectDistro = onSelectDistro,
                                onSetupSandbox = onSetupSandbox,
                                onCancelSandbox = onCancelSandbox,
                                onResetSandbox = onResetSandbox,
                                onInstallPackages = onInstallPackages,
                                onMigrateHome = onMigrateHome,
                            )
                            Spacer(Modifier.height(SettingsSpacing.lg))
                        }
                        IntegrationsContent()
                    }

                    SettingsTab.Model -> {
                        ServicesContent(uiState = uiState, actions = actions)
                    }

                    SettingsTab.Data -> {
                        DataControlContent(uiState = uiState, actions = actions)
                    }

                    SettingsTab.Account -> {
                        AccountPlanContent(uiState = uiState)
                    }
                }

                Spacer(Modifier.height(16.dp))
            }

            Spacer(Modifier.weight(1f))

            BottomInfo()
        }
        VerticalScrollbarForScroll(
            scrollState = settingsScrollState,
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
        )
    }
}

@Composable
fun DataControlContent(
    uiState: SettingsUiState,
    actions: SettingsActions,
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "Data Control",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = "Manage your chat history and stored data.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Button(onClick = { actions.onClearAllConversations() }) {
            Text("Clear all conversations")
        }
    }
}

@Composable
fun AccountPlanContent(
    uiState: SettingsUiState,
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "Account and Plan",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = "Brox Kai 2.0",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = "Version " + com.inspiredandroid.kai.Version.appVersion,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
