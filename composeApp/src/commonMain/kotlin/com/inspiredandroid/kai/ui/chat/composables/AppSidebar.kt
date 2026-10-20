package com.inspiredandroid.kai.ui.chat.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.inspiredandroid.kai.ui.chat.ChatShapes
import com.inspiredandroid.kai.ui.chat.ChatSpacing
import com.inspiredandroid.kai.ui.chat.ChatUiState
import com.inspiredandroid.kai.ui.chat.ConversationSummary
import com.inspiredandroid.kai.ui.components.KaiSearchField
import com.inspiredandroid.kai.ui.handCursor
import kai.composeapp.generated.resources.Res
import kai.composeapp.generated.resources.chat_history_heartbeat_label
import kai.composeapp.generated.resources.ic_settings
import kai.composeapp.generated.resources.logo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

/**
 * Primary navigation sidebar (Plan 7, Phases 1 + 4).
 *
 * New Chat + search on top, a time-grouped conversation list in the middle
 * (Pinned / Today / Yesterday / Previous 7 Days / Previous 30 Days / Older),
 * and app-level destinations plus the brand mark at the bottom. Item actions
 * (rename / pin / delete) live in a per-row dropdown menu.
 */
@Composable
fun AppSidebar(
    uiState: ChatUiState,
    filteredConversations: ImmutableList<ConversationSummary>,
    onNewChat: () -> Unit,
    onConversationSelected: (String) -> Unit,
    onDeleteConversation: (String) -> Unit,
    onRenameConversation: (String, String) -> Unit,
    onTogglePinConversation: (String) -> Unit,
    onNavigateToDashboard: (() -> Unit)?,
    onNavigateToSettings: () -> Unit,
    onEnterInteractiveMode: (() -> Unit)?,
    onOpenKaiBuild: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    var query by remember { mutableStateOf("") }
    val visibleConversations = remember(filteredConversations, query) {
        if (query.isBlank()) filteredConversations
        else filteredConversations.filter { it.title.contains(query, ignoreCase = true) }.toImmutableList()
    }

    // Time-based grouping. Pinned conversations always float to the top group.
    val groups = remember(visibleConversations) { groupConversations(visibleConversations) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .padding(ChatSpacing.sm),
        verticalArrangement = Arrangement.spacedBy(ChatSpacing.xs),
    ) {
        Button(
            onClick = onNewChat,
            modifier = Modifier.fillMaxWidth().handCursor(),
            shape = ChatShapes.quickStartCard,
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
            )
            Spacer(Modifier.width(8.dp))
            Text("New chat", style = MaterialTheme.typography.labelLarge)
        }

        KaiSearchField(
            query = query,
            onQueryChange = { query = it },
            placeholder = "Search chats",
            modifier = Modifier.fillMaxWidth(),
        )

        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(ChatSpacing.xxs),
        ) {
            groups.forEach { (label, conversations) ->
                if (conversations.isEmpty()) return@forEach
                item(key = "header_$label") {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(
                            start = ChatSpacing.xs,
                            top = ChatSpacing.sm,
                            bottom = ChatSpacing.xxs,
                        ),
                    )
                }
                items(conversations, key = { it.id }) { conversation ->
                    ConversationRow(
                        conversation = conversation,
                        isSelected = conversation.id == uiState.currentConversationId,
                        onSelect = { onConversationSelected(conversation.id) },
                        onDelete = { onDeleteConversation(conversation.id) },
                        onRename = { newTitle -> onRenameConversation(conversation.id, newTitle) },
                        onTogglePin = { onTogglePinConversation(conversation.id) },
                    )
                }
            }
            if (visibleConversations.isEmpty()) {
                item {
                    Text(
                        text = if (query.isBlank()) "No chats yet" else "No matches",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = ChatSpacing.md, horizontal = ChatSpacing.xs),
                    )
                }
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

        onNavigateToDashboard?.let {
            SidebarLink(icon = Icons.Default.Dashboard, label = "Dashboard", onClick = it)
        }
        onEnterInteractiveMode?.let {
            SidebarLink(icon = Icons.Default.Widgets, label = "Interactive UI", onClick = it)
        }
        onOpenKaiBuild?.let {
            SidebarLink(icon = Icons.Default.Terminal, label = "Kai Build", onClick = it)
        }
        SidebarLink(
            icon = vectorResource(Res.drawable.ic_settings),
            label = "Settings",
            onClick = onNavigateToSettings,
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = ChatSpacing.xxs),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ChatSpacing.xs),
        ) {
            Image(
                painter = painterResource(Res.drawable.logo),
                contentDescription = null,
                modifier = Modifier.size(24.dp).clip(RoundedCornerShape(6.dp)),
            )
            Text(
                text = "Brox Kai 2.0",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
private fun ConversationRow(
    conversation: ConversationSummary,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onDelete: () -> Unit,
    onRename: (String) -> Unit,
    onTogglePin: () -> Unit,
) {
    var showMenu by remember { mutableStateOf(false) }
    var showRenameDialog by remember { mutableStateOf(false) }

    if (showRenameDialog) {
        RenameDialog(
            initial = conversation.title,
            onConfirm = { newTitle ->
                showRenameDialog = false
                if (newTitle.isNotBlank()) onRename(newTitle)
            },
            onDismiss = { showRenameDialog = false },
        )
    }

    Surface(
        onClick = onSelect,
        shape = ChatShapes.quickStartCard,
        color = if (isSelected) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            androidx.compose.ui.graphics.Color.Transparent
        },
        modifier = Modifier.fillMaxWidth().handCursor(),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = ChatSpacing.xs, vertical = ChatSpacing.xs),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ChatSpacing.xs),
        ) {
            Icon(
                imageVector = if (conversation.isPinned) Icons.Default.PushPin else Icons.AutoMirrored.Filled.Chat,
                contentDescription = null,
                tint = if (isSelected) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                modifier = Modifier.size(16.dp),
            )
            Column(modifier = Modifier.weight(1f)) {
                if (conversation.isHeartbeat) {
                    Text(
                        text = stringResource(Res.string.chat_history_heartbeat_label),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.tertiary,
                    )
                }
                Text(
                    text = conversation.title.ifBlank { "Untitled" },
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Box {
                IconButton(
                    onClick = { showMenu = true },
                    modifier = Modifier.size(24.dp).handCursor(),
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(14.dp),
                    )
                }
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                ) {
                    DropdownMenuItem(
                        text = { Text("Rename") },
                        leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp)) },
                        onClick = {
                            showMenu = false
                            showRenameDialog = true
                        },
                    )
                    DropdownMenuItem(
                        text = { Text(if (conversation.isPinned) "Unpin" else "Pin") },
                        leadingIcon = { Icon(Icons.Default.PushPin, contentDescription = null, modifier = Modifier.size(16.dp)) },
                        onClick = {
                            showMenu = false
                            onTogglePin()
                        },
                    )
                    DropdownMenuItem(
                        text = { Text("Delete") },
                        leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp)) },
                        onClick = {
                            showMenu = false
                            onDelete()
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun RenameDialog(
    initial: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var text by remember { mutableStateOf(initial) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Rename chat") },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(text) }) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
    )
}

@Composable
private fun SidebarLink(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        shape = ChatShapes.quickStartCard,
        color = androidx.compose.ui.graphics.Color.Transparent,
        modifier = Modifier.fillMaxWidth().handCursor(),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = ChatSpacing.xs, vertical = ChatSpacing.sm),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ChatSpacing.sm),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp),
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

private fun groupConversations(
    conversations: ImmutableList<ConversationSummary>,
): List<Pair<String, List<ConversationSummary>>> {
    val pinned = conversations.filter { it.isPinned }
    val unpinned = conversations.filter { !it.isPinned }

    val tz = TimeZone.currentSystemDefault()
    val today = Clock.System.now().toLocalDateTime(tz).date
    fun daysAgo(epochMillis: Long): Int {
        val d = Instant.fromEpochMilliseconds(epochMillis).toLocalDateTime(tz).date
        return (today.toEpochDays() - d.toEpochDays()).toInt()
    }

    val todayList = mutableListOf<ConversationSummary>()
    val yesterdayList = mutableListOf<ConversationSummary>()
    val weekList = mutableListOf<ConversationSummary>()
    val monthList = mutableListOf<ConversationSummary>()
    val olderList = mutableListOf<ConversationSummary>()

    for (c in unpinned) {
        when (val d = daysAgo(c.updatedAt)) {
            0 -> todayList.add(c)
            1 -> yesterdayList.add(c)
            in 2..7 -> weekList.add(c)
            in 8..30 -> monthList.add(c)
            else -> olderList.add(c)
        }
    }

    val result = mutableListOf<Pair<String, List<ConversationSummary>>>()
    if (pinned.isNotEmpty()) result.add("Pinned" to pinned)
    if (todayList.isNotEmpty()) result.add("Today" to todayList)
    if (yesterdayList.isNotEmpty()) result.add("Yesterday" to yesterdayList)
    if (weekList.isNotEmpty()) result.add("Previous 7 Days" to weekList)
    if (monthList.isNotEmpty()) result.add("Previous 30 Days" to monthList)
    if (olderList.isNotEmpty()) result.add("Older" to olderList)
    return result
}
