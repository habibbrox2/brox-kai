# Brox Kai v2.0 — Brand Name Usage

## Canonical name

**Brox Kai** is the product and brand name. The text `v2.0` is not part of the brand name and must not be used as a badge, parenthetical, secondary label, or other brand identifier.

## How to present it

- Write the full name as `Brox Kai` in product titles, onboarding, store copy, documentation, marketing, and sponsorship content.
- Do not put `v2.0` in a badge, parenthesis, secondary label, or separate version field.
- `BK2.0` may be used as a short form only after `Brox Kai` has been established in the same context.

## Technical versions and releases

Build numbers, Git tags, package versions, and release labels are technical metadata. Present them separately from the product name.

## Fork app-version policy

The fork's app version is technical release metadata, separate from the brand name. Use semantic versioning (`MAJOR.MINOR.PATCH`): increment PATCH for compatible fixes, MINOR for backward-compatible features, and MAJOR for incompatible changes.

For the first independently distributed fork release with its own package identity, start at **`1.0.0`**. Until then, the code still carries Kai 9000's `3.3.0` app version and upstream application identifier. If the fork must remain an in-place update of that upstream package, ship the first fork update as **`3.3.1`** and increase Android `versionCode` from `117` to `118`. Once the fork has its own package identity, start independent releases at `1.0.0`.

Example:

```text
Brox Kai
App version 1.0.0
```

Never use an app version or technical build number to redefine or split the product name.

## UI guidance

- App headers, splash screens, dashboard greetings, and Settings titles should display `Brox Kai` as the product name.
- Use the approved full wordmark whenever there is enough room. Use the BK icon alone in constrained spaces such as app icons and compact navigation.
- Ensure screen readers announce the complete name as “Brox Kai”.
