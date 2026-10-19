# Brox Kai v2.0 — Chat UI Upgrade Plan

## Objective

Upgrade the existing Compose Multiplatform chat interface into a clearer, more responsive workspace for conversations, AI streaming, tools, and Kai Build. The visual direction uses Midnight Navy surfaces with restrained Cyan and Electric Blue accents.

The plan preserves the current provider API, persistence layer, and `ChatActions` contract during the first UI migration.

## Current structure

The main UI is in `composeApp/src/commonMain/kotlin/com/inspiredandroid/kai/ui/chat/`:

- `ChatScreen.kt` owns the current chat, interactive mode, sandbox visibility, timeline, composer, banners, and overlays.
- `ChatUiState` exposes immutable conversation, loading, service, file, and notification state.
- `ChatViewModel` remains the owner of business actions and provider interactions.
- `QuestionInput`, `BotMessage`, `UserMessage`, `TopBar`, and `EmptyState` already provide useful component boundaries.
- Screenshot tests cover several chat states across phone, tablet, and desktop targets.

## Target experience

### Chat shell

- Use Midnight Navy as the dark workspace foundation, with Cyan and Electric Blue reserved for primary actions, active states, and AI activity.
- Keep the conversation readable in a centered column of roughly 720–800dp.
- On expanded windows, show a conversation rail, primary chat timeline, and an optional tool or sandbox panel.
- On compact windows, open conversation history and tools in a bottom sheet or full-screen overlay.
- Keep the top bar focused on new conversation, history, model/service selection, workspace controls, and settings.

### Empty conversation state

- Retain the Brox Kai loading mark as the visual anchor.
- Add quick-start cards for Write, Code, Research, and Build.
- Surface recent conversations and commonly used workflows when data is available.
- Make Chat, Build, Design, and Tools easy to enter from one place.
- Explain provider limitations only where they affect an action, such as unavailable interactive mode for an on-device model.

### Conversation timeline

- Render user messages as compact Electric Blue bubbles with attachment previews.
- Render assistant messages in a wider, high-readability content surface with deliberate markdown and code-block spacing.
- Represent thinking, tool execution, fallback, errors, and streaming as distinct status cards rather than ordinary message text.
- Add a streaming cursor or progress row with a clear Cancel action.
- Keep Copy and Retry actions; add Share through a platform-appropriate UI action.
- Reveal message actions on hover for desktop and through long press or an overflow control for compact touch layouts.

### Composer

- Keep a stable bottom composer with multi-line text, attachment tray, slash-command results, and the selected service.
- Clearly distinguish Send from Stop while a response is streaming.
- Provide a visible desktop drop zone while files are dragged over the conversation.
- Ensure the composer remains above the mobile keyboard and does not obscure the latest message.
- Preserve draft text, selected files, and service selection per conversation where appropriate.

## Target Compose structure

```text
ChatScreen
├── ChatShell
│   ├── ChatHeader
│   ├── ConversationRail
│   ├── ChatTimeline
│   │   ├── UserMessageCard
│   │   ├── AssistantMessageCard
│   │   ├── ToolActivityCard
│   │   └── StreamingResponseRow
│   ├── ChatComposer
│   └── ChatOverlays
│       ├── HistorySheet
│       ├── SandboxPanel
│       └── SnackbarHost
```

`ChatScreen` should remain the route that connects `ChatViewModel` to the UI. Shared presentation components should live in `commonMain`; platform-specific sharing and system integration should stay in their platform source sets.

## State model

Keep business state in `ChatUiState` and `ChatActions`. Introduce a focused presentation state holder for UI-only concerns:

- active pane or overlay;
- composer draft and selection;
- scroll-to-latest affordance;
- current message action menu;
- drag-over state;
- local animation state.

Do not move provider calls, message persistence, text-to-speech orchestration, or tool execution into composables.

## Implementation phases

### Phase 1 — Foundations

1. Define shared tokens for page surfaces, elevated surfaces, borders, status colors, spacing, and message shapes.
2. Add reusable components for action buttons, status pills, message action menus, and panel headings.
3. Create previews or screenshot fixtures for dark, light, and OLED themes.

### Phase 2 — Responsive shell

1. Split the regular chat area out of `ChatScreen.kt` into `ChatShell`, `ChatHeader`, `ChatTimeline`, `ChatComposer`, and overlays.
2. Centralize window-width decisions so compact, medium, and expanded layouts use one adaptive policy.
3. Implement the desktop conversation rail and optional sandbox/tool panel.
4. Preserve history sheet behavior on compact screens.

### Phase 3 — Timeline and AI states

1. Refresh user and assistant message cards.
2. Add dedicated visual states for streaming, tool execution, reasoning, fallback, retry, and errors.
3. Add Copy, Retry, Share, and speech actions in a consistent message action row.
4. Ensure long markdown, code, tables, attachments, and dynamic UI blocks remain readable.

### Phase 4 — Composer and entry states

1. Refresh the composer with selected-model context, attachment handling, and clear Send/Stop behavior.
2. Add quick-start cards to the empty state.
3. Improve slash-command discovery and attachment removal affordances.
4. Preserve keyboard shortcuts: Enter sends on desktop, Shift+Enter creates a new line.

### Phase 5 — Accessibility and quality

1. Add content descriptions for all icon-only actions, including Send, Stop, Attach, and Share.
2. Verify a minimum 48dp touch target and visible keyboard focus state.
3. Check contrast in light, dark, and OLED themes.
4. Validate large font scale, mobile keyboard behavior, compact windows, and expanded desktop layouts.
5. Extend screenshot coverage for empty, streaming, error, attachments, tool execution, and expanded-panel states.

## Acceptance criteria

- The same conversation can be used comfortably on phone, tablet, desktop, and web layouts.
- The timeline makes user, assistant, streaming, tool, and error states immediately distinguishable.
- The composer never loses the draft when panels are toggled or the layout adapts.
- Interactive mode, sandbox, Kai Build, text-to-speech, attachments, and existing provider behavior continue to work.
- Existing screenshot tests are retained and new visual states are covered.
- No provider, persistence, or tool business logic is moved into composables.

## Risks and controls

- `ChatScreen.kt` currently coordinates several concerns; split it in small, behavior-preserving steps.
- Share behavior requires platform-specific integration, so introduce it behind a shared UI callback.
- Desktop panel changes can affect compact layouts; test each shell state across the existing screenshot matrix.
- `rememberSaveable` behavior differs across Compose targets; persist only UI state that has a suitable cross-platform strategy.
