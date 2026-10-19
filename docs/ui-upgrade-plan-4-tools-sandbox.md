# Brox Kai v2.0 — UI Upgrade Plan 4: Tools and Sandbox Workspace

## Objective

Create a coherent operational workspace for tools, MCP servers, skills, files, packages, and terminal sessions. It should make power features approachable without hiding their status or risks.

## Current scope

- `ToolsContent` combines MCP servers, Skills, and native tools in one long settings section.
- `SandboxTabsContent` supports setup, terminal sessions, files, and packages.
- Sandbox state is already supplied through `SandboxUiState`; session and tab state are managed by their view models where available.

The redesign should keep tool configuration separate from sandbox operation, while linking them through obvious status and setup actions.

## Target experience

- Treat Tools as a management page: users connect, enable, refresh, and review capabilities.
- Treat Sandbox as an operational workspace: users run commands, browse files, manage packages, and inspect active sessions.
- Provide an at-a-glance capability summary: enabled tools, connected MCP servers, installed skills, sandbox readiness, and active sessions.
- Clearly separate safe routine actions from destructive or environment-changing actions.
- Use status badges for Connected, Disabled, Installing, Needs setup, Error, and Active.

## Information architecture

```text
Tools and workspace
├── Tools overview
├── MCP servers
│   ├── Connected servers
│   ├── Popular servers
│   └── Add server
├── Skills
│   ├── Installed skills
│   └── Discover and install
├── Native tools
└── Sandbox workspace
    ├── Overview and setup
    ├── Terminal sessions
    ├── Files
    └── Packages
```

## Target structure

```text
ToolsScreen
├── ToolsOverview
├── McpServerManager
├── SkillsManager
└── NativeToolsGrid

SandboxWorkspace
├── SandboxHeader
├── SandboxReadinessCard
├── SandboxNavigation
├── SessionBar
└── SandboxContent
    ├── TerminalPane
    ├── FileBrowserPane
    └── PackageManagerPane
```

## State ownership

- Preserve `SettingsUiState`/`SettingsActions` for tool, MCP, and skill mutations.
- Preserve `SandboxUiState`, `SandboxSessionViewModel`, file-browser state, and packages state for operational sandbox information.
- Keep UI-only state local: filters, expanded details, selected compact subtab, and confirmation dialog visibility.
- Do not let cards query controllers or mutate servers directly; all actions flow through existing state holders.

## Implementation phases

### Phase 1 — Shared operational components

1. Create status badges, connection cards, capability count cards, row overflow actions, and confirmation patterns.
2. Standardize working, failed, disabled, and installed states across MCP, skills, tools, and sandbox setup.
3. Add a dedicated empty state for each collection instead of a generic blank list.

### Phase 2 — Tools management page

1. Add a Tools Overview header that summarizes enabled native tools, connected MCP servers, and installed skills.
2. Redesign MCP servers as cards with connection status, refresh time/status, enable switch, detail view, and remove action.
3. Present Popular Servers as a separate discovery area, not mixed with active connections.
4. Redesign Skills as installed and discoverable groups with installation progress and actionable error states.
5. Keep Native Tools as a responsive card grid, but add grouping and search/filter when the collection is large.

### Phase 3 — Sandbox setup

1. Turn the current setup card into a readiness flow with environment explanation, storage/session expectations, selected distribution, and install progress.
2. Make cancellation, retry, reset, and package installation status clear without burying error text.
3. Link skills that require a sandbox to the setup flow using a clear prerequisite state.

### Phase 4 — Sandbox workspace

1. Give Terminal, Files, and Packages a common workspace header and shared navigation.
2. On desktop, show session selection above or beside the terminal and keep Files/Packages available as supporting panes.
3. On compact layouts, retain the tab model with larger targets and selected-tab visibility.
4. Present terminal sessions as named, stateful tabs with active/running status and close action.
5. Keep terminal, file, and package controls independently scrollable and keyboard accessible.

### Phase 5 — Accessibility, safety, and validation

1. Give every stateful control an accessible name and state description.
2. Require an explicit confirmation UI before reset, removal, or other irreversible environment actions.
3. Validate touch targets, focus flow, large fonts, terminal contrast, and keyboard navigation.
4. Add screenshot cases for disconnected, connected, installing, error, sandbox setup, multiple sessions, files, and packages.

## Acceptance criteria

- Users can see the difference between a tool that is available, enabled, connected, installing, or failed.
- MCP, skills, and native tools are manageable without entering the sandbox workspace.
- Sandbox setup and operation are understandable at a glance on compact and desktop screens.
- Existing controller/view-model ownership stays intact.
- Destructive actions have explicit confirmation and existing undo behavior remains where supported.

## Risks and controls

- MCP and skill installation may be asynchronous. Every mutation must render a durable in-progress or error state from the view model.
- Sandbox availability is platform-dependent. Keep capability flags in state and avoid platform checks inside shared cards.
- Terminal performance and focus can regress if nested scroll or gesture handlers are added carelessly; keep the terminal pane isolated from surrounding scroll containers.
