# UI Bug Fixes Implementation Plan - broxkai2.0

**Based on:** UI_AUDIT_REPORT.md
**Strategy:** Parallel execution across 3 phases with subagents per focus area

---

## Phase 1: Critical Accessibility (Week 1)
**Goal:** Fix all 22 Critical issues - missing semantics, div-onClick anti-patterns, touch targets, animation guards

### Subagent Group 1A: Missing contentDescription & Semantic Roles
| Task | Files | Estimated |
|------|-------|-----------|
| Add contentDescription to icon-only buttons | QuestionInput.kt, UserMessage.kt, DashboardActionCard.kt, SandboxFileBrowserScreen.kt, KaiUiRenderer.kt, TerminalKeyRow.kt, SettingsScreen.kt | 2h |
| Replace Surface+clickable with Button/IconButton/TabRow | SettingsTabSelector.kt, BuildSessionBar.kt, SandboxTabsContent.kt, TerminalKeyRow.kt, BuildProjectsContent.kt, DashboardScreen.kt | 3h |
| Fix KaiUiRenderer Checkbox/Switch/RadioGroup | KaiUiRenderer.kt (lines 617, 782, 922) | 2h |

### Subagent Group 1B: Touch Targets & Animation Guards
| Task | Files | Estimated |
|------|-------|-----------|
| Increase touch targets to 48dp | TerminalKeyRow.kt, BuildSessionBar.kt, SandboxTabsContent.kt, SettingsScreen.kt, SettingsTabSelector.kt | 2h |
| Add prefers-reduced-motion guards | AnimatedGradientBorder.kt, ToolMessage.kt, QuestionInput.kt, TopBar.kt | 1h |
| Add PasswordVisualTransformation to API keys | ServicesSettings.kt | 30m |

---

## Phase 2: Focus & Forms (Week 2)
**Goal:** Fix all 39 High issues - focus management, form labels, live regions, locale

### Subagent Group 2A: Focus Management
| Task | Files | Estimated |
|------|-------|-----------|
| Fix popup/dialog focus trapping | ServiceSelector.kt, ModelSelectionSheet.kt, KaiDialog.kt | 2h |
| Add FocusRequester auto-focus | QuestionInput.kt, BuildTerminalContent.kt | 1h |
| Keyboard navigation for tab strips | SettingsTabSelector.kt, BuildSessionBar.kt, SandboxTabsContent.kt | 2h |

### Subagent Group 2B: Forms & Live Regions
| Task | Files | Estimated |
|------|-------|-----------|
| Add label semantics to TextFields | KaiUiRenderer.kt, TerminalSheet.kt, BuildProjectsContent.kt, CodeEditorScreen.kt | 2h |
| Add LiveRegion.Polite to async areas | ChatTimeline.kt, TerminalSheet.kt, SettingsDetailPane.kt, KaiUiRenderer.kt | 1.5h |
| Add keyboard options to dynamic inputs | KaiUiRenderer.kt | 1h |

### Subagent Group 2C: Locale
| Task | Files | Estimated |
|------|-------|-----------|
| Locale-aware date formatting | DashboardScreen.kt, HeartbeatSection.kt, AgentSettings.kt | 1.5h |
| Locale-aware relative time | HeartbeatSection.kt | 1h |

---

## Phase 3: Polish (Week 3)
**Goal:** Fix 40 Medium + 38 Low issues - typography, performance, dark mode, content handling

### Subagent Group 3A: Typography & Dark Mode
| Task | Files | Estimated |
|------|-------|-----------|
| Replace hardcoded colors with colorScheme | KaiSnackbar.kt, BuildDesignTokens.kt, BuildStatusColors | 1.5h |
| Add tabular numerals | BuildTerminalContent.kt, KaiUiRenderer.kt (CodeNode), TerminalKeyRow.kt | 1h |
| Curly quotes / non-breaking spaces | String resources, formatted strings | 1h |

### Subagent Group 3B: Performance & Content
| Task | Files | Estimated |
|------|-------|-----------|
| Virtualize SettingsDetailPane tabs | SettingsDetailPane.kt, ServicesSettings.kt | 2h |
| Add tooltips for truncated text | ModelSelectionSheet.kt, ServicesSettings.kt, SkillsSection.kt | 1.5h |
| Horizontal scroll for tables | KaiUiRenderer.kt | 1h |
| Image contentScale for markdown | MarkdownRenderer.kt | 30m |

---

## Parallel Execution Strategy

```
┌─────────────────────────────────────────────────────────────────┐
│                    PARALLEL SUBAGENT POOL                        │
├─────────────────┬─────────────────┬─────────────────┬───────────┤
│  Subagent 1A    │  Subagent 1B    │  Subagent 1C    │ Subagent 1D│
│  Semantics      │  Touch/Anim     │  Focus          │  Forms     │
├─────────────────┼─────────────────┼─────────────────┼───────────┤
│  Subagent 2A    │  Subagent 2B    │  Subagent 2C    │ Subagent 2D│
│  Focus          │  Live Regions   │  Locale         │  Forms     │
├─────────────────┼─────────────────┼─────────────────┼───────────┤
│  Subagent 3A    │  Subagent 3B    │  Subagent 3C    │ Subagent 3D│
│  Typography     │  Performance    │  Dark Mode      │  Content   │
└─────────────────┴─────────────────┴─────────────────┴───────────┘
```

---

## Execution Order (Dependencies)

```
Phase 1A (Semantics) ──────┬──→ Phase 1B (Touch/Anim) ───┐
Phase 1C (Focus) ──────────┤                             ├──→ Phase 2
Phase 1D (Forms) ──────────┘                             │
                                                          │
Phase 2A (Focus) ──────────┬──→ Phase 2B (Live Regions) ─┤
Phase 2C (Locale) ─────────┤                             ├──→ Phase 3
Phase 2D (Forms) ──────────┘                             │
                                                          │
Phase 3A (Typography) ─────┬──→ Phase 3B (Performance) ──┘
Phase 3C (Dark Mode) ──────┤
Phase 3D (Content) ────────┘
```

---

## Quality Gates

Each phase requires:
- [ ] All subagent tasks complete
- [ ] `./gradlew :composeApp:compileKotlinJvm` passes (when Java available)
- [ ] No regression in existing functionality
- [ ] Manual verification of fixed components

---

## Tracking

| Phase | Status | Subagents Done | Notes |
|-------|--------|----------------|-------|
| 1A | ⏳ Pending | 0/3 | |
| 1B | ⏳ Pending | 0/2 | |
| 1C | ⏳ Pending | 0/2 | |
| 1D | ⏳ Pending | 0/2 | |
| 2A | ⏳ Pending | 0/3 | |
| 2B | ⏳ Pending | 0/2 | |
| 2C | ⏳ Pending | 0/1 | |
| 2D | ⏳ Pending | 0/1 | |
| 3A | ⏳ Pending | 0/3 | |
| 3B | ⏳ Pending | 0/3 | |
| 3C | ⏳ Pending | 0/2 | |
| 3D | ⏳ Pending | 0/2 | |

---

## Ready to Start

**First batch:** Launch Subagents 1A, 1B, 1C, 1D in parallel for Phase 1 Critical fixes.