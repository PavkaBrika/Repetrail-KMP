# MVI и навигация

Актуальный образец: [маршруты и destinations колод](../feature/deck/src/commonMain/kotlin/com/breckneck/feature/deck/navigation/DeckDestinations.kt), [модель списка](../feature/deck/src/commonMain/kotlin/com/breckneck/feature/deck/list/DeckListScreenModel.kt) и [экран списка](../feature/deck/src/commonMain/kotlin/com/breckneck/feature/deck/list/screen/DecksScreen.kt).

## Маршрут и destination

Маршруты типизированы и помечены `@Serializable`. Передавайте через маршрут только аргументы, необходимые для открытия экрана. Например, `DeckFormRoute(deckId: Long? = null)` использует `null` для создания и ID для редактирования.

Destination связывает запись стека, модель и callback навигации:

```kotlin
composable<DeckFormRoute> { entry ->
    val viewModel = koinViewModel<DeckFormViewModel>(
        viewModelStoreOwner = entry,
        extras = entry.defaultViewModelCreationExtras,
    )
    DeckFormScreen(
        viewModel = viewModel,
        onNavigateBack = { navController.popBackStack() },
    )
}
```

Регистрируйте модель в Koin через `viewModel { ... }`. Владельцем служит соответствующий `NavBackStackEntry`: модель очищается при окончательном удалении записи. Форма получает `deckId` из `SavedStateHandle.toRoute<DeckFormRoute>()`. Черновик не сохраняется для восстановления после уничтожения процесса.

## State, Intent и SideEffect

Экранная модель наследует AndroidX `ViewModel`, хранит приватный `MutableStateFlow` и предоставляет `StateFlow` через `asStateFlow()`. UI отправляет действия через `onIntent`; асинхронная работа выполняется в `viewModelScope`. Обязательный базовый класс проекта не нужен.

Экран собирает состояние через `collectAsStateWithLifecycle()`. Одноразовые эффекты собираются одним коллектором внутри `repeatOnLifecycle(Lifecycle.State.RESUMED)`; навигационные эффекты вызывают callback, который destination связывает с контроллером. Контентные composable получают состояние и callbacks, без DI и прямого доступа к навигации.

## Возврат

Неизменённая форма и About передают системный возврат NavHost. Для изменённой формы завершённый жест или кнопка Back показывают существующее подтверждение; отменённый жест не закрывает экран. При открытой клавиатуре Back сначала скрывает её. После подтверждения discard и после сохранения эффект возврата выполняет обычный `popBackStack()`.

Подробности миграции и ручные сценарии: [план шага 3](STEP_3_COMPOSE_NAVIGATION.md).
