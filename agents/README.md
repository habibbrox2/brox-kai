# Brox Kai v2.0 — Project Agent Catalog

These are project-specific subagent definitions. Canonical, Git-trackable copies live in `agents/`; matching local Claude Code copies live in `.claude/agents/` because this checkout's `.gitignore` intentionally ignores `.claude/`.

## Agents and when to use them

| Agent | Use for | Default posture |
|---|---|---|
| `kmp-compatibility-auditor` | Source-set leaks, JVM-only APIs, expect/actual drift | Read-only audit |
| `wasm-build-specialist` | Browser Wasm compiler errors and distribution failures | Diagnose first; edit only when explicitly asked |
| `compose-ui-repair-specialist` | Compose syntax/import/state/focus/API mismatch | Diagnose first; edit only when explicitly asked |
| `security-reviewer` | MCP SSRF, shell execution, Android network policy, storage and release integrity | Read-only review |
| `build-test-verifier` | Gradle task discovery and Android/Desktop/Wasm validation | Verification only; no implementation |
| `resources-localization-specialist` | Compose Resources, missing strings, locale consistency | Diagnose first; edit only when explicitly asked |
| `feature-docs-curator` | Feature specs, knowledge bundles, agent documentation | Documentation-only when authorized |

## Recommended routing
- **Wasm build failure:** `kmp-compatibility-auditor` → `wasm-build-specialist` → `build-test-verifier`.
- **Compose UI error:** `compose-ui-repair-specialist` → `build-test-verifier`.
- **Security-sensitive change:** `security-reviewer` before and after implementation.
- **Missing string/resource:** `resources-localization-specialist` → `build-test-verifier`.
- **Feature behavior change:** inspect feature docs first; use `feature-docs-curator` to review authorized doc updates.

## Shared rules
All agents must follow root `AGENTS.md` and read the relevant project skills under `skills/`. No agent may overwrite unrelated user changes, silently change dependency versions, weaken security, or commit/push/release/deploy without explicit authorization. Read-only diagnosis is the default unless the task clearly authorizes implementation.
