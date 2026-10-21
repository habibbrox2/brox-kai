package com.inspiredandroid.kai.ui.chat.composables

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.inspiredandroid.kai.BackIcon
import com.inspiredandroid.kai.data.ServiceEntry
import com.inspiredandroid.kai.ui.chat.ChatActions
import com.inspiredandroid.kai.ui.handCursor
import com.inspiredandroid.kai.ui.settings.SettingsModel
import kai.composeapp.generated.resources.Res
import kai.composeapp.generated.resources.ic_settings
import kai.composeapp.generated.resources.ic_volume_off
import kai.composeapp.generated.resources.ic_volume_up
import kai.composeapp.generated.resources.interactive_back_content_description
import kai.composeapp.generated.resources.interactive_mode_content_description
import kai.composeapp.generated.resources.sandbox_content_description
import kai.composeapp.generated.resources.settings_content_description
import kai.composeapp.generated.resources.toggle_speech_output_content_description
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import nl.marc_apps.tts.TextToSpeechInstance
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

/**
 * Chat workspace header (Plan 7, Phase 1).
 *
 * ChatGPT/Gemini-style layout: a drawer toggle on the left (only while the
 * sidebar is not permanently visible), the model selector pill centered, and
 * the quick actions (sandbox, speech output, settings) on the right. History
 * and new-chat live in [AppSidebar] now, so the legacy leading icons are gone.
 */
@Composable
internal fun TopBar(
    textToSpeech: TextToSpeechInstance? = null,
    isSpeechOutputEnabled: Boolean,
    isSpeaking: Boolean,
    actions: ChatActions,
    onNavigateToSettings: () -> Unit,
    isSandboxAvailable: Boolean,
    isSandboxOpen: Boolean,
    isShellExecuting: Boolean,
    onToggleSandbox: () -> Unit,
    onOpenDrawer: (() -> Unit)? = null,
    services: ImmutableList<ServiceEntry> = persistentListOf(),
    models: ImmutableList<SettingsModel> = persistentListOf(),
    isLoadingModels: Boolean = false,
    /** Interactive UI mode: the model answers with an interactive screen inside this chat. */
    isInteractiveMode: Boolean = false,
    onToggleInteractiveMode: (() -> Unit)? = null,
    /** Removes the last exchange — only offered while interactive mode is on. */
    onGoBack: (() -> Unit)? = null,
) {
    Box(
        modifier = Modifier.fillMaxWidth().defaultMinSize(minHeight = 56.dp),
    ) {
        Row(modifier = Modifier.align(Alignment.CenterStart)) {
            if (onOpenDrawer != null) {
                IconButton(
                    modifier = Modifier.handCursor(),
                    onClick = onOpenDrawer,
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Open navigation",
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }
            // Leading cluster: the interactive-UI toggle lives here so the right cluster
            // keeps room for the model pill on narrow screens.
            onToggleInteractiveMode?.let { toggle ->
                IconToggleButton(
                    checked = isInteractiveMode,
                    onCheckedChange = { toggle() },
                    modifier = Modifier.handCursor(),
                ) {
                    Icon(
                        imageVector = Icons.Default.Widgets,
                        contentDescription = stringResource(Res.string.interactive_mode_content_description),
                        tint = if (isInteractiveMode) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onBackground
                        },
                    )
                }
            }
        }
        Box(modifier = Modifier.align(Alignment.Center)) {
            if (services.isNotEmpty()) {
                ServiceSelector(
                    services = services,
                    onSelectService = actions.selectService,
                    models = models,
                    isLoadingModels = isLoadingModels,
                    onSelectModel = actions.selectModel,
                    onLoadModels = actions.loadModels,
                    showLabel = true,
                )
            }
        }
        Row(modifier = Modifier.align(Alignment.CenterEnd)) {
            onGoBack?.let { goBack ->
                IconButton(
                    modifier = Modifier.handCursor(),
                    onClick = goBack,
                ) {
                    Icon(
                        imageVector = BackIcon,
                        contentDescription = stringResource(Res.string.interactive_back_content_description),
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }
            if (isSandboxAvailable) {
                SandboxToggleButton(
                    isSandboxOpen = isSandboxOpen,
                    isShellExecuting = isShellExecuting,
                    onToggleSandbox = onToggleSandbox,
                )
            }
            if (textToSpeech != null) {
                SpeechToggleButton(textToSpeech, isSpeechOutputEnabled, isSpeaking, actions)
            }
            IconButton(
                modifier = Modifier.handCursor(),
                onClick = onNavigateToSettings,
            ) {
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_settings),
                    contentDescription = stringResource(Res.string.settings_content_description),
                    tint = MaterialTheme.colorScheme.onBackground,
                )
            }
        }
    }
}

@Composable
private fun SandboxToggleButton(
    isSandboxOpen: Boolean,
    isShellExecuting: Boolean,
    onToggleSandbox: () -> Unit,
) {
    val flashAlpha = remember { Animatable(0f) }
    LaunchedEffect(isShellExecuting) {
        if (isShellExecuting) {
            flashAlpha.snapTo(0.4f)
            flashAlpha.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
            )
        }
    }
    val primary = MaterialTheme.colorScheme.primary
    val checkedContainer = primary.copy(alpha = 0.2f)
    val flashContainer = primary.copy(alpha = flashAlpha.value)
    IconToggleButton(
        checked = isSandboxOpen,
        onCheckedChange = { onToggleSandbox() },
        modifier = Modifier.handCursor(),
        colors = IconButtonDefaults.iconToggleButtonColors(
            containerColor = flashContainer,
            checkedContainerColor = if (flashAlpha.value > 0f) flashContainer else checkedContainer,
            checkedContentColor = MaterialTheme.colorScheme.primary,
        ),
    ) {
        Icon(
            imageVector = Icons.Filled.Dns,
            contentDescription = stringResource(Res.string.sandbox_content_description),
            tint = if (isSandboxOpen) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onBackground
            },
        )
    }
}

@Composable
private fun SpeechToggleButton(
    textToSpeech: TextToSpeechInstance,
    isSpeechOutputEnabled: Boolean,
    isSpeaking: Boolean,
    actions: ChatActions,
) {
    IconButton(
        modifier = Modifier.handCursor(),
        onClick = {
            if (isSpeechOutputEnabled && isSpeaking) {
                actions.setIsSpeaking(false, "")
                textToSpeech.stop()
            }
            actions.toggleSpeechOutput()
        },
    ) {
        Icon(
            imageVector = if (isSpeechOutputEnabled) {
                vectorResource(Res.drawable.ic_volume_up)
            } else {
                vectorResource(Res.drawable.ic_volume_off)
            },
            contentDescription = stringResource(Res.string.toggle_speech_output_content_description),
            tint = MaterialTheme.colorScheme.onBackground,
        )
    }
}
