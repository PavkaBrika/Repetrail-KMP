# Шаг 1 — Фундамент: Koin, Voyager, Room и MVI

## Цель и принятые решения

Подготовить инфраструктуру для следующих шагов: общий DI-граф, три таба с независимой навигацией, рабочая БД на Android и iOS и понятный шаблон разработки фич.

Документ для сохранения: `docs/STEP_1_FOUNDATION.md`.

С учётом уточнений:
- В Room сразу создаём **Deck + Flashcard** со связью между ними.
- Пользовательский CRUD, репозитории и use cases остаются шагам 2–3.
- Используем **Voyager ScreenModel** для MVI и штатную интеграцию с Koin; экспериментальный AndroidX KMP lifecycle bridge удалён.
- MVI описываем как соглашение с примером, без обязательного базового класса.

## Task list

### 1.1. Подготовить зависимости и проверить совместимость

- [ ] Зафиксировать исходное состояние: Android собирается, iOS-приложение запускается.
- [x] Добавить в version catalog Koin, Voyager, Room, SQLite Bundled, KSP и coroutines.
- [x] Для Voyager подключить navigator, tab navigator, screenmodel и koin одной версии.
- [ ] Проверить совместимость выбранных версий с текущими Kotlin `2.4.21`, AGP `9.1.1` и Compose Multiplatform `1.12.2`.
- [x] Зафиксировать конкретные версии без динамических диапазонов; выбрать ScreenModel без экспериментального lifecycle bridge.
- [ ] Проверить разрешение зависимостей для Android, `iosArm64` и `iosSimulatorArm64`.

**Готово, когда:** зависимости разрешаются на всех существующих таргетах без изменения выбранного стека.

#### Результат реализации 1.1

Изменения зависимостей прошли ревью и сохранены коммитом `3a78409`; проверки совместимости и разрешения зависимостей остаются открытыми. По просьбе пользователя дальнейшие сборки и запуски приложений пропущены.

| Компонент | Зафиксированная версия |
|---|---|
| Koin (core, Android, Compose) | `4.2.2` |
| Voyager (navigator, tab navigator, screenmodel, koin) | `2.2.21-1.10.3` |
| Room (runtime, compiler, Gradle plugin) | `2.8.5` |
| SQLite Bundled | `2.7.1` |
| KSP | `2.3.13` |
| Coroutines (core, test) | `1.11.0` |

Версии получены через Android Studio `studio_version_lookup`. Kotlin `2.4.21`, AGP `9.1.1` и Compose Multiplatform `1.12.2` сохранены. Наличие версии в lookup не подтверждает совместимость всего графа: она требует Gradle resolution и компиляции, которые пока не выполнялись после изменений.

Koin, Voyager и coroutines подключены к `shared/commonMain`. Room, SQLite, Koin Android и coroutines test пока объявлены в каталоге для подключения в соответствующих этапах. Плагины Room и KSP объявлены в корневом build script с `apply false`; применение к `core:data` и KSP-конфигурации таргетов относятся к 1.3.

Выбран штатный [Voyager ScreenModel](https://voyager.adriel.cafe/screenmodel/) с `screenModelScope` и [интеграцией Koin](https://voyager.adriel.cafe/screenmodel/koin-integration/). Экспериментальная интеграция AndroidX KMP ViewModel больше не используется.

Фактически выполненные проверки до изменения зависимостей:
- `git status --short` — рабочее дерево чистое.
- `:androidApp:assembleDebug` через `studio_gradle_build` — успешно.
- Запуск iOS не подтверждён; сборки, Gradle sync и проверка разрешения зависимостей после изменений пропущены по просьбе пользователя.

### 1.2. Создать core-модули

- [x] Добавить `core:common`, `core:domain`, `core:data` в Gradle settings.
- [x] Настроить Android и оба iOS-таргета по существующему образцу KMP-модулей проекта.
- [x] Подключить зависимости:
  - `core:domain → core:common`;
  - `core:data → core:domain + core:common`;
  - `shared → новые core-модули`.
- [x] В `core:common` добавить источник текущего времени. По итогам ревью `AppDispatchers` удалён: нужный `CoroutineDispatcher` передаётся напрямую через конструктор.
- [x] В `core:domain` зафиксировать правила: отсутствие Compose, Room, Koin и платформенных API.
- [x] Не добавлять заранее пустые интерфейсы репозиториев и use cases: их контракты определить вместе с CRUD.

**Готово, когда:** модули компилируются на обеих платформах, зависимости не образуют циклов.

#### Результат реализации 1.2

Добавлены три KMP-модуля с Android, `iosArm64` и `iosSimulatorArm64`. Android namespace, SDK и JVM 11 настроены по образцу существующего `core:ui:designsystem`. Собственные framework binaries не нужны: модули будут входить в приложение через `shared`.

По просьбе пользователя `AppDispatchers` удалён. Классы принимают нужный `CoroutineDispatcher` напрямую через конструктор. Зависимость coroutines из `core:common` удалена: текущему коду этого модуля она не нужна.

`TimeProvider` — внедряемый интерфейс текущего Unix-времени в миллисекундах. `SystemTimeProvider` использует `kotlin.time.Clock.System`; тесты могут передать фиксированную реализацию. Регистрация источника времени в Koin относится к 1.4.

В `core/domain/README.md` зафиксированы границы domain. `core:data` пока содержит только конфигурацию модуля; Room добавляется на этапе 1.3.

Граф проектных зависимостей проверен по build scripts: `shared → data/domain/common`, `data → domain/common`, `domain → common`; обратных ссылок нет. `git diff --check` выполнен без ошибок. Компиляция и Gradle sync пропущены по просьбе пользователя; критерий сборки на обеих платформах пока не подтверждён.

### 1.3. Настроить Room KMP

- [x] В `core:data` подключить Room runtime, compiler через KSP и Bundled SQLite.
- [x] Настроить KSP отдельно для Android и обоих iOS-таргетов.
- [x] Создать `RepetrailDatabase`, версия схемы — `1`.
- [x] Настроить генерацию конструктора БД через `RoomDatabaseConstructor`.
- [x] Включить экспорт схем и хранение JSON-схем в Git.

Схема первой версии:

| Entity | Поля |
|---|---|
| `DeckEntity` | `id: Long`, `name: String`, `description: String?`, `createdAt: Long`, `updatedAt: Long` |
| `FlashcardEntity` | `id: Long`, `deckId: Long`, `term: String`, `definition: String`, `example: String?`, `createdAt: Long` |

- [x] Для обеих таблиц использовать автоматически генерируемый первичный ключ.
- [x] Добавить внешний ключ `Flashcard.deckId → Deck.id`, индекс по `deckId` и каскадное удаление карточек.
- [x] Хранить timestamps в миллисекундах Unix.
- [x] Создать минимальные DAO: вставка, чтение по ID, наблюдение списка через `Flow`, удаление.
- [x] Реализовать Android builder с application context и постоянным путём БД.
- [x] Реализовать iOS builder с постоянным путём внутри sandbox приложения.
- [x] Общую конфигурацию выполнять в `commonMain`: Bundled SQLite и coroutine context для запросов. Основа — [официальная схема настройки Room KMP](https://developer.android.com/kotlin/multiplatform/room).
- [x] Не заполнять пользовательскую БД тестовыми данными при запуске.
- [x] Не включать destructive migration.

**Готово, когда:** на Android и iOS можно записать колоду с карточкой и прочитать их после закрытия и повторного открытия БД.

**Миграции:** предыдущей схемы пока нет. На шаге 1 создаём и сохраняем v1; реальную миграцию добавляем при первом изменении схемы, без искусственной версии v2.

#### Результат реализации 1.3

В `core:data` подключены Room runtime, SQLite Bundled и coroutines, плагины Room/KSP. Компилятор Room подключён к `kspAndroid`, `kspIosArm64`, `kspIosSimulatorArm64`. Runtime и coroutines объявлены через `api`, поскольку Room builder, базовый класс БД, Flow и CoroutineDispatcher входят в публичные сигнатуры.

Добавлены таблицы `decks` и `flashcards` с автоматически генерируемыми Long ID. Связь по `deckId` индексирована, удаление колоды каскадно удаляет её карточки. Вставка использует стандартный ABORT при конфликте. DAO предоставляют вставку с возвратом ID, чтение по ID, Flow списка (карточки — по колоде) и удаление по ID. Сортировка включает ID для стабильного порядка при одинаковых timestamps.

`RepetrailDatabase` имеет версию 1 и `exportSchema = true`; actual-конструктор генерирует Room. Android builder использует application context и `getDatabasePath("repetrail.db")`. iOS builder использует Documents внутри sandbox приложения. Общая фабрика принимает `CoroutineDispatcher` напрямую, настраивает Bundled SQLite и query coroutine context. Демо-данные и destructive migrations не добавлены.

Экспорт схем настроен в `core/data/schemas`. После сборки пользователем появился `com.breckneck.core.data.local.RepetrailDatabase/1.json`: проверены таблицы, autoGenerate ID, nullable-поля, индекс по deckId и CASCADE. Схема добавлена в Git.

Проверки: `git diff --check` — без ошибок. Компиляция, KSP и проверки записи/чтения, переоткрытия, внешнего ключа и каскадного удаления не выполнялись. IDE пока сообщает unresolved references для новых зависимостей: после изменения Gradle конфигурации sync не запускался. Регистрация БД и DAO и запуск приложения с ними относятся к 1.4.

### 1.4. Собрать DI-граф

- [x] Создать общий `initKoin` в `shared`, принимающий платформенную конфигурацию.
- [x] Разделить регистрацию на common-, data- и platform-модули.
- [x] Зарегистрировать источник времени, database builder, singleton БД и DAO. Нужный `CoroutineDispatcher` передавать напрямую без `AppDispatchers`.
- [x] Запускать Koin на Android из `Application` до создания UI.
- [x] Инициализировать Koin на iOS до создания общего Compose UI.
- [x] Исключить повторную инициализацию при пересоздании Activity или UIViewController.
- [x] Сохранить работу previews без запуска production DI-графа.
- [ ] Проверить реальное получение БД и DAO из контейнера на обеих платформах: одного успешного `startKoin` недостаточно.

**Готово, когда:** весь зарегистрированный граф разрешается без ошибок, БД создаётся один раз на контейнер.

#### Результат реализации 1.4

Композиция DI-графа находится в `shared/di`: `commonModule` регистрирует `TimeProvider`, `dataModule` — singleton БД и обоих DAO, платформенные модули — Room builder. `core:data` не зависит от Koin. `Dispatchers.IO` передаётся общей фабрике БД напрямую. При закрытии Koin-контейнера БД закрывается через `onClose`.

Общий `initKoin(platformConfiguration)` принимает конфигурацию платформы. Koin core объявлен через `api`, поскольку типы Koin присутствуют в публичном контракте инициализатора. Koin Android подключён только в `androidMain`.

На Android `RepetrailApplication.onCreate` вызывает `initKoinAndroid` с application context; Application зарегистрирован в manifest. Activity не инициализирует DI повторно. На iOS `MainViewController` вызывает `initKoinIos` до создания ComposeUIViewController. Инициализация защищена `lazy`: повторное создание UIViewController использует уже запущенный контейнер.

Compose-экраны и Android preview не запускают production DI. Регистрации ленивые: создание контейнера не открывает БД на главном потоке.

Добавлены `FoundationGraphAndroidTest` (instrumentation) и `FoundationGraphIosTest` с общей проверкой графа. Тесты используют локальный `koinApplication`, получают production platform builder, затем подменяют builder на in-memory перед созданием БД. Проверяются получение источника времени, builder, singleton БД и DAO, а также реальные запросы через оба DAO; production БД при этом не открывается. Тесты закрывают контейнер в `finally`.

Проверка `git diff --check` прошла. Сборки, Gradle sync, запуск тестов и приложений пропущены по просьбе пользователя. Пункт проверки реального разрешения на обеих платформах остаётся открытым до запуска тестов; успешность запуска и lifecycle не подтверждены выполнением.

Пользователь обновил Gradle wrapper до `9.8.2`; properties, wrapper JAR и скрипты сохранены вместе с изменениями 1.4. IDE сообщила об успешной задаче обновления wrapper. Это не подтверждает запуск приложений или тестов.

### 1.5. Перевести табы на Voyager

- [x] Заменить `remember(selectedTab)` и `when(selectedTab)` на `TabNavigator` и `CurrentTab`.
- [x] Создать табы Tasks, Cards, Menu; начальный таб — Tasks.
- [x] Сохранить текущие экраны, тему, floating navigation bar и обработку insets.
- [x] Создать отдельный вложенный `Navigator` внутри каждого таба — по [схеме Voyager](https://voyager.adriel.cafe/navigation/tab-navigation/).
- [x] Использовать стабильные ключи экранов и навигаторов.
- [x] Для проверки вложенной навигации добавить простой экран «О приложении», доступный из Menu.
- [x] Сохранять стек и UI-состояние таба при переключении.
- [x] Повторное нажатие выбранного таба не должно сбрасывать его стек.
- [x] Android Back сначала закрывает вложенный экран; на корне остаётся стандартное поведение Activity.
- [x] На iOS добавить явную кнопку возврата на вложенном экране.
- [ ] Проверить сценарий: Menu → О приложении → Cards → Menu — открыт прежний вложенный экран.

**Готово, когда:** три таба и вложенные переходы работают на Android и iOS без дублирования экранов.

#### Результат реализации 1.5

`MainTab` реализует Voyager `Tab`: Tasks, Cards и Menu имеют стабильные ключи и собственные вложенные `Navigator`. Внешний `TabNavigator` начинает с Tasks, а `CurrentTab` сохраняет saveable UI-состояние таба. Явно задано `disposeNestedNavigators = false`, чтобы переключение таба не освобождало вложенный стек; `TabDisposable` регистрирует освобождение табов при окончательном выходе из корневой навигации. Повторное нажатие выбранного таба не меняет current.

Существующие Tasks/Cards/Menu обёрнуты в Voyager Screen со стабильными ключами. Тема, floating navigation bar и верхние/боковые safeDrawing insets сохранены. Menu и «О приложении» прокручиваются с нижним отступом под floating bar и navigation bar.

Menu открывает `AboutScreen` в собственном стеке. Повторный быстрый вызов перехода не добавляет дубликат, если корневой экран уже сменился. Явная кнопка «Назад» доступна на обеих платформах. Android Back обрабатывает вложенный Navigator; при одном корневом экране обработчик Voyager не перехватывает Back.

IDE inspections изменённых файлов не выявили ошибок. `git diff --check` — без ошибок. Сборки, previews (требуют компиляции), UI-тесты и запуск приложений пропущены по просьбе пользователя. Пункты выше отмечают реализованное поведение; его runtime-проверка, включая восстановление после поворота, остаётся открытой.

Сценарии для проверки:
- Menu → О приложении → Cards → Menu: остаётся «О приложении».
- Повторное нажатие Menu на вложенном экране: стек сохраняется.
- Android Back или явная кнопка «Назад»: возврат в Menu; Back на корне — стандартное поведение Activity.
- Прокрутить Cards, переключить таб и вернуться: позиция прокрутки сохраняется.
- Повернуть Android на вложенном экране: выбранный таб и экран сохраняются.

Пользователь подтвердил работу навигации на Android и iOS. Это ручная проверка пользователя; поворот экрана и отдельные сценарии состояния не считаются автоматически проверенными.

Соглашение MVI и время жизни ScreenModel описаны в 1.6.

### 1.6. Зафиксировать MVI и lifecycle ScreenModel

- [x] Описать контракт фичи: immutable `State`, `Intent`, `SideEffect`, `ScreenModel`.
- [x] Зафиксировать интерфейс модели: `StateFlow<State>`, `Flow<SideEffect>`, `onIntent(intent)`.
- [x] Использовать приватный `MutableStateFlow` и публичный read-only state.
- [x] Для эффектов использовать `Channel` с `receiveAsFlow`; не хранить их в State.
- [x] Зафиксировать: UI передаёт intents, модель выполняет операции в `screenModelScope`, UI обрабатывает навигационные эффекты.
- [x] Описать загрузку, ошибку и Retry; отмену корутин не превращать в пользовательскую ошибку.
- [x] Получать модель через `koinScreenModel()` внутри `Screen.Content()`; регистрировать в Koin как factory.
- [x] Использовать `voyager-screenmodel` и `voyager-koin` одной версии с navigator и tab navigator.
- [x] Удалить экспериментальный lifecycle provider, opt-in и прямые зависимости для AndroidX ViewModel.
- [ ] Проверку lifecycle модели при recomposition, переключении табов и окончательном pop отложить по просьбе пользователя; тесты не добавлять.

**Готово, когда:** следующую фичу можно реализовать по документированному образцу.

#### Соглашение MVI

Обязательного базового класса нет. Каждая фича определяет собственные:

| Часть | Контракт |
|---|---|
| State | immutable data class; все поля val; данные для UI, загрузка, ошибка |
| Intent | sealed interface пользовательских действий |
| SideEffect | sealed interface одноразовых команд UI: навигация, snackbar |
| ScreenModel | Voyager ScreenModel с StateFlow<State>, Flow<SideEffect> и onIntent(intent) |

Модель владеет приватным `MutableStateFlow`, наружу отдаёт `asStateFlow()`. Коллекции внутри State также не изменяются на месте. UI рисует State и передаёт Intent, операции выполняются в `screenModelScope`. Диспетчер для блокирующей работы внедряется напрямую либо контекст переключает нижележащий слой. Модель не хранит Navigator, Context или Composable.

Для SideEffect используется приватный `Channel(Channel.BUFFERED)` и публичный `receiveAsFlow()`. У потока один UI-потребитель; несколько коллекторов разделят события между собой. Событие может ждать UI в буфере, переполнение при `send` приостанавливает отправителя. Это не гарантия обработки после сбоя процесса или отмены обработчика: критичные результаты отражаются в State/БД. Полученный эффект не проигрывается новому подписчику повторно.

Перед загрузкой выставляются `isLoading = true, error = null`. При успехе обновляются данные и снимается loading. При ошибке сохраняются предыдущие данные, выставляется понятная ошибка и доступен Retry. Retry запускает ту же операцию. Повторная загрузка отменяет предыдущий Job. `CancellationException` всегда пробрасывается.

Пример хранится только в документе; production-экраны пока сохраняют демо-контент:

```kotlin
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ExampleState(
    val count: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null,
)

sealed interface ExampleIntent {
    data object Refresh : ExampleIntent
    data object Retry : ExampleIntent
    data object OpenDetails : ExampleIntent
}

sealed interface ExampleSideEffect {
    data object NavigateToDetails : ExampleSideEffect
}

class ExampleScreenModel(
    private val loadCount: suspend () -> Int,
) : ScreenModel {
    private val mutableState = MutableStateFlow(ExampleState())
    val state: StateFlow<ExampleState> = mutableState.asStateFlow()

    private val effects = Channel<ExampleSideEffect>(Channel.BUFFERED)
    val sideEffects: Flow<ExampleSideEffect> = effects.receiveAsFlow()
    private var loadJob: Job? = null

    fun onIntent(intent: ExampleIntent) {
        when (intent) {
            ExampleIntent.Refresh, ExampleIntent.Retry -> refresh()
            ExampleIntent.OpenDetails -> screenModelScope.launch {
                effects.send(ExampleSideEffect.NavigateToDetails)
            }
        }
    }

    private fun refresh() {
        loadJob?.cancel()
        loadJob = screenModelScope.launch {
            mutableState.update { it.copy(isLoading = true, error = null) }
            try {
                val count = loadCount()
                mutableState.update { it.copy(count = count, isLoading = false) }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                mutableState.update {
                    it.copy(isLoading = false, error = "Не удалось загрузить данные")
                }
            }
        }
    }

    override fun onDispose() {
        effects.close()
    }
}
```

#### ScreenModel, Koin и Voyager

[ScreenModel](https://voyager.adriel.cafe/screenmodel/) — штатный интерфейс модели экрана Voyager. Создание и хранение модели привязаны к ключу Screen. Рекомпозиция не создаёт новую модель; при окончательном удалении экрана Voyager вызывает `onDispose()` и отменяет `screenModelScope`. Смена табов при сохранении вложенных навигаторов не должна освобождать модель. Сам объект модели не сохраняется на диск: восстановление навигации после смерти процесса не означает восстановления всех данных из памяти модели.

[Интеграция Koin](https://voyager.adriel.cafe/screenmodel/koin-integration/) использует factory и `koinScreenModel()`:

```kotlin
import org.koin.dsl.module
import cafe.adriel.voyager.koin.koinScreenModel

val exampleModule = module {
    // В реальной фиче сюда внедряется операция загрузки.
    factory { ExampleScreenModel(loadCount = { 42 }) }
}

// Внутри Screen.Content():
val screenModel = koinScreenModel<ExampleScreenModel>()
```

Не создавать модель через обычный `remember` и не вызывать Koin `get` на каждой рекомпозиции: они не обеспечивают правильное владение со стороны Voyager. Не регистрировать модель как singleton. Для параметризованных экранов нужен стабильный уникальный key маршрута. Для нескольких моделей одного типа в пределах Screen используется tag. Navigator-scoped модель выбирается только когда действительно нужна общая модель нескольких экранов.

Пример подключения UI внутри Screen.Content:

```kotlin
// Compose imports: collectAsState, getValue, LaunchedEffect.
val screenModel = koinScreenModel<ExampleScreenModel>()
val state by screenModel.state.collectAsState()
val navigator = LocalNavigator.currentOrThrow

LaunchedEffect(screenModel, navigator) {
    screenModel.sideEffects.collect { effect ->
        when (effect) {
            ExampleSideEffect.NavigateToDetails -> navigator.push(AboutScreen)
        }
    }
}
// UI использует state и обработчик screenModel::onIntent.
// Первую загрузку можно запускать из init модели;
// не отправлять Refresh прямо из тела Composable.
```

Здесь UI-подписки существуют, пока экран находится в composition. При смене видимого экрана `LaunchedEffect` отменяется, но работа самой модели в `screenModelScope` продолжается до её disposal. Это не автоматическая приостановка работы при уходе приложения в фон; такую потребность решаем отдельно в конкретной фиче.

UI-функция принимает State и callbacks. Preview получает обычный State и пустые callbacks без production Koin и модели.

#### Результат 1.6

По решению пользователя вместо AndroidX KMP ViewModel выбран Voyager ScreenModel. В `shared` подключены `voyager-screenmodel` и `voyager-koin` версии `2.2.21-1.10.3`. Прямые зависимости lifecycle-kmp, Koin Compose ViewModel и AndroidX lifecycle Compose удалены. Стандартные AndroidX lifecycle-зависимости могут оставаться транзитивно у Android-компонентов.

Из `MainMenuScreen` удалены `ProvideNavigatorLifecycleKMPSupport`, experimental opt-in и вспомогательная обёртка. Навигация и её стеки сохранены. В production пока нет моделей состояния: соглашение выше применяется с первой фичи.

Тестовый MVI-пример и harness удалены пользователем, их не восстанавливаем. Сборки, Gradle sync и тесты не запускаются по его просьбе. Совместимость новых зависимостей и runtime lifecycle остаются непроверенными. Версии новых модулей проверены через `studio_version_lookup`, API сверены с документацией Voyager.

## Проверка и критерии завершения

- [ ] Android-приложение собирается и запускается.
- [ ] iOS framework компилируется для устройства и симулятора; приложение запускается в симуляторе.
- [ ] DI-тесты разрешают все зарегистрированные зависимости на обеих платформах.
- [ ] Room-тесты проверяют запись/чтение, сохранность после переоткрытия, внешний ключ и каскадное удаление.
- [ ] MVI-тест проверяет изменение State и однократную доставку SideEffect.
- [ ] Проверены независимые стеки табов, возврат назад и lifecycle ScreenModel.
- [ ] На Android после поворота сохраняются выбранный таб и вложенный экран.
- [ ] После перезапуска процесса приложение открывается на Tasks, данные Room сохраняются.
- [x] В документе записаны фактически выполненные команды и результаты проверок.
- [x] В `MASTER_PLAN.md` отмечены выполненные критерии шага 1 и уточнено, что схема Deck + Flashcard подготовлена заранее.

## Итог реализации и отложенные проверки

Реализация пунктов 1.1–1.6 сохранена по итогам ревью:
- `3a78409` — зависимости;
- `c99c134` — core-модули и источник времени, без AppDispatchers;
- `aeba29b` — Room KMP;
- `f95d0b0` — DI, пользовательское обновление Gradle до 9.8.2 и экспортированная схема v1;
- `7dba371` — независимые стеки табов, включая правки пользователя;
- `cefb0cb` — первоначальный вариант MVI с AndroidX KMP lifecycle; затем заменён на ScreenModel по решению пользователя.

Пользователь подтвердил работу навигации на Android и iOS. Агент ранее выполнил `:androidApp:assembleDebug` успешно до изменения зависимостей; последующие сборки и тесты не запускались по просьбе пользователя. Схема Room появилась после пользовательской сборки. IDE inspections production-навигации не выявили ошибок; это не заменяет runtime-проверку.

Финальная статическая сверка: три core-модуля связаны без циклов; Room имеет обе сущности, DAO, builders и схему; DI инициализируется вне composable-экранов; навигация использует отдельные стеки; MVI и получение экранной ScreenModel описаны в документе. `git diff --check` выполнен без ошибок.

По просьбе пользователя новые тесты не добавляем и не восстанавливаем удалённые. Проверки Room/DI, lifecycle ScreenModel, Android rotation и восстановления процесса остаются открытыми. Поведение «после перезапуска процесса всегда Tasks» отдельно не проверено: стандартное восстановление Voyager может вернуть сохранённую навигацию, поэтому это требование не отмечено выполненным. Инициализация DI и успешный запуск UI сами по себе не подтверждают операции БД.

Состояние: инфраструктурный код подготовлен, приёмочные проверки отложены. Следующий этап Master Plan — CRUD колод (шаг 2); он не входит в данный документ.

## Границы шага

CRUD-интерфейсы, репозитории, бизнес-модели, уроки и Leitner реализуются на следующих шагах. Демо-контент существующих экранов пока сохраняется. Шаблонный `feature:add_card` на этом шаге не перерабатывается.

Порядок выполнения: **зависимости → модули → Room → DI → навигация → MVI/lifecycle → итоговая проверка**.
