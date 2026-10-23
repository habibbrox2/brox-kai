# AI Agent Guide Index — Brox Kai v2.0

These guides were written after inspecting the current checkout at `G:\Web\broxkai2.0`. They describe this repository, not a generic Compose template.

## Read in this order
1. [Repository architecture](architecture-guide.md)
2. [Source sets and platform APIs](source-set-guide.md)
3. [Build and test workflow](build-and-test-guide.md)
4. [Compose API repair](compose-api-migration.md)
5. [Resources and localization](resources-and-localization.md)
6. [Security and regression checklist](security-guide.md)

## Skills
Portable reference copies live in `skills/<skill-name>/SKILL.md`. Matching Claude Code-discoverable copies are in `.claude/skills/<skill-name>/SKILL.md`.
- `kmp-compatibility-audit`
- `compose-ui-repair`
- `gradle-build-diagnostics`
- `compose-resources`
- `security-regression`
- `cross-platform-validation`

## Repository-specific notes
- Gradle modules: `:composeApp`, `:androidApp`, `:screenshotTests`.
- `composeApp` configures Android, iOS arm64/simulator arm64, JVM desktop and browser Wasm.
- `commonMain` includes Compose, Navigation, Lifecycle, Ktor, Koin, FileKit, Coil, multiplatform settings, kotlinx-datetime, and SQLDelight runtime.
- Shared expect/actual platform contracts are in `composeApp/src/commonMain/kotlin/com/inspiredandroid/kai/Platform.kt` and platform files.
- The version catalog is the source of truth for plugin/library versions: `gradle/libs.versions.toml`.
- Root project name and package namespace still contain upstream `Kai`; do not rename them just to match branding.
- `CLAUDE.md` remains authoritative for feature specs and `docs/knowledge/` bundles.
