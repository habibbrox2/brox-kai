package com.inspiredandroid.kai.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.inspiredandroid.kai.ui.KaiShapes
import com.inspiredandroid.kai.ui.KaiTypography

/** Color configuration for a KaiSnackbar variant. */
private data class SnackbarColors(
    val background: Color,
    val text: Color,
    val border: Color,
)

/**
 * Lightweight toast/snackbar shown at the bottom of the screen.
 *
 * @param message Message text
 * @param variant Visual variant: info, success, warning, or error
 * @param onDismiss Called when the user dismisses the snackbar
 * @param modifier Compose modifier
 */
@Composable
fun KaiSnackbar(
    message: String,
    variant: SnackbarVariant = SnackbarVariant.Info,
    onDismiss: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    val colorScheme = MaterialTheme.colorScheme
    val colors = snackbarColors(variant, colorScheme)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = KaiShapes.pill,
        color = colors.background,
        border = BorderStroke(1.dp, colors.border),
        contentColor = colors.text,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = message,
                style = KaiTypography.bodySmall,
                modifier = Modifier.weight(1f),
            )
            onDismiss?.let {
                IconButton(
                    onClick = it,
                    modifier = Modifier.padding(start = 4.dp),
                ) {
                    androidx.compose.material3.Text(
                        text = "✕",
                        style = KaiTypography.labelSmall,
                        color = colors.text,
                    )
                }
            }
        }
    }
}

private fun snackbarColors(
    variant: SnackbarVariant,
    colorScheme: androidx.compose.material3.ColorScheme,
): SnackbarColors = when (variant) {
    SnackbarVariant.Success ->
        SnackbarColors(
            background = colorScheme.tertiaryContainer,
            text = colorScheme.onTertiaryContainer,
            border = colorScheme.tertiary,
        )

    SnackbarVariant.Warning ->
        SnackbarColors(
            background = colorScheme.secondaryContainer,
            text = colorScheme.onSecondaryContainer,
            border = colorScheme.secondary,
        )

    SnackbarVariant.Error ->
        SnackbarColors(
            background = colorScheme.errorContainer,
            text = colorScheme.onErrorContainer,
            border = colorScheme.error,
        )

    SnackbarVariant.Info ->
        SnackbarColors(
            background = colorScheme.primaryContainer,
            text = colorScheme.onPrimaryContainer,
            border = colorScheme.primary,
        )
}

/** Visual variants for KaiSnackbar. */
enum class SnackbarVariant {
    Info,
    Success,
    Warning,
    Error,
}
