# UI Upgrade Plan 7 — Premium Chat UI + Code Editor (ChatGPT / Gemini / Grok class, 2026)

## Goal

Migrate and redesign the existing chat application into an international-standard,
premium, professional AI assistant interface — a 2026-era uncommon design — while
keeping every behavior (providers, tools, sandbox, heartbeat, settings) intact.

Brand rules:

- **"Brox Kai 2.0" is the fixed brand name**; only the version (2.0.x) changes.
- Android always opens directly into the **Chat UI** (chat-first, like ChatGPT/Gemini/Grok).
- The Dashboard survives but lives **inside the sidebar** (brand/logo link), not as a tab.

## Decisions (confirmed with owner)

| Question | Decision |
| --- | --- |
| Settings tabs | Restructure to the new **4 tabs** (General, Model & API Key, Data Control, Account & Plan); existing Agent/Tools/Sandbox/Integrations features fold in as sub-sections — nothing is lost |
| Dashboard | Survives as a **sidebar link** (brand click); Chat is the only home |
| "Start Interactive UI" / "Open Kai Build" buttons | Removed **completely** from the empty state; Interactive UI entry stays in the **sidebar** |
| Code editor | **Unified chat-first editor**: full-screen workspace opened from chat responses on mobile, split-pane beside chat on desktop; file tree + syntax-highlighted editing + terminal tab (extends the AI coding agent identity of the app) |

## Current-state audit (what exists today)

- `App.kt`: `NavHost(startDestination = Dashboard)` with a 3-tab segmented bar
  (Dashboard/Chat/Settings) centered in the chat `TopBar` on non-mobile platforms.
- `ChatShell.kt`: adaptive shell — expanded windows get a 260dp conversation rail
  + main content + 320dp side panel; compact windows are full-width with history
  as a `ModalBottomSheet`.
- `TopBar.kt`: leading icon buttons (history, new chat, sandbox), center tab bar,
  right speech toggle + Home + Settings — described by the brief as "cluttered
  legacy icons".
- `EmptyState.kt`: large animated logo, welcome text, quick-start cards
  (Write/Code/Research/Build as icon+label chips), **Start Interactive UI** and
  **Open Kai Build** buttons, privacy note.
- `ChatHistorySheet.kt`: flat list, date per item, delete-only action, no search,
  no grouping, no rename, no pin.
- `ConversationRail.kt`: desktop flat list ("Conversations" + add), no grouping,
  no actions.
- `QuestionInput.kt`: fixed composer with attach (+), skill autocomplete,
  service selector, send (↑)/stop — no voice input, send already highlights.
- Errors: centered `ErrorMessage` blocks in the conversation / interactive view.
- `MessageActionRow.kt`: Copy / Retry / Share / Speak already exist under
  assistant messages — extend with thumbs up/down.
- `CodeFenceBlock.kt`: syntax highlighting + language label + copy button exist —
  polish to "Copy Code" style and add "Open in editor".
- Settings: 6 tabs (`General, Agent, Services, Tools, Sandbox, Integrations`)
  with rail (expanded) / strip (compact) navigation and `SettingsDetailPane`.
- Data: `Conversation(id, title, updatedAt, …)` — **no pin field, no rename
  action** yet; `ConversationStorage` + `RemoteDataRepository` own persistence.

## Phase plan

### Phase 1 — Chat-first shell: default route + sidebar navigation

- [ ] `App.kt`: `startDestination = Home` (Chat) on every platform; Dashboard
      becomes an ordinary pushed destination.
- [ ] Remove the 3-tab segmented navigation bar (dashboard/chat/settings) from
      `App.kt`/`TopBar` — the sidebar replaces it; `navigationTabBar` params pass
      `null`.
- [ ] New `ui/sidebar/AppSidebar.kt` (presentation-only, reused by drawer + rail):
      - **Top**: New Chat button (prominent, ➕) + search field (`KaiSearchField`).
      - **Middle**: conversation list (Phase 4 adds grouping/search/rename/pin).
      - **Bottom**: brand/profile row (owner identity, local avatar), Dashboard
        link, Interactive UI entry (Android), Settings gear.
- [ ] `ChatModeScreen`/`ChatShell`: on expanded windows the sidebar is the
      permanent left rail (replacing `ConversationRail`); on compact windows it
      is a `ModalNavigationDrawer` opened by a hamburger toggle in the `TopBar`.
- [ ] `TopBar` redesign: **left** = drawer toggle (compact) / brand (expanded),
      **center** = model selector pill (service icon + name + chevron, opens the
      existing selector popup), **right** = quick actions (speech toggle,
      Settings gear); legacy icon clutter removed.
- [ ] Sidebar navigation callbacks wired in `App.kt` (Dashboard, Settings,
      Interactive mode, Kai Build on Android).
- [ ] Compile: desktop + wasm green.

### Phase 2 — Empty state redesign (welcome screen)

- [ ] Remove `Start Interactive UI` and `Open Kai Build` buttons from
      `EmptyState` entirely.
- [ ] Minimal small logo (reduced `LogoAnimation`) + light heading
      "How can I help you today?".
- [ ] Quick cards Write / Code / Research / Build as a **2×2 grid**, each with
      title + subtext ("Code — Write & debug code"), subtle border and
      hover/touch glow accent (`animatedGradientBorder`).
- [ ] Privacy note kept, compact.
- [ ] Compile: desktop + wasm green.

### Phase 3 — Conversation view: bubbles + response action bar

- [ ] User message: right-aligned bubble with light background.
- [ ] Assistant response: full-width container, custom typography, scroll.
- [ ] Response action bar under every assistant message: **Copy, Thumbs
      Up/Down, Regenerate, Read Aloud** (extends `MessageActionRow`; feedback is
      local-only UI state — no backend).
- [ ] Code blocks: keep syntax highlighting; header shows language tag +
      "Copy Code" button + **Open in editor** (Phase 7 hook).
- [ ] Compile: desktop + wasm green.

### Phase 4 — Chat history management

- [ ] Time-based grouping: **Today / Yesterday / Previous 7 Days / Previous
      30 Days** (shared by sidebar list and history sheet).
- [ ] Item actions on hover (desktop) / long-press (mobile): **Rename** (title
      edit dialog → new `renameConversation` action), **Pin** (new
      `isPinned`/`pinnedAt` fields on `Conversation`, pinned group on top),
      **Delete** (existing undo flow).
- [ ] Inline **search bar** above the history list filtering titles live.
- [ ] Data layer: `Conversation` serialization fields (defaulted → backward
      compatible), `ConversationStorage`, repository + viewmodel actions.
- [ ] Compile: desktop + wasm green.

### Phase 5 — Settings restructure to 4 tabs

- [ ] `SettingsTab` → `General, Model, Data, Account` (serializable; dashboard
      deep links remapped).
- [ ] **General**: theme, language, auto-scroll + folded sub-sections for the
      existing Agent / Tools / Sandbox / Integrations content (section headers,
      nothing removed).
- [ ] **Model & API Key**: existing Services content (keys, models, temperature,
      token limits, custom endpoints).
- [ ] **Data Control**: chat history save on/off, Export Chat Data, Clear All
      Conversations.
- [ ] **Account & Plan**: owner profile card (reuse `SupportProfile`),
      system status / app version / plan status.
- [ ] Rail + strip labels, detail pane routing, dashboard deep links updated.
- [ ] Compile: desktop + wasm green.

### Phase 6 — Input bar + error toasts

- [ ] Composer: fixed bottom, attach (+) left, send (↑) right with active
      highlight while typing (verify + polish), stop state preserved.
- [ ] Voice **input** (speech-to-text): only if a cross-platform STT path
      already exists; otherwise the mic slot is omitted (no fake buttons) and
      the existing speech-output toggle stays in the header.
- [ ] Errors: replace centered `ErrorMessage` blocks with a **top-right toast /
      alert badge** overlay (auto-dismiss, retry action) above the input bar;
      snackbar host kept for transient messages.
- [ ] Compile: desktop + wasm green.

### Phase 7 — Code editor workspace (unified, chat-first)

- [ ] New `editor` package: `CodeEditorScreen` + workspace state (in-memory
      file store seeded from AI code blocks via "Open in editor", plus sandbox
      project files where available).
- [ ] Layout: file tree (left), tabbed editor (center, monospace editable with
      highlight preview), terminal tab (bottom/right — Android sandbox shell /
      desktop shell where available).
- [ ] Opening behavior: mobile = full-screen route with back-to-chat; desktop
      expanded = split pane beside the chat (editor as the `ChatShell`
      side-panel slot), compact desktop = full-screen like mobile.
- [ ] Save/reload, dirty indicators, copy-all; behavior stays local (no
      filesystem writes outside the sandbox/workspace).
- [ ] Compile: desktop + wasm green.

### Phase 8 — Docs, changelog, verification

- [ ] Progress status section below all checked.
- [ ] CHANGELOG entry for Plan 7.
- [ ] Final desktop + wasm compile; commit per phase; push.

## Progress status

### Phase 1 — Chat-first shell
### Phase 2 — Empty state
### Phase 3 — Conversation view
### Phase 4 — History management
### Phase 5 — Settings 4 tabs
### Phase 6 — Input & toasts
### Phase 7 — Code editor
### Phase 8 — Docs
