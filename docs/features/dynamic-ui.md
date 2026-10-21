# Dynamic UI (kai-ui)

**Last verified:** 2026-10-02

AI-generated interactive UI layouts rendered inline in chat messages. The AI produces JSON-based layout definitions wrapped in `kai-ui` code fences. Compose renders them natively with support for forms, buttons, and multi-step flows. Enabled by default; users can disable it in Settings, which removes the instructions from the system prompt. Because the system prompt is rebuilt per request, toggling the setting takes effect on the next message in any conversation. Parsing and rendering stay active regardless so existing messages with kai-ui blocks always render.

## Concepts

### Layout Blocks

A `kai-ui` code fence inside an assistant message contains a JSON object describing a component tree. The unified markdown parser treats kai-ui fences as first-class AST blocks, alongside headings, paragraphs, and other markdown; the renderer dispatches each block to its composable. Supports both single-object JSON and multi-line NDJSON (one JSON object per line, automatically wrapped in a column).

### Component Types

- **Layout**: column, row, card, box, divider (spacing between children is fixed by the renderer — the LLM does not control it)
- **Content**: text (with headline/title/body/caption styles), image (optional aspect ratio to prevent distortion on wide screens), icon (curated material icon set or any emoji), code (syntax-highlighted block with a built-in copy-to-clipboard icon in the top-right corner)
- **Interactive**: button (filled/outlined/text/tonal variants), text input, checkbox, switch, select dropdown, radio group, slider, chip group (single-select, multi-select, or display-only tags)
- **Feedback**: progress (determinate/indeterminate), countdown (relative duration with optional expiry action; a callback action fires only while the message is still interactive, so older countdowns never send messages when the chat is reopened or scrolled), alert (info/success/warning/error)
- **Navigation**: tabs (tabbed content), accordion (collapsible sections)
- **Display**: quote (blockquote with accent border), badge (colored count/status pill), stat (large metric display), avatar (circular image or initials)
- **Data**: list, table

### Actions

Buttons carry an action that fires on click (chip groups are form inputs, not action carriers):

- **callback** — collects form data from specified input IDs and sends a structured message back to the AI via the normal chat flow (e.g. "Pressed: event_name" or "Responded with: key: value"). The AI then responds with text or more UI. The prompt explicitly tells the AI to only offer callback buttons for actions it can fulfill — callbacks do not trigger system operations like printing, file export, or downloads. Clipboard access is available via the dedicated copy action below.
- **toggle** — shows/hides a target element locally without AI roundtrip
- **open_url** — opens a link in the browser
- **copy_to_clipboard** — writes a literal string to the system clipboard locally, no AI roundtrip. A button carrying this action always renders as a compact clipboard icon button regardless of its variant; the button's label is ignored. Intended for surfacing copyable values (snippets, commands, tokens) alongside the content they belong to.

### Interaction Flow

When a callback fires, the renderer collects input values and formats them as a user message of the form `Pressed: <event>` or `Responded with: <key: value>` (no special prefix). The AI receives the event name and form data in conversation history and can respond with new UI, text, or tool calls. While the callback is in flight, the clicked button keeps its label visible and pulses (a subtle scale and alpha animation); other buttons in the same assistant message become non-interactive until the response arrives.

The originating assistant's kai-ui card stays in place across the exchange. While the submission is in flight the pressed button pulses; once the reply arrives the card settles into a frozen snapshot (prior values seeded, pressed button highlighted) with an edit pencil in the top-right corner. The user's submission does not produce a separate text bubble — the filled-in form on the assistant card already shows exactly what was sent. Clicking the pencil re-enables the form seeded with the previous picks; pressing any button then triggers a resubmit that truncates from that point and re-asks the AI. The text form ("Responded with: key: value") is still what the AI receives; only the visual representation differs. Snapshots persist with the conversation, so reloading preserves them.

### Layout Lifecycle

Only the latest assistant message's layouts are interactive. Older layouts become read-only with disabled buttons and inputs. Form state is local to each layout; cross-step state lives in conversation history.

### Error Handling

Errors are absorbed as locally as possible so a single bad field never kills a whole block. The parser first repairs common JSON syntax mistakes (extra trailing braces/brackets via stack-based brace matching, truncated mid-stream responses, `"key="` typos, and missing closing braces between sibling objects in an array — e.g. when the LLM writes `,{` where a key was expected), then walks the resulting tree field-by-field. Each field reader tolerates the wrong value type (objects where a string was expected, bare strings where an object list was expected, string booleans, numeric strings) and falls back to the data-class default if nothing can be salvaged — the node still builds, the offending field is simply missing from the rendered UI. Unknown node types in `children` or `items` are silently dropped. Only JSON so malformed that the syntax repair can't produce a parseable tree falls back to rendering as a code block. Individual malformed lines in multi-line NDJSON are skipped while valid lines still render. TTS walks the kai-ui tree and reads out the human-readable labels it finds — text, button labels, alert messages, and similar — while skipping non-speakable elements like code blocks, icons, and dividers.

### Settings

The feature is controlled by the Dynamic UI toggle in Settings (General tab). When disabled, the system prompt omits kai-ui instructions so the AI stops generating them. Parsing and rendering remain active unconditionally.

## Interactive UI Mode

A mode of the **same chat**, not a separate screen. When it is on, the AI answers with a complete screen layout built from kai-ui, and that screen renders inline in the conversation — the user keeps the chat timeline, the composer and the rest of the app, and navigates the screens by clicking the buttons inside them.

### Turning Interactive Mode On

The chat header carries an interactive-UI toggle (a widgets icon, tinted while the mode is on), and the sidebar has an "Interactive UI" entry that shows the same state. Toggling it changes how the **next** answer is produced and rendered; the conversation itself is untouched — messages, history and the composer all stay where they were, and the mode can be switched off again at any time. The toggle is hidden when the primary service is on-device (LiteRT), because small local models cannot reliably produce kai-ui JSON; on-device services are also filtered out of the service picker while the mode is on.

### Screen Navigation

Each interactive answer is a self-contained screen inside the timeline. The kai-ui blocks of the latest assistant message stay interactive — buttons, forms and other callbacks work exactly as they do in the full-screen mode used before, and pressing one submits the collected values back to the AI.

### Back Action

While interactive mode is on, the chat header offers a back action next to the toggle. It removes the last exchange (user message + assistant response) from the conversation history, so the previous answer becomes the active screen again. When only one exchange remains, the history is cleared and the user is back at the composer — interactive mode stays on.

### Auto-Retry on Parse Failure

If the AI responds without valid kai-ui blocks, the system automatically retries up to 2 times, sending the parse error details back to the AI so it can fix its JSON.

### Conversation Persistence

Interactive sessions are saved with type `interactive`, and loading an interactive conversation from history turns the mode back on. Switching the mode in an open conversation updates that conversation's type, so the mode a chat was left in is the mode it reopens in.

### System Prompt

In interactive mode, the system prompt asks the AI for a single kai-ui code fence that renders as a screen inside the chat, and tells it that text outside the fence is shown as ordinary chat text — so the prose stays minimal and the screen carries the content.

## Key Files

| File | Purpose |
|------|---------|
| `composeApp/.../ui/dynamicui/KaiUiNode.kt` | Serializable component tree model — 28 node types, all @Immutable |
| `composeApp/.../ui/dynamicui/UiAction.kt` | Action types (callback, toggle, open_url, copy_to_clipboard) |
| `composeApp/.../ui/dynamicui/KaiUiParser.kt` | Sanitizes malformed JSON and decodes kai-ui fence bodies via `parseUiBlockBody` |
| `composeApp/.../ui/dynamicui/KaiUiNodeBuilders.kt` | Tolerant field-by-field construction of KaiUiNode instances from JsonElement |
| `composeApp/.../ui/dynamicui/KaiUiRenderer.kt` | Recursive Compose renderer for the component tree, wrapInCard option |
| `composeApp/.../ui/dynamicui/KaiUiTts.kt` | Speakable-text extraction for TTS: walks kai-ui trees for human-readable labels |
| `composeApp/.../ui/markdown/MarkdownParser.kt` | Unified markdown parser; emits `KaiUiBlock` AST nodes for kai-ui fences |
| `composeApp/.../ui/markdown/MarkdownRenderer.kt` | Compose renderer that dispatches each block (including kai-ui) to its composable |
| `composeApp/.../ui/chat/composables/BotMessage.kt` | Integration point — runs the parser, renders frozen/edit-enabled snapshots for paired user submissions, and renders past kai-ui read-only via `isInteractive = false` |
| `composeApp/.../ui/chat/ChatScreen.kt` | Branches between chat mode and interactive mode |
| `composeApp/.../ui/chat/composables/TopBar.kt` | Chat header: the interactive-UI toggle and the back action |
| `composeApp/.../ui/chat/ChatActions.kt` | submitUiCallback, setInteractiveMode, goBackInteractiveMode |
| `composeApp/.../ui/chat/ChatViewModel.kt` | Interactive mode lifecycle, auto-retry on parse failure |
| `composeApp/.../ui/chat/ChatUiState.kt` | isInteractiveMode state flag |
| `composeApp/.../ui/chat/composables/ChatTimeline.kt` | Renders the kai-ui blocks inline, with the callbacks wired |
| `composeApp/.../data/DataRepository.kt` | popLastExchange, setInteractiveMode/isInteractiveModeActive |
| `composeApp/.../data/RemoteDataRepository.kt` | Interactive mode system prompt, TYPE_INTERACTIVE conversation saving |
| `composeApp/.../data/AppSettings.kt` | isDynamicUiEnabled / setDynamicUiEnabled |
| `composeApp/.../data/Conversation.kt` | TYPE_INTERACTIVE conversation type constant |
