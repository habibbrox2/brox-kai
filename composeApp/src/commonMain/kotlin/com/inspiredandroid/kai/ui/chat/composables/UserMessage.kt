package com.inspiredandroid.kai.ui.chat.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.inspiredandroid.kai.data.Attachment
import com.inspiredandroid.kai.decodeToImageBitmap
import com.inspiredandroid.kai.ui.chat.ChatSpacing
import com.inspiredandroid.kai.ui.components.LocalShowFullScreenImage
import com.inspiredandroid.kai.ui.handCursor
import com.inspiredandroid.kai.ui.isOledFlavor
import androidx.compose.foundation.border
import kai.composeapp.generated.resources.Res
import kai.composeapp.generated.resources.ic_file
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.painterResource
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class, ExperimentalLayoutApi::class)
@Composable
internal fun UserMessage(
    message: String,
    attachments: ImmutableList<Attachment> = persistentListOf(),
) {
    val showFullScreen = LocalShowFullScreenImage.current
    val isOled = MaterialTheme.colorScheme.isOledFlavor
    
    SelectionContainer {
        Row(Modifier.padding(ChatSpacing.lg)) {
            Spacer(Modifier.weight(1f))
            Column(
                modifier = Modifier
                    .background(
                        if (isOled) {
                            com.inspiredandroid.kai.ui.SurfaceRaised.copy(alpha = 0.9f)
                        } else {
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                        },
                        RoundedCornerShape(16.dp),
                    )
                    .border(
                        if (isOled) {
                            androidx.compose.foundation.BorderStroke(1.dp, com.inspiredandroid.kai.ui.AccentPrimary.copy(alpha = 0.3f))
                        } else {
                            androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                        },
                        RoundedCornerShape(16.dp)
                    )
                    .padding(ChatSpacing.lg),
                horizontalAlignment = Alignment.End,
            ) {
                val images = attachments.filter { it.mimeType.startsWith("image/") }
                val others = attachments.filter { !it.mimeType.startsWith("image/") }
                for (att in images) {
                    val imageBitmap = remember(att.data) {
                        try {
                            decodeToImageBitmap(Base64.decode(att.data))
                        } catch (_: Exception) {
                            null
                        }
                    }
                    if (imageBitmap != null) {
                        Image(
                            bitmap = imageBitmap,
                            contentDescription = "Image attachment",
                            modifier = Modifier
                                .widthIn(max = 200.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .handCursor()
                                .clickable { showFullScreen(imageBitmap) },
                            contentScale = ContentScale.FillWidth,
                        )
                        Spacer(Modifier.height(ChatSpacing.sm))
                    }
                }
                if (others.isNotEmpty()) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(ChatSpacing.sm),
                        verticalArrangement = Arrangement.spacedBy(ChatSpacing.xs),
                    ) {
                        for (att in others) {
                            key("${att.fileName ?: att.mimeType}_${att.data.take(8)}") {
                                val chipColors = SuggestionChipDefaults.suggestionChipColors(
                                    containerColor = if (isOled) {
                                        com.inspiredandroid.kai.ui.CardFill
                                    } else {
                                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                    },
                                    labelColor = MaterialTheme.colorScheme.onSurface,
                                )
                                SuggestionChip(
                                    onClick = {},
                                    colors = chipColors,
                                    icon = {
                                        Icon(
                                            modifier = Modifier.size(ChatSpacing.lg),
                                            painter = painterResource(Res.drawable.ic_file),
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurface,
                                        )
                                    },
                                    label = { Text(truncateFileName(att.fileName ?: att.mimeType)) },
                                )
                            }
                        }
                    }
                    if (message.isNotEmpty()) {
                        Spacer(Modifier.height(ChatSpacing.sm))
                    }
                }
                if (message.isNotEmpty()) {
                    Text(
                        text = message,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }
        }
    }
}