# Brox Kai v2.0

<img src="https://img.shields.io/badge/Platform-Web-f7df1c?logo=javascript" alt="Web"> <img src="https://img.shields.io/badge/Platform-Android-34a853.svg?logo=android" alt="Android" /> <img src="https://img.shields.io/badge/Platform-iOS-lightgrey.svg?logo=apple" alt="iOS" /> <img src="https://img.shields.io/badge/Platform-Windows/macOS/Linux-e10707.svg?logo=openjdk" alt="Platform JVM" />
<div align="center">

<br>
<img src="site/img/logo_animation.svg" height="80">
<br>
<br>

**Brox Kai v2.0** — A security-hardened AI assistant with persistent memory.

Runs on **Android, iOS, Windows, Mac, Linux, and Web**.

**[Website](https://kai9000.com)** - **[Documentation](https://kai9000.com/docs/)** - **[Source](https://github.com/habibbrox2/brox-kai)** - **[Releases](https://github.com/habibbrox2/brox-kai/releases)**

</div>

## Brox Kai v2.0 — What's Different

Brox Kai v2.0 is a security-hardened fork of [Kai 9000](https://github.com/SimonSchubert/Kai) with the following enhancements:

### Security Hardening
- **MCP SSRF Protection** — URL validation (scheme, IP-range, credentials, encoding), connection-time re-validation, redirect validation (max 3 hops), header sanitization
- **Shell Command Hardening** — Fail-closed allowlist, no shell interpretation (direct ProcessBuilder), metacharacter/path traversal/dangerous flag rejection
- **Android Network Security** — HTTPS-only by default, cleartext restricted to localhost/127.0.0.1/10.0.2.2
- **Android Backup Disabled** — `allowBackup=false`, encrypted prefs and conversation DB excluded from backup rules
- **Release Integrity** — SHA-256 checksums for all release artifacts

### Branding
- Brox Kai brand colors (Navy + Cyan) applied throughout the UI
- Brox Kai logo and visual identity
- Editable logo assets and the v2.0 brand board are in [`docs/branding/brox-kai-v2-svg`](docs/branding/brox-kai-v2-svg/README.md)

### UI Polish
- Chat screen improvements (message bubbles, empty state, input area)
- Settings screen consistency
- Accessibility fixes (content descriptions, touch targets)
- Cross-platform responsive design plan

## Installation

### Brox Kai v2.0 Release
Download the latest release from [GitHub Releases](https://github.com/habibbrox2/brox-kai/releases/tag/v2.0.0):
- **Android APK (Foss):** `Brox-Kai-2.0-Android.apk`
- **Windows (x64):** `Brox-Kai-2.0-Windows-x64.msi` / `Brox-Kai-2.0-Windows-x64.zip`

## Features

- **Persistent memory** — Kai remembers important details across conversations and uses them automatically
- **Customizable soul** — Define the AI's personality and behavior with an editable system prompt
- **Multi-service fallback** — 30 LLM providers with automatic failover
- **On-device inference** — Run AI models locally on Android using LiteRT, no internet needed
- **Tool execution** — Web search, notifications, calendar events, shell commands, and more
- **MCP server support** — Connect to remote tool servers via the Model Context Protocol (with SSRF protection)
- **Autonomous heartbeat** — Periodic self-checks that surface anything needing attention
- **Settings export/import** — Backup and restore all settings as a JSON file
- **Encrypted storage** — Conversations stored locally with encryption
- **Security hardened** — SSRF protection, fail-closed shell execution, Android backup disabled, HTTPS-only
- **Text to speech** — Listen to AI responses
- **Linux Sandbox** — On Android, the AI can run shell commands, scripts, and tools in a secure sandboxed Linux environment
- **Image attachments** — Attach images to any conversation

## Supported Services

**[Atlas Cloud](https://www.atlascloud.ai?utm_source=github&utm_medium=link&utm_campaign=Kai)** · [Anthropic](https://console.anthropic.com) · [OpenAI](https://openai.com) · [Gemini](https://aistudio.google.com) · [DeepSeek](https://www.deepseek.com) · [Mistral](https://mistral.ai) · [xAI](https://x.ai) · [OpenRouter](https://openrouter.ai) · [Requesty](https://requesty.ai) · [Groq](https://groq.com) · [NVIDIA](https://developer.nvidia.com) · [Cerebras](https://cerebras.ai) · [Ollama Cloud](https://ollama.com) · [LongCat](https://longcat.chat) · [Together AI](https://together.ai) · [Hugging Face](https://huggingface.co) · [Venice AI](https://venice.ai) · [Moonshot AI](https://moonshot.cn) · [Z.AI](https://z.ai) · [MiniMax](https://minimax.io) · [AIHubMix](https://aihubmix.com) · [Deep Infra](https://deepinfra.com) · [Fireworks AI](https://fireworks.ai) · [OpenCode](https://opencode.ai) · [AI Horde](https://stablehorde.net/) · [1min.AI](https://1min.ai) · [Perplexity](https://www.perplexity.ai) · OpenAI-Compatible API · LiteRT On-Device (Android) · Free tier (no API key needed)

## References (Upstream Kai 9000)

Brox Kai v2.0 is forked from [Kai 9000](https://github.com/SimonSchubert/Kai) by Simon Schubert. All original Kai features, documentation, and resources are available upstream:

- **Upstream Repository:** [SimonSchubert/Kai](https://github.com/SimonSchubert/Kai)
- **Website:** [kai9000.com](https://kai9000.com)
- **Documentation:** [kai9000.com/docs](https://kai9000.com/docs/)
- **App Store:** [apps.apple.com/us/app/kai-ai](https://apps.apple.com/us/app/kai-ai/id6758148023)
- **Play Store:** [play.google.com/store/apps/details?id=com.inspiredandroid.kai](https://play.google.com/store/apps/details?id=com.inspiredandroid.kai)
- **F-Droid:** [f-droid.org/en/packages/com.inspiredandroid.kai](https://f-droid.org/en/packages/com.inspiredandroid.kai/)
- **Web App:** [kai9000.com/app](https://kai9000.com/app/)
- **Homebrew (macOS):** `brew install --cask simonschubert/tap/kai`
- **AUR (Arch Linux):** `yay -S kai-bin`
- **Winget (Windows):** `winget install SimonSchubert.Kai`
- **Upstream Releases:** [GitHub Releases](https://github.com/SimonSchubert/Kai/releases)
- **Sponsor (Atlas Cloud):** [atlascloud.ai](https://www.atlascloud.ai?utm_source=github&utm_medium=link&utm_campaign=Kai)

### Upstream Installation (Original Kai)
| Platform | Format | Download |
|----------|--------|----------|
| Android | APK | [GitHub Releases](https://github.com/SimonSchubert/Kai/releases) |
| macOS | DMG | [GitHub Releases](https://github.com/SimonSchubert/Kai/releases) |
| Windows | MSI | [GitHub Releases](https://github.com/SimonSchubert/Kai/releases) |
| Linux | DEB | [GitHub Releases](https://github.com/SimonSchubert/Kai/releases) |
| Linux | RPM | [GitHub Releases](https://github.com/SimonSchubert/Kai/releases) |
| Linux | AppImage | [GitHub Releases](https://github.com/SimonSchubert/Kai/releases) |

### Upstream Features Reference
- **AI That Builds Screens** — Interactive UI lets the AI generate full interactive screens (quizzes, dashboards, recipes, brainstorms)
- **Linux Sandbox** — On Android, built-in Linux environment via Alpine Linux + proot
- **MCP Servers** — Context7, MDN, DeepWiki, Parallel Search, Yahoo Finance, CoinGecko, Jina AI, Open-Meteo Weather, Kiwi.com, Malwarebytes, tldraw, Find-A-Domain, SubwayInfo NYC
- **Splinterlands Auto-Battle** — Automated Splinterlands Wild Ranked battles (Android & Desktop)
- **Supported Languages** — 50+ languages
- **Screenshot Automation** — Compose screenshot tests for README and store listings

## Credits

- Original Kai 9000 by [Simon Schubert](https://github.com/SimonSchubert)
- Brox Kai v2.0 fork by [habibbrox2](https://github.com/habibbrox2)
- Mistral: https://mistral.ai/
