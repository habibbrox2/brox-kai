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
import com.inspiredandroid.kai.ui.AccentPrimary
import com.inspiredandroid.kai.ui.BorderSoft
import com.inspiredandroid.kai.ui.DangerAlert
import com.inspiredandroid.kai.ui.KaiShapes
import com.inspiredandroid.kai.ui.KaiTypography
import com.inspiredandroid.kai.ui.SuccessGreen
import com.inspiredandroid.kai.ui.TextPrimary
import com.inspiredandroid.kai.ui.WarningAmber

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
            background = SuccessGreen,
            text = TextPrimary,
            border = SuccessGreen,
        )
    SnackbarVariant.Warning ->
        SnackbarColors(
            background = WarningAmber,
            text = TextPrimary,
            border = WarningAmber,
        )
    SnackbarVariant.Error ->
        SnackbarColors(
            background = DangerAlert,
            text = TextPrimary,
            border = DangerAlert,
        )
    SnackbarVariant.Info ->
        SnackbarColors(
            background = AccentPrimary,
            text = colorScheme.onPrimary,
            border = AccentPrimary,
        )
}

/** Visual variants for KaiSnackbar. */
enum class SnackbarVariant {
    Info,
    Success,
    Warning,
    Error,
}
