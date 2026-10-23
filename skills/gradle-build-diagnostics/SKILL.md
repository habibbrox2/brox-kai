---
name: gradle-build-diagnostics
description: Diagnose Gradle, Kotlin compiler, Wasm distribution and toolchain failures in Brox Kai v2.0 without masking the first root cause.
---

# Gradle Build Diagnostics

1. Record branch/status/diff and verify JDK; existing CI uses JDK 21.
2. Use `gradlew.bat` on Windows and discover task names with `tasks --all` before relying on them.
3. Reproduce the narrowest failing task; retain full command, exit code and the first meaningful compiler error.
4. Separate toolchain/dependency resolution from source errors, and syntax/import errors from target-compatibility errors.
5. Do not run `clean`, change wrapper/plugin versions, or edit caches until evidence justifies it.
6. Be cautious with `.github/workflows/test.yml`: it formats code, updates screenshots, converts images and can push generated changes.
7. Propose the minimal fix and validation ladder; do not modify code/build files without authorization.
8. Never report a successful build from a still-running process or partial log.
