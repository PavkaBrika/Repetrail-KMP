# Геометрия плавающего управления и insets

Читай при изменении app shell, верхних controls, FAB, нижних панелей, scroll-padding или поведения IME. Сначала установи, какой предок уже применяет/consumes insets и где находятся панели относительно feature-экрана.

## Layout-контракт

Предпочтительная схема:

```text
Полноэкранный root
├── Контент: viewport до краёв окна, внутренние scroll-отступы
└── Overlay-слой: независимые островки в безопасных границах
    ├── Назад / компактный заголовок / меню
    ├── FAB или floating action bar
    └── Нижняя навигация приложения, когда присутствует
```

Root согласует геометрию. Контракт может передавать `PaddingValues`/занятые области, предоставлять slots или размещать элементы в общем layout. Не связывай feature с app-модулем ради доступа к панели. Пересчитывай границы при изменении размера окна, шрифта, видимости панелей и IME.

Измеряй **видимую панель** отдельно от внешнего системного padding. Все границы сравнивай в одной системе координат. Для изменения layout предпочитай измерения в соответствующей фазе layout; не создавай цикл «измерение → state → изменение той же высоты».

## Инварианты расположения

Пусть `H` — высота окна, координата `y` растёт вниз, `B` — безопасный нижний inset от этого же окна, `gEdge` — тематический зазор до безопасного края, `gControls` — зазор между панелью и FAB.

```text
nav.bottom = H - B - gEdge
nav.top    = nav.bottom - measuredNavHeight

если нижняя навигация видна:
    fab.bottom <= nav.top - gControls
иначе:
    fab.bottom <= H - B - gEdge
```

`measuredNavHeight` не включает уже учтённые `B` и `gEdge`. Если контракт вместо высоты передаёт полную занятую снизу область или готовую координату `nav.top`, не прибавляй inset второй раз. Для floating action bar формулы те же. Верхняя граница плавающих элементов находится ниже верхней safe area с тематическим зазором; учитывай доступную ширину между боковыми bubbles.

**Clearance для списка** определяется самым высоким препятствием, которое пересекает его полезную ширину: nav, FAB/action bar и соответствующими зазорами. Отступ должен позволять последней строке целиком прокрутиться выше препятствия. Учитывай уже применённые родительские отступы; не суммируй полные высоты пересекающихся областей.

Для IME используй эффективную доступную область выбранной политики. Часто нижняя граница — верх клавиатуры, а глобальная навигация скрыта. IME и navigation bar могут занимать пересекающиеся области: их union/максимум уместен для общего нижнего ограничения; простая сумма обычно завышает отступ.

## Compose: что проверить в коде

- `enableEdgeToEdge()` включает рисование за системным UI, но не размещает управление автоматически.
- Root-фон и viewport списка остаются полноэкранными. `safeDrawingPadding()`/`systemBarsPadding()` на всём контентном предке могут обрезать нужный эффект. Применяй выбранные insets к overlay-controls и к внутреннему clearance контента.
- `LazyColumn.contentPadding` позволяет первым/последним элементам отступать в покое и уходить под overlay при прокрутке. Внешний `.padding()` уменьшает viewport. Для обычного scroll-контента порядок padding и scroll-модификатора также определяет, прокручивается ли отступ вместе с содержимым.
- Material `Scaffold` и компоненты могут иметь собственные insets. Проверь `contentWindowInsets`, переданный `innerPadding`, вложенные scaffolds и `consumeWindowInsets` до добавления нового padding. При ручном владении insets отключай только дублирующий автоматический учёт и обеспечь защиту controls самостоятельно.
- `windowInsetsPadding`, `navigationBarsPadding` и `imePadding` участвуют в consumption. `asPaddingValues()` само по себе не помечает inset потреблённым; при ручном применении согласуй consumption для соответствующих потомков. Не потребляй область, которую layout фактически не учёл.
- Соседние overlay-ветки могут независимо нуждаться в одном системном inset. «Учитывать один раз» означает один раз на пути до конкретного элемента, а не один раз на всё дерево.
- `zIndex` управляет отрисовкой, а не освобождает место и не перемещает hit targets. Геометрическое пересечение исправляется размещением.
- Проверяй существующие API и версию Compose проекта перед выбором конкретного механизма. Здесь задан layout-контракт, а не обязательный универсальный composable.

## Платформенные детали

**Android.** В gesture navigation цель — прозрачная системная область без дополнительной непрозрачной полосы. `enableEdgeToEdge()` по умолчанию может оставлять contrast scrim в three-button navigation. Для полной прозрачности проверь `window.isNavigationBarContrastEnforced = false` на Android-границе приложения и читаемость системных кнопок; не считай, что один вызов edge-to-edge уже обеспечивает оба режима. Сверяй поведение с [system bar protection](https://developer.android.com/develop/ui/compose/system/system-bars) и доступными API проекта.

**iOS.** Сохраняй фон и scroll-viewport до краёв окна; controls учитывают safe area, home indicator, вырезы и клавиатуру. Проверь host-контроллер и shared Compose, чтобы safe area не добавлялась повторно. Принципы Apple floating controls не гарантируют наличие нативного Liquid Glass в Compose Multiplatform.

## Точки входа в текущем проекте

Пути ниже относительны этому reference. Читай фактическую версию файлов перед изменением:

- [MainActivity.kt](../../../androidApp/src/main/kotlin/com/breckneck/repetrail/MainActivity.kt) — Android edge-to-edge setup.
- [MainMenuScreen.kt](../../../shared/src/commonMain/kotlin/com/breckneck/repetrail/ui/navigation/MainMenuScreen.kt) — общий tab shell и размещение нижней панели.
- [FloatingNavigationBar.kt](../../../shared/src/commonMain/kotlin/com/breckneck/repetrail/ui/components/FloatingNavigationBar.kt) — размеры/поверхность навигационного островка.
- [PageHeading.kt](../../../shared/src/commonMain/kotlin/com/breckneck/repetrail/ui/components/PageHeading.kt) — существующий заголовок для оценки переиспользования.
- [DeckListContent.kt](../../../feature/deck/src/commonMain/kotlin/com/breckneck/feature/deck/list/screen/DeckListContent.kt) — feature-level Scaffold, FAB и list-padding.

Это места интеграции, а не подтверждение соответствия гайдлайнам. Особое внимание: safe-area padding вокруг `CurrentTab`, FAB во вложенном `Scaffold` и фиксированные нижние отступы могут расходиться с полноэкранным viewport и фактической геометрией нижней панели.
