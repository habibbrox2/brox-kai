package com.inspiredandroid.kai.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.inspiredandroid.kai.ui.broxNavy

// The Brox Kai "BK" monogram, authored in the same 512x512 space as the launcher
// icon so the animated mark is the exact artwork used everywhere else.
private const val MARK_CENTER_X = 264.5f
private const val MARK_CENTER_Y = 256f
private const val MARK_HEIGHT = 320f

private val MarkCyan = Color(0xFF00D1E8)
private val MarkBlueOnDark = Color(0xFF168BDB)

private val broxMarkB: Path by lazy {
    Path().apply {
        // Outer "B"
        moveTo(104f, 96f)
        lineTo(240f, 96f)
        cubicTo(300f, 96f, 333f, 129f, 333f, 176f)
        cubicTo(333f, 207f, 317f, 230f, 291f, 244f)
        cubicTo(328f, 257f, 349f, 284f, 349f, 321f)
        cubicTo(349f, 377f, 309f, 416f, 246f, 416f)
        lineTo(104f, 416f)
        lineTo(104f, 96f)
        close()
        // Upper counter
        moveTo(176f, 158f)
        lineTo(176f, 222f)
        lineTo(232f, 222f)
        cubicTo(252f, 222f, 264f, 210f, 264f, 190f)
        cubicTo(264f, 170f, 252f, 158f, 232f, 158f)
        lineTo(176f, 158f)
        close()
        // Lower counter
        moveTo(176f, 280f)
        lineTo(176f, 354f)
        lineTo(240f, 354f)
        cubicTo(263f, 354f, 276f, 340f, 276f, 317f)
        cubicTo(276f, 294f, 262f, 280f, 239f, 280f)
        lineTo(176f, 280f)
        close()
    }
}

private val broxMarkK: Path by lazy {
    Path().apply {
        moveTo(354f, 96f)
        lineTo(422f, 96f)
        lineTo(328f, 253f)
        lineTo(425f, 416f)
        lineTo(346f, 416f)
        lineTo(270f, 287f)
        lineTo(354f, 96f)
        close()
    }
}

/**
 * The Brox Kai mark, alive: the two halves of the BK monogram ease apart and back
 * together while the whole mark gently breathes. Colors adapt to the surface it
 * sits on so the mark stays legible on both light and dark themes.
 */
@Composable
fun LogoAnimation(
    modifier: Modifier = Modifier,
    size: Dp = 52.dp,
) {
    val animatable = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        while (true) {
            animatable.animateTo(1f, tween(900, easing = EaseInOut))
            animatable.animateTo(0f, tween(900, easing = EaseInOut))
        }
    }

    val kColor = if (MaterialTheme.colorScheme.background.luminance() > 0.5f) broxNavy else MarkBlueOnDark

    Canvas(modifier = modifier.size(size)) {
        val breath = 1f - 0.04f * animatable.value
        val scale = this.size.height * 0.66f / MARK_HEIGHT * breath
        val centerX = this.size.width / 2f
        val centerY = this.size.height / 2f
        val drift = this.size.width * 0.09f * animatable.value

        fun drawHalf(path: Path, color: Color, offsetX: Float) {
            withTransform({
                translate(centerX + offsetX, centerY)
                translate(-MARK_CENTER_X, -MARK_CENTER_Y)
                scale(scale, scale, Offset.Zero)
            }) {
                drawPath(path, color)
            }
        }

        // B is the left half, K the right half; they part and reunite.
        drawHalf(broxMarkB, MarkCyan, -drift)
        drawHalf(broxMarkK, kColor, drift)
    }
}
