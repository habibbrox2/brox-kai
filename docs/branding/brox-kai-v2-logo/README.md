# Brox Kai v2.0 — Logo Asset Pack

Use **Brox Kai v2.0** as one complete brand name. The `v2.0` text is part of the name, not a separate version badge. See [brand name usage](../brand-name-usage.md).

Palette reference:
- Navy Blue: #0B2B5B
- Cyan: #00D1E8

## Contents
- `01_Geometric_Logo/`: geometric-symbol reference crop from the first image.
- `02_BK_Monogram/`: BK symbol crops and square PNG sizes.
- `03_Wordmarks/`: light/dark wordmark crops and width-based exports.
- `04_App_Icons/`: icon crops and square PNG sizes.
- `05_Monochrome/`: single-color silhouette variants and common sizes.
- `06_Previews/`: contact sheet preview.
- `manifest.json`: generated asset list, dimensions, and notes.
- Animated logo assets are maintained in [`site/img/logo_animation.svg`](../../../site/img/logo_animation.svg) (original BK loading mark) and [`site/img/logo_animation_readme.svg`](../../../site/img/logo_animation_readme.svg) (refined BK plus Brox Kai v2.0 wordmark used by the README).

## Important source-quality notes
1. The first reference image contains a checkerboard pattern baked into its RGB pixels. Its geometric reference crops are therefore **not guaranteed to have a transparent background**.
2. The second image has an alpha channel. Cropped assets preserve that channel, but alpha halos and the visual quality of each crop should still be checked before production use.
3. The wordmark and icon crops originate from a branding board, not clean source vectors. Some crops may include original glow, edge artifacts, or board-derived effects.
4. Resized PNGs are raster exports, not vector artwork. Do not treat them as a replacement for a proper SVG master.
5. This pack is a practical extraction starter pack, not a claim that every asset is production-perfect. Review `contact_sheet.jpg` and validate each asset against the original before shipping.

## Recommended use
- Android: use a suitable high-resolution icon source and prepare adaptive foreground/background assets according to the app's actual icon pipeline.
- Desktop: generate the exact icon format required by the existing packaging configuration.
- Website/docs: use wordmark exports at a suitable width.
