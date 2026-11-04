# Custom Design System Module Implementation Plan

Create a dedicated, modular Design System for Compose Multiplatform containing colors, typography, dimensions/spacings, shapes, and elevations. The module will be placed inside a hierarchical package structure `core/ui/designsystem` so that future UI modules (such as `core/ui/components` / `compose-common`) can reside in the same parent directory.

## User Review Required

> [!NOTE]
> Module directory structure and package name:
> - **Gradle module path**: `:core:ui:designsystem` (located under root directory `core/ui/designsystem`)
> - **Future module placement**: `core/ui/components` (path `:core:ui:components`) will share the `core/ui/` parent folder.
> - **Kotlin package**: `com.breckneck.core.ui.designsystem`

## Proposed Changes

### Module Setup & Build Configuration

#### `settings.gradle.kts`
- Include the new `:core:ui:designsystem` module.

#### [NEW] [build.gradle.kts](../core/ui/designsystem/build.gradle.kts)
- Configure KMP with Android and iOS targets (`iosArm64`, `iosSimulatorArm64`, `iosX64`).
- Add Compose Multiplatform dependencies (`compose.runtime`, `compose.foundation`, `compose.material3`, `compose.ui`).

---

### Design System Tokens & Theme

#### [NEW] [ColorTokens.kt](../core/ui/designsystem/src/commonMain/kotlin/com/breckneck/core/ui/designsystem/tokens/ColorTokens.kt)
- Define immutable `RepetrailColors` data class:
  - `background`: `Color(0xFFF6F7FB)`
  - `ink`: `Color(0xFF20233A)`
  - `muted`: `Color(0xFF85899D)`
  - `accent`: `Color(0xFF635BEE)`
  - `mint`: `Color(0xFFB9F2D0)`
  - `line`: `Color(0xFFE9EAF1)`
  - `surface`: `Color.White`
  - `surfaceVariant`: `Color(0xFFF0F1F7)`
  - `selectedTabBackground`: `Color(0xFFF0EFFF)`
- Provide `lightRepetrailColors()` factory and `LocalRepetrailColors` static CompositionLocal.

#### [NEW] [TypographyTokens.kt](../core/ui/designsystem/src/commonMain/kotlin/com/breckneck/core/ui/designsystem/tokens/TypographyTokens.kt)
- Define `RepetrailTypography` data class:
  - `eyebrow`: 10.sp, bold, letterSpacing 1.05.sp
  - `headingLarge`: 29.sp, lineHeight 34.sp, bold
  - `headingMedium`: 15.sp, semiBold
  - `titleMedium`: 14.sp, semiBold
  - `bodyMedium`: 13.sp, lineHeight 19.sp, medium
  - `bodySmall`: 11.sp / 12.sp
  - `tabLabel`: 11.sp, medium / semiBold
  - `badge`: 11.sp, bold
- Provide `defaultRepetrailTypography()` and `LocalRepetrailTypography` CompositionLocal.

#### [NEW] [DimensionTokens.kt](../core/ui/designsystem/src/commonMain/kotlin/com/breckneck/core/ui/designsystem/tokens/DimensionTokens.kt)
- Define `RepetrailSpacings` / `RepetrailDimensions` data class:
  - Spacings: `extraSmall` (4.dp), `small` (6.dp / 8.dp), `medium` (12.dp / 14.dp / 16.dp), `large` (20.dp / 22.dp), `extraLarge` (28.dp)
  - Icon sizes: `small` (18.dp), `medium` (20.dp), `default` (24.dp)
  - Elevations: `card` (1.dp), `tonal` (3.dp), `floating` (12.dp)
  - Component heights: `navBarItem` (54.dp)
- Provide `defaultRepetrailDimensions()` and `LocalRepetrailDimensions` CompositionLocal.

#### [NEW] [ShapeTokens.kt](../core/ui/designsystem/src/commonMain/kotlin/com/breckneck/core/ui/designsystem/tokens/ShapeTokens.kt)
- Define `RepetrailShapes` data class:
  - `card`: `RoundedCornerShape(22.dp)`
  - `cardSmall`: `RoundedCornerShape(18.dp)`
  - `navBar`: `RoundedCornerShape(28.dp)`
  - `navBarItem`: `RoundedCornerShape(21.dp)`
  - `dialog`: `RoundedCornerShape(24.dp)`
  - `pill`: `CircleShape`
- Provide `defaultRepetrailShapes()` and `LocalRepetrailShapes` CompositionLocal.

#### [NEW] [RepetrailTheme.kt](../core/ui/designsystem/src/commonMain/kotlin/com/breckneck/core/ui/designsystem/theme/RepetrailTheme.kt)
- Theme object `RepetrailTheme` providing static accessors:
  - `RepetrailTheme.colors`
  - `RepetrailTheme.typography`
  - `RepetrailTheme.dimensions`
  - `RepetrailTheme.shapes`
- Theme root Composable `RepetrailTheme` providing composition locals and wrapping `MaterialTheme`.

---

### Project Integration & Refactoring

#### [`shared/build.gradle.kts`](../shared/build.gradle.kts)
- Add dependency on `:core:ui:designsystem`.

#### [`RepetrailTheme.kt`](../shared/src/commonMain/kotlin/com/breckneck/repetrail/ui/theme/RepetrailTheme.kt)
- Re-export or delegate to the new design system theme from `:core:ui:designsystem`.

#### Existing UI Components and Screens
- Refactor `FloatingNavigationBar.kt`, `PageHeading.kt`, `CardsScreen.kt`, `MenuScreen.kt`, `TasksScreen.kt`, `MainMenuScreen.kt` to consume `RepetrailTheme.colors`, `RepetrailTheme.dimensions`, `RepetrailTheme.typography`, `RepetrailTheme.shapes` instead of hardcoded DP/SP values and raw colors.

---

## Verification Plan

### Automated Tests
- Gradle build verification:
  - `./gradlew :core:ui:designsystem:commonMainClasses`
  - `./gradlew :androidApp:assembleDebug`
  - `./gradlew :shared:allTests`

### Manual Verification
- Verify that app builds cleanly and all screens render identically using design tokens.
