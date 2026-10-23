@file:Suppress("DEPRECATION")

package com.inspiredandroid.kai.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

// ═══════════════════════════════════════════════════════════════
// BROX KAI 2.0 — FUTURISTIC MIDNIGHT GLASSMORPHISM DESIGN SYSTEM
// ═══════════════════════════════════════════════════════════════

// ─── Core Brand Colors ───
val broxNavy = Color(0xFF0B2B5B)
val broxCyan = Color(0xFF00E5FF) // Updated to Neon Cyan #00E5FF
val broxDeepNavy = Color(0xFF06152E)
val broxCharcoal = Color(0xFF1A1F2B)
val broxLightSurface = Color(0xFFF5F8FC)
val broxMutedText = Color(0xFF718096)

// ─── Glassmorphism Design Tokens ───

/** Ultra-deep OLED background — pure black #000000 */
val OledBlack = Color.Black

/** Raised surface for floating panels, dialogs, drawer menus */
val SurfaceRaised = Color(0xFF0F172A)

/** Semi-transparent card fill with backdrop blur: rgba(30, 41, 59, 0.4) */
val CardFill = Color(0x661E293B)

/** Subtle 1px outline for cards and inputs: rgba(255, 255, 255, 0.08) */
val BorderSoft = Color(0x14FFFFFF)

/** High-visibility Neon Cyan for active toggles, primary actions, focus rings */
val AccentPrimary = broxCyan

/** Soft glow halo under active buttons and glowing status badges */
val AccentGlow = Color(0x4000E5FF)

/** Main body text, headers, and active tab labels */
val TextPrimary = Color(0xFFF8FAFC)

/** Subtitles, helper text, and secondary timestamps */
val TextSecondary = Color(0xFF94A3B8)

/** Destructive actions and critical status indicators */
val DangerAlert = Color(0xFFFF4D4D)

/** Success/connected state */
val SuccessGreen = Color(0xFF4CAF50)

/** Warning/pending state */
val WarningAmber = Color(0xFFFF9800)

// ─── KaiTypography — Centralized type scale for Futuristic Midnight Glassmorphism ───
val KaiTypography = Typography(
    displayLarge = TextStyle(
        fontSize = 57.sp,
        brush = SolidColor(TextPrimary),
    ),
    displayMedium = TextStyle(
        fontSize = 45.sp,
        brush = SolidColor(TextPrimary),
    ),
    displaySmall = TextStyle(
        fontSize = 36.sp,
        brush = SolidColor(TextPrimary),
    ),
    headlineLarge = TextStyle(
        fontSize = 32.sp,
        brush = SolidColor(TextPrimary),
    ),
    headlineMedium = TextStyle(
        fontSize = 28.sp,
        brush = SolidColor(TextPrimary),
    ),
    headlineSmall = TextStyle(
        fontSize = 24.sp,
        brush = SolidColor(TextPrimary),
    ),
    titleLarge = TextStyle(
        fontSize = 22.sp,
        brush = SolidColor(TextPrimary),
    ),
    titleMedium = TextStyle(
        fontSize = 16.sp,
        brush = SolidColor(TextPrimary),
    ),
    titleSmall = TextStyle(
        fontSize = 14.sp,
        brush = SolidColor(TextSecondary),
    ),
    bodyLarge = TextStyle(
        fontSize = 16.sp,
        brush = SolidColor(TextPrimary),
    ),
    bodyMedium = TextStyle(
        fontSize = 14.sp,
        brush = SolidColor(TextPrimary),
    ),
    bodySmall = TextStyle(
        fontSize = 12.sp,
        brush = SolidColor(TextSecondary),
    ),
    labelLarge = TextStyle(
        fontSize = 14.sp,
        brush = SolidColor(TextPrimary),
    ),
    labelMedium = TextStyle(
        fontSize = 12.sp,
        brush = SolidColor(TextSecondary),
    ),
    labelSmall = TextStyle(
        fontSize = 11.sp,
        brush = SolidColor(TextSecondary),
    ),
)

// ─── KaiShapes — Centralized shape definitions ───
object KaiShapes {
    /** Card / panel default radius */
    val card = RoundedCornerShape(16.dp)

    /** Section container radius */
    val section = RoundedCornerShape(20.dp)

    /** Row / item container radius */
    val row = RoundedCornerShape(12.dp)

    /** Pill / chip full radius */
    val pill = RoundedCornerShape(999.dp)

    /** Input field radius */
    val input = RoundedCornerShape(12.dp)

    /** Button radius */
    val button = RoundedCornerShape(999.dp)

    /** User message bubble */
    val userBubble = RoundedCornerShape(
        topStart = 20.dp,
        topEnd = 8.dp,
        bottomStart = 20.dp,
        bottomEnd = 20.dp,
    )

    /** Assistant message surface */
    val assistantSurface = RoundedCornerShape(16.dp)

    /** Status card */
    val statusCard = RoundedCornerShape(12.dp)

    /** Quick-start card */
    val quickStartCard = RoundedCornerShape(16.dp)

    /** Composer container */
    val composer = RoundedCornerShape(28.dp)

    /** Panel / sheet heading */
    val panel = RoundedCornerShape(20.dp)
}

// ─── KaiBorders — Centralized stroke definitions ───
object KaiBorders {
    /** Subtle 0.5px hairline */
    val hairline = BorderStroke(0.5.dp, BorderSoft)

    /** Standard 1px soft outline */
    val soft = BorderStroke(1.dp, BorderSoft)

    /** Neon cyan accent border */
    val accent = BorderStroke(1.dp, AccentPrimary)

    /** Focused state border with glow */
    val focus = BorderStroke(1.dp, AccentPrimary.copy(alpha = 0.6f))

    /** Input focused border */
    val inputFocused = BorderStroke(1.dp, AccentPrimary)

    /** Disabled border */
    val disabled = BorderStroke(1.dp, BorderSoft.copy(alpha = 0.3f))
}

// ─── KaiElevation — Cross-platform elevation using borders + alpha overlays ───
object KaiElevation {
    /** Subtle card elevation — soft border + faint overlay */
    val level1 = BorderStroke(1.dp, BorderSoft.copy(alpha = 0.8f))

    /** Raised panel elevation — soft border + visible overlay */
    val level2 = BorderStroke(1.dp, BorderSoft.copy(alpha = 1.0f))

    /** Interactive element elevation — accent border + glow overlay */
    val level3 = BorderStroke(1.dp, AccentPrimary.copy(alpha = 0.4f))

    /** Modal / dialog elevation — accent border + strong glow */
    val level4 = BorderStroke(1.dp, AccentPrimary.copy(alpha = 0.6f))
}

// ─── Legacy brand tokens (kept for migration) ───
val darkPurple = broxNavy
val lightPurple = Color(0xff8063C5)
val gradientBrush = androidx.compose.ui.graphics.Brush.horizontalGradient(listOf(broxNavy, broxCyan))

// ─── Animated border gradient colors ───
val gradientPurple = broxNavy
val gradientViolet = broxCyan
val gradientMagenta = broxDeepNavy

// ─── Utility Modifiers ───
fun Modifier.handCursor() = pointerHoverIcon(PointerIcon.Hand, overrideDescendants = true)

/** Glassmorphic card surface with backdrop blur effect */
@Composable
fun Modifier.glassmorphicSurface(
    shape: Shape = KaiShapes.card,
    elevation: Boolean = true,
): Modifier = this
    .clip(shape)
    .background(CardFill, shape)
    .border(KaiBorders.soft, shape)
    .then(
        if (elevation) {
            Modifier.border(KaiElevation.level1, shape)
        } else {
            Modifier
        },
    )

/** Glassmorphic card with hover/focus glow */
@Composable
fun Modifier.glassmorphicCard(
    shape: Shape = KaiShapes.card,
    isInteractive: Boolean = false,
    isFocused: Boolean = false,
    isHovered: Boolean = false,
): Modifier = this
    .clip(shape)
    .background(CardFill, shape)
    .border(
        if (isFocused || isHovered) KaiBorders.focus else KaiBorders.soft,
        shape,
    )
    .then(
        if (isInteractive && (isFocused || isHovered)) {
            Modifier.border(KaiElevation.level3, shape)
        } else if (isInteractive) {
            Modifier.border(KaiElevation.level1, shape)
        } else {
            Modifier
        },
    )

/** Cyan glow modifier for active/primary elements.
 *  Cross-platform implementation using border + alpha overlay instead of RenderEffect.
 */
@Composable
fun Modifier.cyanGlow(enabled: Boolean = true): Modifier = if (enabled) {
    this
        .border(KaiBorders.accent)
        .background(AccentGlow, shape = KaiShapes.pill)
} else {
    this
}

// ─── Color Schemes ───

/** Standard dark theme (grey-based) */
val DarkColorScheme = darkColorScheme(
    primary = AccentPrimary,
    onPrimary = OledBlack,
    primaryContainer = broxNavy,
    onPrimaryContainer = TextPrimary,
    secondary = broxDeepNavy,
    onSecondary = TextPrimary,
    tertiary = broxCharcoal,
    onTertiary = TextPrimary,
    error = DangerAlert,
    onError = OledBlack,
    errorContainer = DangerAlert.copy(alpha = 0.3f),
    onErrorContainer = TextPrimary,
    background = Color(0xFF121212),
    onBackground = TextPrimary,
    surface = Color(0xFF1E1E1E),
    onSurface = TextPrimary,
    surfaceVariant = Color(0xFF2A2A2A),
    onSurfaceVariant = TextSecondary,
    outline = BorderSoft,
    outlineVariant = BorderSoft.copy(alpha = 0.5f),
    inverseSurface = Color(0xFFE0E0E0),
    inverseOnSurface = OledBlack,
    inversePrimary = broxNavy,
    surfaceContainerHighest = Color(0xFF2A2A2A),
    surfaceContainerHigh = Color(0xFF242424),
    surfaceContainer = Color(0xFF1E1E1E),
    surfaceContainerLow = Color(0xFF1A1A1A),
    surfaceContainerLowest = Color(0xFF121212),
)

/** Midnight theme — deep navy surfaces with cyan accent */
val MidnightColorScheme = darkColorScheme(
    primary = AccentPrimary,
    onPrimary = OledBlack,
    primaryContainer = broxNavy,
    onPrimaryContainer = TextPrimary,
    secondary = broxDeepNavy,
    onSecondary = TextPrimary,
    tertiary = broxCharcoal,
    onTertiary = TextPrimary,
    error = DangerAlert,
    onError = OledBlack,
    errorContainer = DangerAlert.copy(alpha = 0.3f),
    onErrorContainer = TextPrimary,
    background = broxDeepNavy,
    onBackground = TextPrimary,
    surface = Color(0xFF0E2A4D),
    onSurface = TextPrimary,
    surfaceVariant = Color(0xFF15355A),
    onSurfaceVariant = TextSecondary,
    outline = BorderSoft,
    outlineVariant = BorderSoft.copy(alpha = 0.5f),
    inverseSurface = Color(0xFFE0E0E0),
    inverseOnSurface = OledBlack,
    inversePrimary = broxNavy,
    surfaceContainerHighest = Color(0xFF15355A),
    surfaceContainerHigh = Color(0xFF112D4D),
    surfaceContainer = Color(0xFF0E2A4D),
    surfaceContainerLow = Color(0xFF0A223A),
    surfaceContainerLowest = broxDeepNavy,
)

/** Pure OLED black theme — maximum contrast, power saving */
val OledColorScheme = darkColorScheme(
    primary = AccentPrimary,
    onPrimary = OledBlack,
    primaryContainer = broxNavy,
    onPrimaryContainer = TextPrimary,
    secondary = broxDeepNavy,
    onSecondary = TextPrimary,
    tertiary = broxCharcoal,
    onTertiary = TextPrimary,
    error = DangerAlert,
    onError = OledBlack,
    errorContainer = DangerAlert.copy(alpha = 0.3f),
    onErrorContainer = TextPrimary,
    background = OledBlack,
    onBackground = TextPrimary,
    surface = OledBlack,
    onSurface = TextPrimary,
    surfaceVariant = Color(0xFF0F172A),
    onSurfaceVariant = TextSecondary,
    outline = BorderSoft,
    outlineVariant = BorderSoft.copy(alpha = 0.5f),
    inverseSurface = Color(0xFFE0E0E0),
    inverseOnSurface = OledBlack,
    inversePrimary = broxNavy,
    surfaceContainerHighest = Color(0xFF0F172A),
    surfaceContainerHigh = Color(0xFF0A0E17),
    surfaceContainer = OledBlack,
    surfaceContainerLow = OledBlack,
    surfaceContainerLowest = OledBlack,
)

/** Light theme */
val LightColorScheme = lightColorScheme(
    primary = broxNavy,
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFD6E4F0),
    onPrimaryContainer = broxNavy,
    secondary = Color(0xFFE8F0F8),
    onSecondary = broxNavy,
    tertiary = Color(0xFFF0F4FA),
    onTertiary = broxNavy,
    error = DangerAlert,
    onError = Color(0xFFFFFFFF),
    errorContainer = DangerAlert.copy(alpha = 0.2f),
    onErrorContainer = broxNavy,
    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF000000),
    surface = Color(0xFFF8FAFC),
    onSurface = Color(0xFF000000),
    surfaceVariant = Color(0xFFE2E8F0),
    onSurfaceVariant = Color(0xFF475569),
    outline = Color(0xFF94A3B8),
    outlineVariant = Color(0xFFCBD5E1),
    inverseSurface = Color(0xFF1E1E1E),
    inverseOnSurface = Color(0xFFFFFFFF),
    inversePrimary = Color(0xFF93C5FD),
    surfaceContainerHighest = Color(0xFFE2E8F0),
    surfaceContainerHigh = Color(0xFFEBF1F7),
    surfaceContainer = Color(0xFFF1F5F9),
    surfaceContainerLow = Color(0xFFF8FAFC),
    surfaceContainerLowest = Color(0xFFFFFFFF),
)

/** Check if current scheme is OLED flavor (pure black background) */
val ColorScheme.isOledFlavor: Boolean get() = background == OledBlack

/** Extension to create a pure black background variant */
fun ColorScheme.withBlackBackground(): ColorScheme = copy(
    background = OledBlack,
    surface = OledBlack,
    surfaceContainerLowest = OledBlack,
)

// ─── Card Colors & Borders ───

@Composable
fun kaiAdaptiveCardColors(): CardColors = CardDefaults.cardColors(
    containerColor = if (MaterialTheme.colorScheme.isOledFlavor) {
        CardFill
    } else {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    },
)

@Composable
fun kaiAdaptiveCardBorder(): BorderStroke? {
    val scheme = MaterialTheme.colorScheme
    return if (scheme.isOledFlavor || scheme == MidnightColorScheme) {
        BorderStroke(1.dp, BorderSoft)
    } else {
        BorderStroke(1.dp, scheme.outlineVariant)
    }
}

@Composable
fun Modifier.kaiAdaptiveCardSurface(shape: Shape = CardDefaults.shape): Modifier = this
    .clip(shape)
    .background(
        if (MaterialTheme.colorScheme.isOledFlavor) {
            CardFill
        } else {
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        },
        shape,
    )
    .then(
        if (MaterialTheme.colorScheme.isOledFlavor || MaterialTheme.colorScheme == MidnightColorScheme) {
            Modifier.border(1.dp, BorderSoft, shape)
        } else {
            Modifier
        },
    )

/** Glassmorphic card colors for Settings and UI panels */
@Composable
fun glassmorphicCardColors(): CardColors = CardDefaults.cardColors(
    containerColor = CardFill,
)

/** Glassmorphic card border */
@Composable
fun glassmorphicCardBorder(): BorderStroke = BorderStroke(1.dp, BorderSoft)

// ─── Text Field Colors ───

@Composable
fun outlineTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = TextPrimary,
    unfocusedTextColor = TextPrimary,
    disabledTextColor = TextSecondary.copy(alpha = 0.5f),
    errorTextColor = DangerAlert,
    focusedContainerColor = SurfaceRaised.copy(alpha = 0.8f),
    unfocusedContainerColor = SurfaceRaised.copy(alpha = 0.8f),
    disabledContainerColor = SurfaceRaised.copy(alpha = 0.4f),
    errorContainerColor = DangerAlert.copy(alpha = 0.2f),
    cursorColor = AccentPrimary,
    errorCursorColor = DangerAlert,
    focusedBorderColor = AccentPrimary,
    unfocusedBorderColor = BorderSoft,
    disabledBorderColor = BorderSoft.copy(alpha = 0.5f),
    errorBorderColor = DangerAlert,
    focusedLabelColor = AccentPrimary,
    unfocusedLabelColor = TextSecondary,
    disabledLabelColor = TextSecondary.copy(alpha = 0.5f),
    errorLabelColor = DangerAlert,
    focusedPlaceholderColor = TextSecondary,
    unfocusedPlaceholderColor = TextSecondary,
    disabledPlaceholderColor = TextSecondary.copy(alpha = 0.5f),
    errorPlaceholderColor = DangerAlert,
    focusedLeadingIconColor = TextSecondary,
    unfocusedLeadingIconColor = TextSecondary,
    disabledLeadingIconColor = TextSecondary.copy(alpha = 0.5f),
    errorLeadingIconColor = DangerAlert,
    focusedTrailingIconColor = TextSecondary,
    unfocusedTrailingIconColor = TextSecondary,
    disabledTrailingIconColor = TextSecondary.copy(alpha = 0.5f),
    errorTrailingIconColor = DangerAlert,
)

@Composable
fun KaiOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = false,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        label = label,
        placeholder = placeholder,
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        shape = RoundedCornerShape(12.dp),
        colors = outlineTextFieldColors(),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
    )
}

@Composable
fun KaiClearableTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    singleLine: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    var focused by remember { mutableStateOf(false) }
    KaiOutlinedTextField(
        modifier = modifier.fillMaxWidth().onFocusChanged { focused = it.isFocused },
        value = value,
        onValueChange = onValueChange,
        label = label,
        singleLine = singleLine,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        trailingIcon = {
            IconButton(
                onClick = { onValueChange("") },
                modifier = Modifier.handCursor()
                    .alpha(if (focused && value.isNotEmpty()) 1f else 0f),
                enabled = value.isNotEmpty(),
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
    )
}

@Composable
@Preview
fun Theme(
    colorScheme: ColorScheme,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = colorScheme,
        typography = KaiTypography,
        shapes = Shapes(
            extraSmall = KaiShapes.input,
            small = KaiShapes.row,
            medium = KaiShapes.card,
            large = KaiShapes.section,
            extraLarge = KaiShapes.panel,
        ),
    ) {
        content()
    }
}
