package com.inspiredandroid.kai.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.unit.dp
import com.inspiredandroid.kai.ui.AccentPrimary
import com.inspiredandroid.kai.ui.BorderSoft
import com.inspiredandroid.kai.ui.CardFill
import com.inspiredandroid.kai.ui.KaiShapes
import com.inspiredandroid.kai.ui.KaiTypography
import com.inspiredandroid.kai.ui.OledBlack
import com.inspiredandroid.kai.ui.SurfaceRaised
import com.inspiredandroid.kai.ui.TextPrimary
import com.inspiredandroid.kai.ui.TextSecondary

/**
 * Glassmorphism-styled dialog for confirmations, alerts, and forms.
 *
 * @param onDismissRequest Called when the user requests dismissal (backdrop click / escape)
 * @param title Optional dialog title
 * @param text Optional body text
 * @param confirmButton Optional confirm button (onClick + label)
 * @param dismissButton Optional dismiss button (onClick + label)
 * @param modifier Compose modifier
 */
@Composable
fun KaiDialog(
    onDismissRequest: () -> Unit,
    title: String? = null,
    text: String? = null,
    confirmButton: KaiDialogButton? = null,
    dismissButton: KaiDialogButton? = null,
    modifier: Modifier = Modifier,
) {
    val colorScheme = MaterialTheme.colorScheme
    val isOled = colorScheme.background == com.inspiredandroid.kai.ui.OledBlack

    val dialogFocusRequester = remember { FocusRequester() }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp)
            .focusRequester(dialogFocusRequester)
            .onKeyEvent { event ->
                if (event.key == Key.Escape) {
                    onDismissRequest()
                    true
                } else {
                    false
                }
            }
            .onFocusChanged { focused ->
                if (focused.isFocused) {
                    dialogFocusRequester.requestFocus()
                }
            },
        shape = KaiShapes.section,
        color = if (isOled) CardFill else SurfaceRaised,
        border = BorderStroke(1.dp, BorderSoft),
        contentColor = colorScheme.onSurface,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp),
        ) {
            // Request focus when dialog opens
            LaunchedEffect(Unit) {
                dialogFocusRequester.requestFocus()
            }

            title?.let { titleText ->
                Text(
                    text = titleText,
                    style = KaiTypography.headlineSmall,
                    color = colorScheme.onSurface,
                )
            }

            text?.let { bodyText ->
                Text(
                    text = bodyText,
                    style = KaiTypography.bodyMedium,
                    color = colorScheme.onSurfaceVariant,
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.End,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            ) {
                dismissButton?.let { btn ->
                    TextButton(
                        onClick = { btn.onClick(); onDismissRequest() },
                        modifier = Modifier.padding(end = 8.dp),
                    ) {
                        Text(text = btn.label)
                    }
                }

                confirmButton?.let { btn ->
                    TextButton(
                        onClick = { btn.onClick(); onDismissRequest() },
                    ) {
                        Text(text = btn.label)
                    }
                }
            }
        }
    }
}

/** Button configuration for KaiDialog. */
data class KaiDialogButton(
    val label: String,
    val onClick: () -> Unit,
)
