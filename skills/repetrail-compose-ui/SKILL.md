---
name: repetrail-compose-ui
description: Create or modify Jetpack Compose and Compose Multiplatform screens and components in Repetrail using its design-system tokens, UI conventions, resources, and preview practices.
---

# Repetrail Compose UI

Apply these rules when creating or changing Repetrail Compose screens and components. Inspect the affected rendering code, its state and navigation boundary, available components, and current tokens first. Existing UI can help locate behavior and reusable components, but existing violations are not precedents for new recommendations. Keep changes within the requested scope; do not repair unrelated UI along the way.

For screen layout, visual styling, floating controls, or system-bar geometry, also apply [Repetrail Bubble Design](../repetrail-bubble-design/SKILL.md). It defines the app's edge-to-edge visual language, circular back/overflow bubbles, optional collapsing title, and FAB clearance above the floating bottom navigation. The content viewport extends behind system bars; interactive controls stay within safe bounds.

## Design-system sources of truth

Paths below are relative to this skill file. Read the relevant sources before choosing styling values; do not duplicate their token definitions here.

| Source | Access and purpose |
| --- | --- |
| [RepetrailTheme.kt](../../core/ui/designsystem/src/commonMain/kotlin/com/breckneck/core/ui/designsystem/theme/RepetrailTheme.kt) | Theme provider and `RepetrailTheme` accessors. |
| [ColorTokens.kt](../../core/ui/designsystem/src/commonMain/kotlin/com/breckneck/core/ui/designsystem/tokens/ColorTokens.kt) | `RepetrailTheme.colors`: semantic colors and light/dark palettes. |
| [ShapeTokens.kt](../../core/ui/designsystem/src/commonMain/kotlin/com/breckneck/core/ui/designsystem/tokens/ShapeTokens.kt) | `RepetrailTheme.shapes`: component shapes. |
| [DimensionTokens.kt](../../core/ui/designsystem/src/commonMain/kotlin/com/breckneck/core/ui/designsystem/tokens/DimensionTokens.kt) | `RepetrailTheme.dimensions`: spacing (`spacings`), icon sizes (`iconSizes`), elevation (`elevations`), and other dimension tokens. |
| [TypographyTokens.kt](../../core/ui/designsystem/src/commonMain/kotlin/com/breckneck/core/ui/designsystem/tokens/TypographyTokens.kt) | `RepetrailTheme.typography`: text styles. |

## 1. Theme ownership and token access

Use `RepetrailTheme` at the app/UI entry root and in every standalone preview. Screens and child components under that root inherit the theme; do not wrap each child in another theme.

Obtain colors, shapes, sizes, spacing, elevation, and typography through `RepetrailTheme.colors`, `.shapes`, `.dimensions`, and `.typography`. Local aliases such as `val dimensions = RepetrailTheme.dimensions` are fine. Choose tokens by their intended role, not just a matching numeric value. Let themed components inherit defaults where appropriate; explicit styling must use the Repetrail tokens rather than hardcoded values or an independent Material theme.

## 2. Missing tokens require an agreed extension

If no token fits, propose a token name, concrete value, semantic purpose, and the source file to extend. Include light/dark values when relevant. Obtain agreement on the design-system extension before adding or applying the value. Do not silently substitute a literal, derive an ad hoc style, or repurpose an unrelated token. Continue independent work that does not depend on the decision; existing explicit approval for that particular extension is sufficient.

Whenever introducing a hardcoded UI value (such as spacing, size, or an offset), add a brief adjacent comment explaining its purpose and where the value comes from or how it is calculated. This also applies when defining a new token. One or two short sentences are enough: explain the reason or calculation rather than restating the code. A comment does not replace token reuse or the required agreement on a design-system extension.

## 3. Spacing and code grouping

Prefer `Spacer` for gaps between elements while it improves readability. Use token-based `padding` for internal space and when extra spacers would make the layout harder to read. Preserve necessary system insets and scroll-container `contentPadding`; do not replace them with spacers. Concise arrangements or list spacing are appropriate when individual spacers would add noise.

Separate neighboring major UI elements and each intervening `Spacer` with blank lines. Keep closely related declarations, arguments, and small pieces of one element grouped together.

```kotlin
Column(modifier = modifier) {
    Text(
        text = title,
        style = RepetrailTheme.typography.headingMedium,
        color = RepetrailTheme.colors.ink,
    )

    Spacer(Modifier.height(RepetrailTheme.dimensions.spacings.medium))

    Text(
        text = description,
        style = RepetrailTheme.typography.bodyMedium,
        color = RepetrailTheme.colors.muted,
    )
}
```

Here `title` and `description` are inputs: resolve app-authored copy from resources at the appropriate boundary, while keeping user data as data.

## 4. Named arguments and lambdas

Use named arguments when they improve clarity, such as ambiguous values, booleans, or multiple similar parameters. Ordinary arguments may be positional when their role is obvious from the call: `Spacer(Modifier.height(RepetrailTheme.dimensions.spacings.medium))` does not need `modifier =` or `height =`. Do not add redundant names mechanically.

Name every non-content lambda or function-valued argument wherever Kotlin supports named arguments, including event handlers, predicates, transformations, and item keys. In particular, pass custom-component handlers as `onBackClick = { navigator.pop() }`, `onBackClick = onBackClick`, or `onIntent = screenModel::onIntent`. Do not put these arguments in the trailing-lambda position.

Obvious UI content is the exception and may use a trailing lambda, such as `Box(modifier) { ... }` or `RepetrailTheme { ... }`. Kotlin restrictions on named arguments still apply, including calls through function types. Implicit `it` is allowed when its meaning is clear; use an explicit parameter name when it improves clarity.

## 5. Localized copy from the start

Put app-authored user-visible text in resources immediately, including labels, accessibility descriptions, empty/error messages, dialogs, and snackbars. For shared UI, use Compose Multiplatform resources in the owning module's `src/commonMain/composeResources/values/strings.xml` and the `org.jetbrains.compose.resources` APIs with generated `Res` accessors. Keep resources in an accessible owning module; do not introduce a dependency from a feature back to the app just to share text.

Use formatted string resources for substituted values and plural resources for quantity-dependent wording. Do not construct localized phrases by concatenation or Kotlin interpolation. Deck names, card text, and other user data are not string resources; pass them as data or as formatting arguments inside a localized message.

## 6. Proportionate, representative previews

Add previews when they do not require disproportionately large supporting infrastructure. Usually one preview is enough for a small component; add variants only to show a meaningful difference. Wrap preview content in `RepetrailTheme` and pass sample state and inert event handlers to rendering composables rather than constructing dependency injection, repositories, or navigation.

For screens, show existing loading, success/content, empty, and error states where feasible. Add a dark-theme variant when relevant to the change. Do not invent product states solely to create previews. Prefer representative data, including long text where useful, over many nearly identical examples.

## 7. Reuse and rendering boundaries

Reuse existing components where their behavior and API fit. Inspect [shared UI components](../../shared/src/commonMain/kotlin/com/breckneck/repetrail/ui/components/) and components in the affected feature before introducing equivalents. Reuse does not authorize copying their existing styling or localization violations.

Keep rendering separate from state collection, dependency injection, and navigation wiring. Follow the existing architecture at the integration boundary instead of introducing a new state-management pattern. Reusable components should accept `modifier: Modifier = Modifier` and named event callbacks for their interactions; apply the supplied modifier to their outer layout. Pass state and events to the rendering layer rather than reaching into a navigator or state holder from a reusable child.

## 8. Accessibility

Provide appropriate semantics, meaningful control labels, and adequate touch targets. Give meaningful icons localized content descriptions when their purpose is not already announced by the parent control. Decorative icons use `contentDescription = null`; avoid duplicate announcements. Check that merged semantics still expose the intended actions and state.

## 9. Flexible layouts, insets, and scrolling

Support narrow screens, large font settings, and long translations. Prefer content-driven or flexible sizing and wrapping; avoid unjustified fixed widths or heights that clip text or controls.

Handle system bars, the keyboard, and scrolling at a clear layout boundary. Inspect padding already supplied by the parent or `Scaffold`, apply or consume insets appropriately, and avoid applying the same inset twice. Keep focused input and bottom actions reachable when the keyboard is visible. Preserve intentional list `contentPadding` and avoid nested scrolling containers with unbounded constraints.

## 10. State and effects

Hoist state to the lowest level that needs to own or share it. Preserve input and scroll position when the workflow requires it, using appropriate saveable state or the existing state holder. Keep previewable rendering driven by explicit inputs.

Run side effects with the appropriate Compose APIs and meaningful keys, such as `LaunchedEffect`, `DisposableEffect`, and `rememberCoroutineScope`. Do not start navigation, subscriptions, or other external work directly during composition. Release resources when their composition lifecycle ends.

## 11. Lists and measured optimization

Use lazy containers for large or unbounded lists and stable, unique item keys based on identity. Avoid position-based keys for mutable lists. Preserve list state as needed and keep events explicitly named at component call sites. Prefer concise lazy-list spacing to inserting separate spacer items when those would complicate the list.

Introduce optimizations when the affected behavior or measurements justify them. Do not add blanket caching, stability annotations, or recomposition workarounds as a ritual.

## 12. Verification and scope

Check the affected real states and previews, including relevant interactions, narrow layouts, large fonts, long translations, theme contrast, and inset/keyboard behavior. Verify resource references and compilation for the affected targets using the project's available checks. If preview rendering or device verification is unavailable, report that limitation instead of claiming it passed.

Add focused UI tests for substantial behavior, such as meaningful state transitions or important user actions. Do not require screenshot-test infrastructure for every UI change. Keep unrelated UI and existing violations outside the task.

## Consistency checks

- **Back action component:** expose `modifier` and `onBackClick`; the integration boundary passes `onBackClick = { navigator.pop() }`. Inside the component, use `onClick = onBackClick`; button content may remain a trailing lambda. A simple themed preview can pass `onBackClick = {}`. A clear callback body such as `onItemClick = { onSelect(it) }` is valid.
- **List screen with states:** keep state/navigation wiring outside content, render the existing state branches, and use a lazy list with identity keys for content. Preview supported loading/content/empty/error states with inert callbacks, preserve scroll state as needed, and account for parent padding once. Do not add absent state variants just for previews.
- **Missing token:** if a design calls for a dimension with no suitable token, propose its value, role, name, and location in `DimensionTokens.kt`, then wait for agreement before extending or using it. Do not slip the proposed value into the component while the decision is pending.
