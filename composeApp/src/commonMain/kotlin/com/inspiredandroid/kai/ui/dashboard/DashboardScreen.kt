package com.inspiredandroid.kai.ui.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.inspiredandroid.kai.BackIcon
import com.inspiredandroid.kai.KaiBuildController
import com.inspiredandroid.kai.SandboxController
import com.inspiredandroid.kai.ui.chat.ChatUiState
import com.inspiredandroid.kai.ui.chat.ConversationSummary
import com.inspiredandroid.kai.ui.components.CollectionEmptyState
import com.inspiredandroid.kai.ui.components.OperationalStatus
import com.inspiredandroid.kai.ui.components.OperationalStatusColors
import com.inspiredandroid.kai.ui.components.StatusBadge
import com.inspiredandroid.kai.ui.handCursor
import com.inspiredandroid.kai.ui.kaiAdaptiveCardBorder
import com.inspiredandroid.kai.ui.kaiAdaptiveCardColors
import com.inspiredandroid.kai.ui.settings.SettingsTab
import kai.composeapp.generated.resources.Res
import kai.composeapp.generated.resources.logo
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject

private val dashboardDateFormat = DateTimeComponents.Format {
    day()
    char(' ')
    monthName(MonthNames.ENGLISH_ABBREVIATED)
    char(' ')
    year()
}

private fun formatDashboardDate(epochMillis: Long): String = try {
    kotlin.time.Instant.fromEpochMilliseconds(epochMillis).format(dashboardDateFormat)
} catch (_: Exception) {
    ""
}

@Composable
fun DashboardScreen(
    chatUiState: ChatUiState,
    isBuildAvailable: Boolean,
    isSandboxAvailable: Boolean,
    isDesignAvailable: Boolean,
    onNavigateToChat: () -> Unit,
    onOpenBuild: (String) -> Unit,
    onOpenSettings: (SettingsTab) -> Unit,
    onStartDesign: (String) -> Unit,
    onOpenConversation: (String) -> Unit,
    onNavigateBack: (() -> Unit)? = null,
    navigationTabBar: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    val sandboxController = koinInject<SandboxController>()
    val buildController = koinInject<KaiBuildController>()
    val sandboxStatus by sandboxController.status.collectAsStateWithLifecycle()
    val buildState by buildController.state.collectAsStateWithLifecycle()

    DashboardScreenContent(
        state = buildDashboardUiState(
            chatUiState = chatUiState,
            sandboxStatus = sandboxStatus,
            buildState = buildState,
            isBuildAvailable = isBuildAvailable,
            isSandboxAvailable = isSandboxAvailable,
            isDesignAvailable = isDesignAvailable,
        ),
        onNavigateToChat = onNavigateToChat,
        onOpenBuild = onOpenBuild,
        onOpenSettings = onOpenSettings,
        onStartDesign = onStartDesign,
        onOpenConversation = onOpenConversation,
        onNavigateBack = onNavigateBack,
        navigationTabBar = navigationTabBar,
        modifier = modifier,
    )
}

internal fun dashboardColumnsFor(width: Dp): Int = when {
    width < 600.dp -> 1
    width < 1000.dp -> 2
    else -> 4
}

@Composable
internal fun DashboardScreenContent(
    state: DashboardUiState,
    onNavigateToChat: () -> Unit,
    onOpenBuild: (String) -> Unit,
    onOpenSettings: (SettingsTab) -> Unit,
    onStartDesign: (String) -> Unit,
    onOpenConversation: (String) -> Unit,
    onNavigateBack: (() -> Unit)? = null,
    navigationTabBar: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
    ) {
        val columns = dashboardColumnsFor(maxWidth)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = if (columns == 1) 16.dp else 24.dp)
                .padding(vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            DashboardHeader(
                navigationTabBar = navigationTabBar,
                onNavigateBack = onNavigateBack,
            )
            ReadinessCard(state = state, onOpenSettings = onOpenSettings)
            DashboardActionGrid(
                state = state,
                columns = columns,
                onNavigateToChat = onNavigateToChat,
                onOpenBuild = onOpenBuild,
                onOpenSettings = onOpenSettings,
                onStartDesign = onStartDesign,
            )
            RecentWorkSection(
                state = state,
                onOpenConversation = onOpenConversation,
                onOpenBuild = onOpenBuild,
                onViewAllConversations = onNavigateToChat,
                onViewAllProjects = { onOpenBuild("") },
            )
        }
    }
}

@Composable
private fun DashboardHeader(
    navigationTabBar: (@Composable () -> Unit)?,
    onNavigateBack: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    val brand: @Composable () -> Unit = {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (onNavigateBack != null) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.handCursor(),
                ) {
                    Icon(
                        imageVector = BackIcon,
                        contentDescription = "Back to chat",
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }
            Image(
                painter = painterResource(Res.drawable.logo),
                contentDescription = null,
                modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)),
            )
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = "Brox Kai",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Text(
                    text = "What are you building today?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
    if (navigationTabBar == null) {
        Box(modifier = modifier.fillMaxWidth()) {
            brand()
        }
    } else {
        Box(
            modifier = modifier.fillMaxWidth().defaultMinSize(minHeight = 64.dp),
        ) {
            Row(modifier = Modifier.align(Alignment.CenterStart)) {
                brand()
            }
            Box(modifier = Modifier.align(Alignment.Center)) {
                navigationTabBar()
            }
        }
    }
}

@Composable
private fun ReadinessCard(
    state: DashboardUiState,
    onOpenSettings: (SettingsTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = kaiAdaptiveCardColors(),
        border = kaiAdaptiveCardBorder(),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            when (val service = state.service) {
                null -> StatusBadge(
                    label = "Checking services",
                    status = OperationalStatus.Connecting,
                )

                else -> Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    StatusBadge(
                        label = if (service.isSharedKey) "Shared free service" else "Ready",
                        status = if (service.isSharedKey) {
                            OperationalStatus.NeedsSetup
                        } else {
                            OperationalStatus.Ready
                        },
                    )
                    if (service.isSharedKey) {
                        Spacer(modifier = Modifier.weight(1f))
                        TextButton(onClick = { onOpenSettings(SettingsTab.Model) }) {
                            Text("Set up a service")
                        }
                    } else {
                        Text(
                            text = if (service.modelId.isBlank()) {
                                service.serviceName
                            } else {
                                "${service.serviceName} · ${service.modelId}"
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }
            if (state.facts.isNotEmpty()) {
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.12f))
                state.facts.forEach { fact ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            text = fact.label,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.weight(1f),
                        )
                        StatusBadge(label = fact.value, status = fact.status)
                    }
                }
            }
        }
    }
}

private data class DashboardAction(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val isPrimary: Boolean,
    val isEnabled: Boolean,
    val disabledNote: String?,
    val onClick: () -> Unit,
)

@Composable
private fun DashboardActionGrid(
    state: DashboardUiState,
    columns: Int,
    onNavigateToChat: () -> Unit,
    onOpenBuild: (String) -> Unit,
    onOpenSettings: (SettingsTab) -> Unit,
    onStartDesign: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val actions = listOf(
        DashboardAction(
            title = "Chat",
            subtitle = "Ask, plan, and write with your AI services",
            icon = Icons.AutoMirrored.Filled.Chat,
            isPrimary = true,
            isEnabled = true,
            disabledNote = null,
            onClick = onNavigateToChat,
        ),
        DashboardAction(
            title = "Kai Build",
            subtitle = "Create apps in a Linux workspace on this device",
            icon = Icons.Filled.Build,
            isPrimary = false,
            isEnabled = state.isBuildAvailable,
            disabledNote = "Kai Build runs on Android devices",
            onClick = { onOpenBuild("") },
        ),
        DashboardAction(
            title = "Design",
            subtitle = "Start a guided design conversation",
            icon = Icons.Filled.Palette,
            isPrimary = false,
            isEnabled = state.isDesignAvailable,
            disabledNote = "Coming soon",
            onClick = { onStartDesign(DESIGN_STARTER_PROMPT) },
        ),
        DashboardAction(
            title = "Tools",
            subtitle = "Configure services, tools, and the sandbox",
            icon = Icons.Filled.Tune,
            isPrimary = false,
            isEnabled = true,
            disabledNote = null,
            onClick = { onOpenSettings(SettingsTab.General) },
        ),
    )
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        actions.chunked(columns).forEach { rowActions ->
            Row(
                modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                rowActions.forEach { action ->
                    DashboardActionCard(
                        action = action,
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                    )
                }
                repeat(columns - rowActions.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun DashboardActionCard(
    action: DashboardAction,
    modifier: Modifier = Modifier,
) {
    val container = if (action.isPrimary) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)
    } else {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
    }
    val iconTint = if (action.isPrimary) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onBackground
    }
    val border: BorderStroke? = if (action.isPrimary) {
        BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.45f))
    } else {
        kaiAdaptiveCardBorder()
    }
    val content: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit = {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(container),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = action.icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp),
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = action.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Text(
                    text = action.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                if (!action.isEnabled && action.disabledNote != null) {
                    Text(
                        text = action.disabledNote,
                        style = MaterialTheme.typography.labelSmall,
                        color = OperationalStatusColors.needsSetup,
                    )
                }
            }
        }
    }
    if (action.isEnabled) {
        Card(
            modifier = modifier.handCursor(),
            onClick = action.onClick,
            colors = kaiAdaptiveCardColors(),
            border = border,
            content = content,
        )
    } else {
        Card(
            modifier = modifier.alpha(0.6f),
            colors = kaiAdaptiveCardColors(),
            border = border,
            content = content,
        )
    }
}

@Composable
private fun RecentWorkSection(
    state: DashboardUiState,
    onOpenConversation: (String) -> Unit,
    onOpenBuild: (String) -> Unit,
    onViewAllConversations: () -> Unit,
    onViewAllProjects: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val conversations = state.recentConversations.take(4)
    val projects = state.recentProjects.take(4)

    if (conversations.isEmpty() && projects.isEmpty()) {
        CollectionEmptyState(
            message = "No saved work yet — conversations and build projects will show up here.",
        )
        return
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        if (conversations.isNotEmpty()) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Recent conversations",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.weight(1f),
                    )
                    TextButton(onClick = onViewAllConversations) {
                        Text("View all")
                    }
                }
                conversations.forEach { conversation ->
                    ConversationRow(
                        conversation = conversation,
                        onClick = { onOpenConversation(conversation.id) },
                    )
                }
            }
        }
        if (projects.isNotEmpty() && state.isBuildAvailable) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Recent projects",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.weight(1f),
                    )
                    TextButton(onClick = onViewAllProjects) {
                        Text("View all")
                    }
                }
                projects.forEach { project ->
                    ProjectRow(
                        project = project,
                        onClick = { onOpenBuild(project) },
                    )
                }
            }
        }
    }
}

@Composable
private fun ConversationRow(
    conversation: ConversationSummary,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth().handCursor(),
        onClick = onClick,
        colors = kaiAdaptiveCardColors(),
        border = kaiAdaptiveCardBorder(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    text = conversation.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = formatDashboardDate(conversation.updatedAt),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun ProjectRow(
    project: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth().handCursor(),
        onClick = onClick,
        colors = kaiAdaptiveCardColors(),
        border = kaiAdaptiveCardBorder(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(
                imageVector = Icons.Filled.Build,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = project,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
        }
    }
}
