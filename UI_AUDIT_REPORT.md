# UI Design Audit Report - broxkai2.0

**Date:** 2026-10-23
**Auditor:** AI Assistant using Web Interface Guidelines + Compose Multiplatform best practices
**Scope:** All frontend UI screens (Chat, Settings, Build, Sandbox, Dashboard, Editor, Dynamic UI, Markdown, Components)

---

## 📊 Summary

| Area | Files Audited | Critical | High | Medium | Low | Total |
|------|---------------|----------|------|--------|-----|-------|
| **Chat UI** | 30+ | 4 | 8 | 10 | 5 | 27 |
| **Settings UI** | 25+ | 6 | 13 | 15 | 25 | 59 |
| **Build/Sandbox/Dashboard/Other** | 50+ | 12 | 18 | 15 | 8 | 53 |
| **TOTAL** | ~105 | **22** | **39** | **40** | **38** | **139** |

---

## 🔴 Critical Issues (22)

### Accessibility — Missing Semantics & Labels
| File | Line | Issue |
|------|------|-------|
| `QuestionInput.kt` | 163, 188 | Send/Attach buttons: `contentDescription = null` |
| `UserMessage.kt` | 91 | Image attachments: no `contentDescription` |
| `ServiceSelector.kt` | 198 | Popup `focusable = false` blocks keyboard |
| `SmallIconButton.kt` | 54 | 36dp touch target (needs 48dp) |
| `SettingsTabSelector.kt` | 424 | Tab pills: `Surface + clickable` no tab role |
| `ToolsSettings.kt` | 355 | Tool Card clickable, no switch semantics |
| `DashboardActionCard.kt` | 429 | Action icons no `contentDescription` |
| `SandboxFileBrowserScreen.kt` | 384 | File/folder icons no `contentDescription` |
| `KaiUiRenderer.kt` | 531 | Copy buttons no `contentDescription` |
| `TerminalKeyRow.kt` | 108 | Key caps no accessible labels |

### Anti-pattern: div-onClick Equivalents
| File | Line | Should Use |
|------|------|------------|
| `SettingsTabSelector.kt` | 424 | `TabRow` / `Button` |
| `BuildSessionBar.kt` | 216 | `TabRow` / `Button` |
| `SandboxTabsContent.kt` | 245 | `TabRow` / `Button` |
| `TerminalKeyRow.kt` | 244 | `Button` / `IconButton` |
| `DashboardScreen.kt` | 462 | `Button` |
| `BuildProjectsContent.kt` | 169 | `Button` |
| `KaiUiRenderer.kt` | 617 | Native `Checkbox`/`Switch`/`RadioButton` with `onCheckedChange` |

### Touch Targets (< 48dp)
| File | Element | Current |
|------|---------|---------|
| `TerminalKeyRow.kt` | Key caps | 34×40dp |
| `BuildSessionBar.kt` | Back/New/Close | 28–40dp |
| `SandboxTabsContent.kt` | Session/Sub tabs | ~36dp |
| `SettingsScreen.kt` | Back/GitHub/Docs | 24–40dp |
| `SettingsTabSelector.kt` | Tab pills | ~36dp |

### Animation — No `prefers-reduced-motion`
| File | Animation |
|------|-----------|
| `AnimatedGradientBorder.kt` | 35–41 infinite border pulse |
| `ToolMessage.kt` | 114–130 pulsing status |
| `QuestionInput.kt` | 124–146 send button pulse |
| `TopBar.kt` | 215–224 sandbox toggle flash |

---

## 🟠 High Issues (39)

### Focus Management
- `ServiceSelector.kt:198` — Popup not focusable
- `QuestionInput.kt:304` — FocusRequester never auto-focused
- `ModelSelectionSheet.kt:106` — Bottom sheet no focus trap
- `BuildTerminalContent.kt:366` — TextField no visible focus indicator
- `KaiDialog.kt` — No focus trapping in dialogs

### Forms & Inputs
- `ServicesSettings.kt:1159` — API key fields need `PasswordVisualTransformation()`
- `KaiUiRenderer.kt:591` — Dynamic form inputs missing keyboard options, labels
- `TerminalSheet.kt:399` — Terminal input needs `label` semantics

### Live Regions for Async Updates
- `ChatTimeline.kt` — New messages/streaming
- `TerminalSheet.kt` — Streaming output
- `SettingsDetailPane.kt` — Tab content changes
- `KaiUiRenderer.kt` — Install progress, search results

### Locale — Hardcoded English Formats
- `DashboardScreen.kt:69` — English month names
- `HeartbeatSection.kt:754` — "just now", "Xm ago", "Xh ago"
- `AgentSettings.kt:700` — English month abbreviations

---

## 🟡 Medium Issues (40)

### Typography
- Straight quotes instead of curly (`"` vs `“”`)
- No `text-wrap: balance` equivalent
- No tabular numerals (`fontFeatureSettings = "tnum"`) for code/terminal

### Performance
- `SettingsDetailPane.kt` — Single Column, no LazyColumn per tab
- `ServicesSettings.kt` — ReorderableColumn not virtualized
- `KaiUiRenderer.kt` — Deep trees render all children (no virtualization)

### Content Handling
- Truncated text without tooltips (model IDs, URLs, skill descriptions)
- Wide tables in `KaiUiRenderer` overflow on mobile

### Dark Mode
- `KaiSnackbar.kt` — Hardcoded brand colors not from `colorScheme`
- `BuildDesignTokens.kt` — Status colors hardcoded

---

## 🟢 Low Issues (38)
- Generic "Cancel" labels in confirm dialogs
- Missing `overscroll-behavior` in sheets/drawers
- No `contentScale = ContentScale.Crop` on markdown images
- Curly quotes, non-breaking spaces in formatted strings

---

## ✅ What Works Well
- ✅ Virtualized lists everywhere (`LazyColumn` with keys)
- ✅ State hoisting + `rememberSaveable` for config changes
- ✅ Material3 theming with dynamic color + OLED support
- ✅ Most icon buttons have proper `contentDescription` (TopBar, MessageActionRow, etc.)
- ✅ Gesture handling via `pointerInput` / `detectTransformGestures`
- ✅ String resources for all user-facing text
- ✅ `SelectionContainer` on messages for copy
- ✅ `AnimatedVisibility` with transform/opacity only
- ✅ Glassmorphism design system consistently applied
- ✅ Drag-and-drop file attachment with visual feedback

---

## 🎯 Recommended Fix Order

### Phase 1: Critical Accessibility (Week 1)
1. Add `contentDescription` to all icon-only clickables (15+ locations)
2. Replace all `Surface/Box/Row + clickable` with semantic `Button`/`IconButton`/`TabRow`
3. Fix `KaiUiRenderer` Checkbox/Switch/RadioGroup to use native `onCheckedChange`
4. Increase all touch targets to ≥ 48dp
5. Add `prefers-reduced-motion` guards to infinite animations

### Phase 2: Focus & Forms (Week 2)
6. Fix focus order/trapping in popups, sheets, dialogs
7. Add `label` semantics to all text fields
8. Add live regions (`LiveRegion.Polite`) to async content areas

### Phase 3: Polish (Week 3)
9. Locale-aware date/number formatting
10. Keyboard navigation for all custom tab strips
11. Replace hardcoded colors with `colorScheme` mappings
12. Tabular numerals in code/terminal
13. Tooltips for truncated text