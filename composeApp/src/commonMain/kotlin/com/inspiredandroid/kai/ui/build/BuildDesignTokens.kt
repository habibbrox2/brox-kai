package com.inspiredandroid.kai.ui.build

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.inspiredandroid.kai.build.BuildEnvironmentState
import com.inspiredandroid.kai.build.BuildStep

/**
 * Build-specific design tokens for the Kai Build workspace (Plan 3, Phase 1).
 *
 * Terminal-centric colors that work with the Brox Kai palette and stay legible on
 * both the dark terminal surface and the app's adaptive backgrounds.
 */
object BuildSpacing {
    val xxs: Dp = 2.dp
    val xs: Dp = 4.dp
    val sm: Dp = 8.dp
    val md: Dp = 12.dp
    val lg: Dp = 16.dp
    val xl: Dp = 24.dp
    val xxl: Dp = 32.dp

    val minTouchTarget: Dp = 48.dp
}

object BuildShapes {
    val card = RoundedCornerShape(16.dp)
    val pane = RoundedCornerShape(12.dp)
    val pill = RoundedCornerShape(999.dp)
    val terminal = RoundedCornerShape(10.dp)
}

/** Status semantics shared by the workspace header, session tabs, and setup flow. */
@Immutable
enum class BuildStatusKind {
    /** Environment/session is ready and idle. */
    Idle,

    /** Something is actively running (install, build, command). */
    Running,

    /** Completed successfully. */
    Success,

    /** Needs attention but did not fail outright (missing agent, unsaved work). */
    Warning,

    /** A step or command failed. */
    Failed,
}

/**
 * Resolves the environment state to a status kind so headers, pills, and setup
 * blocks all agree on what "installing", "ready", and "not installed" look like.
 */
fun BuildEnvironmentState.toStatusKind(): BuildStatusKind = when (this) {
    BuildEnvironmentState.NotInstalled -> BuildStatusKind.Idle
    is BuildEnvironmentState.Installing -> BuildStatusKind.Running
    BuildEnvironmentState.Ready -> BuildStatusKind.Success
}

/** Color ramp for the four terminal/workflow statuses plus neutral idle. */
object BuildStatusColors {
    val idle: Color = Color(0xFF8899A6)
    val running: Color = Color(0xFF00BCD4)
    val success: Color = Color(0xFF16C60C)
    val warning: Color = Color(0xFFFFB74D)
    val failed: Color = Color(0xFFE74856)

    fun forKind(kind: BuildStatusKind): Color = when (kind) {
        BuildStatusKind.Idle -> idle
        BuildStatusKind.Running -> running
        BuildStatusKind.Success -> success
        BuildStatusKind.Warning -> warning
        BuildStatusKind.Failed -> failed
    }
}

/** Fixed surfaces used by the workspace, kept here so the palette stays in one place. */
object BuildSurfaces {
    /** The terminal canvas — always dark for readable ANSI colors. */
    val terminal: Color = Color(0xFF0C0C0C)
    val terminalRaised: Color = Color(0xFF1A1A1A)
    val terminalBorder: Color = Color(0xFF262626)

    /** Progress bar fill for the current install step. */
    val progressTrack: Color = Color(0xFF1F3355)
}

/**
 * Human label for an install step, used by the setup surface and progress blocks.
 */
fun BuildStep.label(): String = when (this) {
    BuildStep.Download -> "Download"
    BuildStep.Extract -> "Extract"
    BuildStep.Configure -> "Configure"
    BuildStep.BasePackages -> "Base packages"
    BuildStep.Agent -> "Agent"
}
