# Шаг 3 — Compose Navigation: подробный план реализации

> **Дата создания:** 2026-11-06
> **Основание:** [MASTER_PLAN.md](MASTER_PLAN.md), шаг 3.
> **Статус:** план согласован; реализация в процессе.

## Результат и ограничения

Перевести существующую навигацию с Voyager на мультиплатформенную Compose Navigation с `NavHost` / `NavController`. Перенести две модели колод на AndroidX KMP ViewModel с Koin. Сохранить существующие экраны, CRUD и внешний вид.

**Уточнение, выбранное разработчиком:** неизменённая форма и экран «О приложении» используют штатные интерактивные жесты. В изменённой форме завершённый жест открывает подтверждение выхода без анимации ухода. Отменённый жест ничего не делает.

Приложение ещё не опубликовано. В рамках шага:

- Не добавлять автоматические тесты, тестовую инфраструктуру, lifecycle harness, диагностические экраны и постоянное логирование.
- Не создавать собственный навигационный фреймворк, Router, глобальный event bus, базовый ViewModel или универсальный механизм подтверждения выхода.
- Не сохранять совместимость с сериализованными стеками Voyager и не создавать миграцию старого навигационного состояния.
- Не менять Room, репозитории, use cases и бизнес-логику CRUD.
- Не реализовывать карточки, детали колоды, snackbar создания, undo удаления и остальные отложенные задачи.
- Не переделывать дизайн, отступы, загрузку, тексты и компоненты экранов.
- Проверку приложения выполняет разработчик вручную по короткому списку в конце.

## Зафиксированная схема

### Табы и стеки

Использовать **три отдельных `NavController`**, по одному на таб. В композиции отображать `NavHost` только выбранного таба.

| Таб | Начальный экран | Вложенные экраны |
|---|---|---|
| Tasks | `TasksRoute` | Пока отсутствуют |
| Decks — текущая подпись «Карточки» | `DeckListRoute` | `DeckFormRoute(deckId: Long? = null)` |
| Menu | `MenuRoute` | `AboutRoute` |

Все маршруты пометить `@Serializable`.

`deckId = null` означает создание, ненулевой `deckId` — редактирование. В маршрут формы передавать только ID; название, описание и объект колоды через навигацию не передавать.

Маршруты колод и регистрацию их destinations разместить в `feature:deck`. Маршруты Tasks/Menu/About оставить в `shared`. Зависимость `feature:deck → shared` не добавлять.

### Владение состоянием

- `NavController` и выбранный таб принадлежат оболочке приложения.
- Каждая экранная ViewModel принадлежит своей `NavBackStackEntry`.
- Данные формы принадлежат `DeckFormViewModel`; `deckId` формы читается из `SavedStateHandle` записи маршрута.
- Позиция прокрутки остаётся Compose-состоянием соответствующего экрана.
- Контентные composable получают `State` и callbacks/intents; навигацию и DI в них не переносить.

## Task list

Выполнять последовательно: **3.1 → 3.2 → 3.3 → 3.4 → 3.5 → 3.6 → 3.7 → 3.8**. Галочки отмечать только по факту.

### 3.1. Подключить зависимости

Исходные версии проекта: Kotlin `2.4.21`, Compose Multiplatform `1.12.2`, Koin `4.2.2`, AGP `9.1.1`.

- [x] Добавить в version catalog следующие зависимости:

| Артефакт | Версия |
|---|---|
| `org.jetbrains.androidx.navigation:navigation-compose` | `2.10.0-rc01` |
| `org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose` | `2.11.0` |
| `org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-savedstate` | `2.11.0` |
| `org.jetbrains.androidx.lifecycle:lifecycle-runtime-compose` | `2.11.0` |
| `org.jetbrains.androidx.navigationevent:navigationevent-compose` | `1.1.0` |
| `org.jetbrains.kotlinx:kotlinx-serialization-json` | `1.11.0` |
| `io.insert-koin:koin-compose-viewmodel` | Существующий `version.ref = "koin"` |

Navigation, Lifecycle и Navigation Event взяты из состава [Compose Multiplatform 1.12.2](https://github.com/JetBrains/compose-multiplatform/releases/tag/v1.12.2). Версия Serialization соответствует примеру для [Kotlin 2.4.21](https://kotlinlang.org/docs/serialization-get-started.html).

- [x] Добавить plugin alias `org.jetbrains.kotlin.plugin.serialization` с `version.ref = "kotlin"`.
- [x] Объявить plugin alias в корневом Gradle-файле с `apply false`.
- [x] Применить Serialization plugin в `shared` и `feature:deck`.
- [x] В `shared/commonMain` подключить Navigation Compose и Serialization.
- [x] В `feature:deck/commonMain` подключить перечисленные зависимости для навигации, ViewModel, состояния и Back.
- [x] Для общих Compose API использовать мультиплатформенные артефакты JetBrains; импорты классов при этом остаются `androidx.*`.
- [x] Voyager пока оставить: удалить после перевода всех использований в пункте 3.7.

**Готово, когда:** зависимости и плагины объявлены. Navigation 3, `NavDisplay` и entry decorators не подключены. Общего обновления toolchain нет.

### 3.2. Определить маршруты и границу feature-модуля

- [x] В пакете `navigation` модуля `feature:deck` создать публичные маршруты `DeckListRoute` и `DeckFormRoute`.
- [x] Там же создать публичную функцию `NavGraphBuilder.deckDestinations(navController: NavHostController)`.
- [x] В функции зарегистрировать список и форму через типизированные `composable<…>`.
- [x] В пакете навигации `shared` создать внутренние маршруты `TasksRoute`, `MenuRoute`, `AboutRoute`.
- [x] Для создания открывать `DeckFormRoute()`, для редактирования — `DeckFormRoute(deckId)`.
- [x] Для переходов на форму и About использовать `launchSingleTop = true`, сохранив отсутствие дубликатов при повторном нажатии.
- [x] Не создавать строковые шаблоны маршрутов, собственные сериализаторы и ручной парсинг аргументов.

**Готово, когда:** destinations и аргументы определены, `shared` может подключить навигацию колод через публичную функцию feature-модуля.

### 3.3. Заменить оболочку Voyager

Основная точка изменения — [MainMenuScreen.kt](../shared/src/commonMain/kotlin/com/breckneck/repetrail/ui/navigation/MainMenuScreen.kt).

- [x] Превратить `MainTab` в обычный enum с текущими названием и иконкой. Удалить реализацию Voyager `Tab`, `Screen`, `TabOptions` и создание `Navigator`.
- [x] В `MainMenuScreen` создать три `rememberNavController()` **выше выбора активного таба**. Переключение вкладки не должно создавать новый контроллер.
- [x] Хранить выбранный таб через `rememberSaveable`; начальное значение — Tasks.
- [x] Создать `rememberSaveableStateHolder()` и отображать активный таб внутри `SaveableStateProvider` со стабильным ключом таба.
- [x] Для выбранного таба показывать соответствующий `NavHost`. Не держать скрытые NavHost поверх друг друга.
- [x] В Tasks зарегистрировать текущий `TasksScreen`.
- [x] В Decks подключить `deckDestinations`.
- [x] В Menu зарегистрировать текущий `MenuScreen` и `AboutContent`.
- [x] Callback «О приложении» вызывает переход в контроллере Menu; кнопка возврата вызывает `popBackStack()` этого же контроллера.
- [x] Повторное нажатие активного таба ничего не делает.
- [x] Переключение таба меняет только выбранный таб: без `popUpTo`, сброса стека и подтверждения выхода из формы.
- [x] Сохранить существующие `RepetrailTheme`, Surface, floating navigation bar и обработку insets.
- [x] Панель вкладок оставить видимой и на форме, и на About, как сейчас.
- [x] На корне каждого таба не регистрировать собственный обработчик выхода: Android Back передаётся системе, без перехода на Tasks.

Для независимых стеков допустимы отдельные NavHost; библиотека также предоставляет штатные платформенные жесты. См. [Compose Navigation](https://kotlinlang.org/docs/multiplatform/compose-navigation.html).

**Готово, когда:** три таба работают без Voyager, каждый сохраняет собственный стек.

### 3.4. Перенести ScreenModel на AndroidX ViewModel

- [x] Переименовать `DeckListScreenModel` в `DeckListViewModel`, `DeckFormScreenModel` — в `DeckFormViewModel`.
- [x] Заменить `StateScreenModel` на `androidx.lifecycle.ViewModel`.
- [x] В каждой модели явно объявить приватный `MutableStateFlow` и публичный `StateFlow` через `asStateFlow()`.
- [x] Заменить `screenModelScope` на `viewModelScope`.
- [x] Сохранить существующие `State`, `Intent`, `SideEffect`, Channel, загрузку, валидацию, сохранение и удаление.
- [x] Сохранить проброс `CancellationException`.
- [x] В форме добавить `SavedStateHandle` в конструктор. `deckId` получать из `savedStateHandle.toRoute<DeckFormRoute>()`.
- [x] В Koin заменить `factory` экранных моделей на `viewModel { … }`. Регистрации use cases оставить прежними.
- [x] Получать модели через `koinViewModel()` внутри соответствующих destinations; явно передавать их `NavBackStackEntry` как `viewModelStoreOwner` и её `defaultViewModelCreationExtras`.
- [x] Не создавать модели через `remember`, `get()` или конструктор внутри composable. Не привязывать их к Activity или всему табу.
- [x] Обычный `popBackStack()` формы должен окончательно удалять её запись без `saveState = true`; очисткой ViewModel занимается библиотека.

В Navigation Compose запись destination является владельцем ViewModel; Koin получает аргументы и `SavedStateHandle` через её extras. См. [Koin ViewModel in Compose](https://insert-koin.io/docs/reference/koin-compose/compose-viewmodel/).

**Готово, когда:** модели переживают рекомпозицию и переключение табов, а после окончательного выхода новая форма получает новую модель.

### 3.5. Перевести экранные обёртки и сохранить состояние

- [x] Заменить Voyager-обёртки `DecksScreen` и `DeckFormScreen` обычными composable-функциями.
- [x] Удалить из них `Screen`, `.Content()`, `LocalNavigator` и `koinScreenModel`.
- [x] Собирать состояние ViewModel через `collectAsStateWithLifecycle()`.
- [x] Обрабатывать навигационные side effects одним коллектором для соответствующей записи, пока она находится в `RESUMED`.
- [x] `NavigateToDeckForm` открывает типизированный маршрут в контроллере Decks.
- [x] `NavigateBack` вызывает один `popBackStack()`. Использовать его и после сохранения, и после подтверждения discard.
- [x] `DeckSaved` оставить без нового пользовательского поведения: snackbar и возврат результатов в список здесь не реализовывать.
- [x] `NavigateToDeckDetail` оставить неподключённым; исправить комментарий на «шаг 4». Экран деталей не создавать.
- [x] В списке явно использовать `rememberLazyListState()` и передать его в `LazyColumn`; сохранить стабильные ключи колод.
- [x] Существующее scroll-состояние формы и остальных экранов оставить в контентном слое.

**Готово, когда:** переключение табов и Android rotation сохраняют ввод и прокрутку; экран редактирования загружает выбранную колоду.

### 3.6. Настроить жесты и подтверждение выхода

#### Штатные переходы

- [ ] Оставить стандартные transitions у NavHost и destinations. Не задавать собственные slide/fade-переходы: они могут заменить штатную iOS-анимацию.
- [ ] В iOS сохранить включённый back gesture у `ComposeUIViewController`.
- [ ] В Android manifest явно установить `android:enableOnBackInvokedCallback="true"`.
- [ ] Сохранить текущий `ComponentActivity`; не создавать собственный системный dispatcher.
- [ ] На неизменённой форме без клавиатуры и на About отдавать жест самому NavHost.

Navigation Compose поддерживает predictive back штатно; постоянный перехват Back поверх него здесь не нужен. См. [Android predictive back](https://developer.android.com/develop/ui/compose/system/predictive-back-setup).

#### Форма

- [ ] Заменить существующий `expect/actual DeckFormBackHandler` одним общим Compose-обработчиком на `NavigationBackHandler`; удалить Android-реализацию с обычным `BackHandler` и пустую iOS-реализацию.
- [ ] Создать для него собственный `rememberNavigationEventState`. Не использовать один state в нескольких обработчиках.
- [ ] Включать обработчик только при открытой клавиатуре или несохранённых изменениях. При открытом подтверждении выходом управляет сам bottom sheet.
- [ ] При завершённом Back с клавиатурой сначала скрывать клавиатуру и оставлять форму открытой.
- [ ] При завершённом Back без клавиатуры отправлять существующий `BackClicked`.
- [ ] На отменённый жест не отправлять intent, не показывать подтверждение и не менять стек.
- [ ] Кнопка возврата формы продолжает отправлять тот же `BackClicked`.
- [ ] При изменениях `BackClicked` открывает существующий bottom sheet «Отменить изменения?».
- [ ] «Отмена», закрытие sheet и Back внутри sheet оставляют форму и введённые данные.
- [ ] «Выйти» отправляет существующий `ConfirmDiscard`; последующий `NavigateBack` закрывает форму без повторной проверки изменений.
- [ ] После успешного сохранения `NavigateBack` также закрывает форму напрямую.
- [ ] Сохранить штатную обработку Back у sheet удаления и контекстного меню списка; не добавлять глобальные перехватчики поверх них.

**Готово, когда:** обычный возврат и жесты соблюдают выбранное поведение. Изменённая форма не удаляется из стека до подтверждения.

### 3.7. Удалить Voyager и обновить соглашение

- [ ] Удалить старые Voyager-обёртки корневых экранов и About после подключения их composable в графах.
- [ ] Удалить Voyager-зависимости из `shared`, `feature:deck` и version catalog.
- [ ] Проверить поиском production-код и Gradle-файлы на `cafe.adriel.voyager`, `koinScreenModel`, `screenModelScope`, `TabNavigator` и старые названия моделей; устранить оставшиеся использования.
- [ ] Не удалять несвязанные демо-файлы и шаблон `feature:add_card` в рамках этой миграции.
- [ ] Создать `docs/MVI_NAVIGATION.md` с коротким актуальным образцом: маршрут → destination → Koin ViewModel с владельцем entry → State/Intent/SideEffect → callback навигации.
- [ ] В соглашении закрепить `viewModelScope`, `SavedStateHandle`, обработку эффектов в активной записи и отсутствие обязательного базового класса.
- [ ] В `MASTER_PLAN.md` добавить ссылки на этот план и новое соглашение; отразить выбранное исключение для жеста изменённой формы.
- [ ] В документах шагов 0–2 сохранить историю Voyager. Добавить ссылку на актуальное соглашение, не переписывая исторические примеры.
- [ ] Ручные критерии готовности в мастер-плане не отмечать выполненными без подтверждения разработчика.

**Готово, когда:** production-код больше не зависит от Voyager, а следующую фичу можно писать по одному актуальному примеру.

### 3.8. Короткая ручная приёмка разработчиком

Автотесты и специальные проверочные механизмы не создавать. Агенту не запускать сборки, тесты и приложения без отдельной просьбы.

- [ ] **Табы:** стартует Tasks; Menu → About → Decks → Menu возвращает на About. Повторный тап Menu стек не меняет. Прокрутка списка сохраняется.
- [ ] **CRUD:** создание и редактирование открывают правильный режим формы; сохранение возвращает в список; удаление через существующий sheet работает.
- [ ] **Черновик:** ввод сохраняется после переключения табов и Android rotation.
- [ ] **iOS:** неизменённая форма и About следуют за пальцем; завершение возвращает назад, отмена оставляет экран открытым.
- [ ] **Android:** predictive back работает на поддерживаемом устройстве; обычный Back работает на остальных; на корнях табов Back передаётся системе.
- [ ] **Изменённая форма:** кнопка и завершённый жест открывают подтверждение; отменённый жест его не открывает; отмена подтверждения сохраняет ввод, подтверждение закрывает форму.
- [ ] **Слои:** Back сначала закрывает клавиатуру, открытый sheet или контекстное меню, без случайного выхода из формы.

Непроверенные сценарии оставить с пустыми галочками. После выполнения этого списка шаг 3 завершён; дальнейшая разработка карточек относится к шагу 4.
