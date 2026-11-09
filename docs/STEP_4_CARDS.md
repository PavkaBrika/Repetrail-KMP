# Шаг 4 — Карточки (CRUD): подробный план реализации

> **Дата создания:** 2026-11-09
> **Основание:** [MASTER_PLAN.md](MASTER_PLAN.md), шаг 4; [MVI_NAVIGATION.md](MVI_NAVIGATION.md).
> **Статус:** план реализации; пункты ниже ещё не выполнены.
> **Формат работы:** последовательно реализовать описанную функциональность. В план не входят ручные проверки, остановки для одобрения разработчиком, написание тестов и новая тестовая инфраструктура.

## 1. Что должно получиться

В табе колод пользователь нажимает на колоду и открывает её детали: название, описание, количество карточек и список. Из деталей можно добавить карточку, открыть существующую для редактирования, удалить карточку, изменить или удалить саму колоду.

Форма карточки содержит термин, определение и необязательный пример. Обычное сохранение возвращает в детали колоды. «Сохранить и добавить ещё» создаёт карточку, очищает форму и оставляет пользователя на ней.

Вся функциональность реализуется в общем KMP-коде для Android и iOS. Данные хранятся в существующей Room-базе; интерфейс обновляется через Flow.

### Границы шага

- Реализовать CRUD карточек и интеграцию с существующим CRUD колод.
- Создать модуль `feature:card`; оставить `feature:deck` владельцем списка и формы колоды.
- Использовать текущие Compose Navigation, AndroidX ViewModel, Koin и MVI. Не возвращать Voyager и не переходить на Navigation 3.
- Сохранить существующие три таба, отдельные контроллеры и стеки, платформенные анимации переходов.
- Сохранить Room v1: таблица карточек уже содержит все необходимые поля. Новые DAO-методы не требуют миграции.
- Урок, Leitner, XP, CSV, поиск, сортировки пользователем, массовое редактирование и перенос карточек между колодами в этот шаг не входят.
- Не добавлять кнопку запуска урока с пустым обработчиком: её реализация относится к шагу 5.
- Не добавлять поля `box`, `nextReviewAt`, `lastReviewedAt`, статусы и бейджи Leitner: это шаг 7.
- Не исправлять попутно все старые экраны и не менять зависимости/toolchain без необходимости.

### Уточнения к общему user flow

[USER_FLOW.md](USER_FLOW.md) описывает также будущие шаги. Для шага 4 действуют следующие конкретные решения:

| Вопрос | Решение |
|---|---|
| Статистика в деталях | «Всего: N» и «Новые: N». Пока уроков и повторений нет, все карточки новые. |
| «На повторение» и «Выучены» | Не показывать до шага 7; не подставлять выдуманные значения. |
| Порядок карточек | `createdAt ASC, id ASC`, как в существующем DAO. |
| Одинаковые термины | Разрешены; уникальность термина не вводить. |
| Меню карточки | Долгое нажатие на строку открывает стандартное dropdown-меню «Редактировать / Удалить». Обычный тап открывает редактирование. |
| Удаление карточки | По выбору «Удалить» в dropdown-меню сразу удалить из БД; без дополнительного подтверждения, snackbar и undo. |
| Удаление колоды | Существующее подтверждение и немедленное каскадное удаление; undo для колоды не добавлять. |
| Изменение карточек и дата колоды | Не менять `Deck.updatedAt` при CRUD карточек. Существующая сортировка колод сохраняется. |
| Нижняя панель табов | Остаётся видимой на деталях и форме. На форме с открытой клавиатурой панель скрывается; стеки табов при этом сохраняются. |
| Черновик формы | Живёт в ViewModel записи стека. Переключение таба и Android rotation его сохраняют; восстановление черновика после уничтожения процесса не добавлять. |

## 2. Состояние проекта, на которое опирается план

Все пути далее относительны корню репозитория.

| Уже есть | Где и что использовать |
|---|---|
| Таблица карточек | `core/data/.../local/entity/FlashcardEntity.kt`: id, deckId, term, definition, example, createdAt; FK на колоду, индекс deckId, CASCADE. |
| Базовый DAO карточек | `FlashcardDao.kt`: insert, getById, observeByDeckId, deleteById; обновления пока нет. |
| Реактивные счётчики колод | `DeckDao.observeAllWithCardCount()` → `DeckRepositoryImpl` → `ObserveDecksUseCase` → список колод. |
| Domain колод | `core/domain/.../model/Deck.kt`, `repository/DeckRepository.kt`, `usecase/deck/*`. |
| Список и форма колоды | `feature/deck`, включая готовые `DeckFormRoute` и `DeleteDeckUseCase`. |
| Заготовка перехода в детали | `DeckListSideEffect.NavigateToDeckDetail(deckId)` уже отправляется; в `DecksScreen.kt` обработчик пока пустой. |
| Навигационная оболочка | `shared/.../ui/navigation/MainMenuScreen.kt`: отдельный NavHost каждого таба. |
| Привязка ViewModel | `DeckDestinations.kt`: `koinViewModel` с владельцем entry и его extras. |
| Обработка выхода из формы | `DeckFormScreen.kt`: NavigationBackHandler, учёт IME, подтверждение discard. |
| DI | `shared/.../di/DataModule.kt`, `InitKoin.kt`, `feature/deck/.../di/DeckModule.kt`. |
| Оформление | `RepetrailTheme` и токены в `core:ui:designsystem`; правила bubble UI в skills проекта. |

Важные особенности текущего кода:

- Файлы `DeckListScreenModel.kt` и `DeckFormScreenModel.kt` уже содержат классы `DeckListViewModel` и `DeckFormViewModel`. Ориентироваться на актуальные классы, а не старые имена файлов.
- Не копировать поведение, при котором ошибка загрузки формы превращается в пустую доступную для сохранения форму. У формы карточки будут отдельные Loading, Error и NotFound.
- Не копировать фиксированные нижние отступы `84.dp` и `112.dp` из старых экранов. Новые экраны используют измеренную геометрию панели.
- Компоненты из `shared/ui/components` недоступны фичам через обратную зависимость. Не добавлять `feature:card → shared`.
- Счётчик карточек на экране колод уже подключён к Room. Не создавать второй счётчик в памяти и не обновлять его вручную после операций.

## 3. Порядок реализации

Выполнить последовательно: **4.1 → 4.2 → 4.3 → 4.4 → 4.5 → 4.6 → 4.7 → 4.8 → 4.9 → 4.10 → 4.11**.

Внутри каждого пункта сначала создать контракты и состояние, затем реализацию. Галочки отмечать только после реализации соответствующего пункта. Не останавливаться между пунктами для согласований.

### 4.1. Создать модуль и каркас файлов

- [ ] Создать `feature/card/build.gradle.kts` на основе `feature/deck/build.gradle.kts`.
- [ ] Сохранить те же плагины KMP, Android Multiplatform Library, Compose, Compose Compiler и Serialization.
- [ ] Установить namespace `com.breckneck.feature.card`, Android SDK/JVM-настройки взять из модуля колод.
- [ ] Добавить те же production-зависимости для Compose, ресурсов, Navigation, Lifecycle, Navigation Event, Serialization, Koin и coroutines.
- [ ] Подключить `core:domain` и `core:ui:designsystem`; прямую зависимость на `core:data` не добавлять.
- [ ] Использовать текущие aliases version catalog; не вводить другие версии библиотек.
- [ ] Объявить targets `iosArm64`, `iosSimulatorArm64` и Android по образцу `feature:deck`.
- [ ] В `settings.gradle.kts` добавить `include(":feature:card")`.
- [ ] В `shared/build.gradle.kts` добавить `implementation(project(":feature:card"))`.
- [ ] Не переносить новый код в шаблон `feature:add_card`. Его удаление и переименование не нужны для CRUD.

Структура нового модуля; Kotlin-пути ниже находятся внутри `src/commonMain/kotlin/com/breckneck/feature/card/`:

```text
di/CardModule.kt
navigation/CardRoutes.kt
navigation/CardDestinations.kt
detail/DeckDetailState.kt
detail/DeckDetailIntent.kt
detail/DeckDetailSideEffect.kt
detail/DeckDetailViewModel.kt
detail/screen/DeckDetailScreen.kt
detail/screen/DeckDetailContent.kt
detail/screen/CardListItem.kt
form/CardFormState.kt
form/CardFormIntent.kt
form/CardFormSideEffect.kt
form/CardFormViewModel.kt
form/screen/CardFormScreen.kt
form/screen/CardFormContent.kt
ui/CardScreenControls.kt
src/commonMain/composeResources/values/strings.xml
src/commonMain/composeResources/values-ru/strings.xml
```

Последние два пути относятся к корню модуля. Небольшие вспомогательные типы можно разместить рядом с соответствующим State; не создавать отдельный модуль ради общего кода двух экранов.

### 4.2. Добавить domain-модель и контракты операций

- [ ] В `core/domain/.../model/Card.kt` создать модель без Room-аннотаций:

```kotlin
data class Card(
    val id: Long = 0,
    val deckId: Long,
    val term: String,
    val definition: String,
    val example: String?,
    val createdAt: Long,
)
```

- [ ] В `core/domain/.../repository/CardRepository.kt` объявить контракт:

```kotlin
interface CardRepository {
    fun observeCards(deckId: Long): Flow<List<Card>>
    suspend fun getCardById(deckId: Long, cardId: Long): Card?
    suspend fun createCard(card: Card): Long
    suspend fun updateCard(card: Card): Boolean
    suspend fun deleteCard(deckId: Long, cardId: Long): Boolean
}
```

Семантика методов обязательна:

- `createCard` принимает только новую карточку с `id == 0L` и возвращает сгенерированный ID.
- `getCardById`, update и delete проверяют принадлежность карточки указанной колоде.
- `updateCard` возвращает true только при обновлении одной существующей записи. Отсутствующую запись не создаёт.
- `deleteCard` удаляет запись с указанными cardId и deckId. Возвращает true, если удалена одна строка, и false, если карточка уже отсутствует. Снимок удалённой записи не нужен.

- [ ] В `usecase/card/` создать `ObserveCardsUseCase`, `GetCardByIdUseCase`, `SaveCardUseCase`, `DeleteCardUseCase`. Последний возвращает Boolean из repository.
- [ ] Для `SaveCardUseCase` использовать вход `deckId, cardId: Long? = null, term, definition, example`; результат — ID сохранённой карточки.
- [ ] Добавить простые domain-ошибки `DeckNotFoundException` и `CardNotFoundException` для отсутствующих записей; не передавать в domain Compose-ресурсы или UI-строки.

Алгоритм `SaveCardUseCase`:

1. Проверить, что deckId положительный; ненулевой cardId также должен быть положительным.
2. Выполнить `trim()` у term и definition. Оба значения должны быть непустыми после trim.
3. Выполнить `example?.trim()?.ifBlank { null }`. Сохранять внутренние пробелы и переводы строк.
4. Через существующий `DeckRepository.getDeckById` убедиться, что колода существует; иначе выбросить `DeckNotFoundException`.
5. Для создания: взять время из `TimeProvider.currentTimeMillis()`, собрать Card с id = 0 и вызвать createCard.
6. Для редактирования: загрузить карточку через getCardById(deckId, cardId); при null выбросить `CardNotFoundException`.
7. Скопировать в найденную карточку только term, definition и example; сохранить исходные id, deckId и createdAt.
8. Вызвать updateCard; false также означает `CardNotFoundException`, а не повод сделать insert.
9. Вернуть ID только после завершения записи в БД.

Не вводить произвольные ограничения длины и уникальность текста. Валидация use case защищает domain-контракт; ошибки полей в форме реализуются отдельно.

### 4.3. Реализовать DAO, mapper и repository

- [ ] В `FlashcardDao.kt` сохранить существующие методы и дополнить их операциями, ограниченными одновременно id и deckId.
- [ ] Добавить `getByIdInDeck(deckId, cardId)` с условием `WHERE id = :cardId AND deckId = :deckId`.
- [ ] Добавить update-запрос, возвращающий число обновлённых строк:

```sql
UPDATE flashcards
SET term = :term, definition = :definition, example = :example
WHERE id = :cardId AND deckId = :deckId
```

Такой update не меняет createdAt и deckId и не создаёт новую запись.

- [ ] Добавить scoped delete-запрос с тем же условием id + deckId и результатом Int. Для удаления достаточно одного DELETE; предварительный SELECT и отдельная транзакция не нужны.
- [ ] Обычный insert создания оставить без IGNORE/REPLACE: unexpected failure должна доходить до вызывающего кода.
- [ ] Не использовать `INSERT OR REPLACE` и upsert для редактирования: это может затереть существующую запись.
- [ ] В `core/data/.../mapper/CardMapper.kt` добавить `FlashcardEntity.toDomain()` и `Card.toEntity()`; переносить все поля без новых значений времени.
- [ ] В `core/data/.../repository/CardRepositoryImpl.kt` реализовать domain-контракт поверх FlashcardDao. `observeCards` преобразует существующий Flow списка через mapper.
- [ ] `updateCard` возвращает `rowsUpdated == 1`; `deleteCard` возвращает `rowsDeleted == 1` из scoped DELETE.
- [ ] В repository не добавлять Snackbar, mutable UI state, задержки удаления и навигацию.
- [ ] Room suspend/Flow уже выполняют запросы через настроенный dispatcher базы. Не добавлять `runBlocking`, отдельные ручные потоки или `Dispatchers.Main` для БД.

Для реактивного названия и описания колоды:

- [ ] В `DeckDao` добавить `observeByIdWithCardCount(id): Flow<DeckWithCardCount?>`.
- [ ] Взять SQL из `observeAllWithCardCount`, добавить `WHERE d.id = :id` перед GROUP BY; сохранить LEFT JOIN, чтобы пустая колода тоже возвращалась.
- [ ] В `DeckRepository` добавить `observeDeckById(id): Flow<Deck?>`.
- [ ] В `DeckRepositoryImpl` реализовать его через новый DAO-запрос и существующий mapper.
- [ ] В `usecase/deck/` добавить `ObserveDeckByIdUseCase`.

Не менять `FlashcardEntity`, `DeckEntity`, version базы и JSON-схему. FK остаётся защитой от появления карточки без колоды, в том числе если колода исчезла между проверкой в use case и записью.

### 4.4. Подключить DI

- [ ] В `shared/.../di/DataModule.kt` зарегистрировать `single<CardRepository> { CardRepositoryImpl(flashcardDao = get()) }`.
- [ ] Использовать существующие singleton Room и FlashcardDao; вторую БД не создавать.
- [ ] В `feature/deck/.../di/DeckModule.kt` добавить factory для `ObserveDeckByIdUseCase`. Остальные регистрации use cases колод оставить там.
- [ ] В `feature/card/.../di/CardModule.kt` создать публичный `cardModule`: factories четырёх use cases карточек и регистрации двух ViewModel через `viewModel { ... }`.
- [ ] Обе ViewModel получают `SavedStateHandle` через Koin и зависимости use cases через конструктор.
- [ ] В `InitKoin.kt` подключить `cardModule` рядом с `deckModule`.

`DeckDetailViewModel` получает ObserveDeckByIdUseCase, ObserveCardsUseCase, DeleteCardUseCase и DeleteDeckUseCase. `CardFormViewModel` получает GetDeckByIdUseCase, GetCardByIdUseCase и SaveCardUseCase. Контроллер навигации в ViewModel не передавать.

### 4.5. Определить маршруты и связь модулей

- [ ] В `CardRoutes.kt` объявить публичные типизированные маршруты:

```kotlin
@Serializable
data class DeckDetailRoute(val deckId: Long)

@Serializable
data class CardFormRoute(val deckId: Long, val cardId: Long? = null)
```

- [ ] В `CardDestinations.kt` создать публичную функцию `NavGraphBuilder.cardDestinations(...)`.
- [ ] Передавать в неё NavHostController текущего таба, layout-параметры из пункта 4.8 и callback `onEditDeck: (Long) -> Unit`.
- [ ] Обе ViewModel создавать в composable соответствующего маршрута через `koinViewModel(viewModelStoreOwner = entry, extras = entry.defaultViewModelCreationExtras)`.
- [ ] Аргументы читать внутри ViewModel через `savedStateHandle.toRoute<...>()`. Не сериализовать Card/Deck или введённый текст в маршрут.
- [ ] В `DecksScreen` добавить callback `onNavigateToDeckDetail: (Long) -> Unit` и вызвать его в существующей ветке NavigateToDeckDetail.
- [ ] В `deckDestinations` принять этот callback и передать списку. `feature:deck` не должен импортировать маршруты `feature:card`.
- [ ] В Decks NavHost внутри `MainMenuScreen` зарегистрировать и deckDestinations, и cardDestinations.
- [ ] На уровне shared связать callback списка с `navigate(DeckDetailRoute(deckId))`; callback редактирования колоды — с `navigate(DeckFormRoute(deckId))`.
- [ ] При открытии деталей/форм использовать `launchSingleTop = true`. Не сбрасывать существующий стек и не создавать второй контроллер для карточек.

Ожидаемые стеки:

```text
DeckListRoute → DeckDetailRoute(deckId)
DeckListRoute → DeckDetailRoute(deckId) → CardFormRoute(deckId, null)
DeckListRoute → DeckDetailRoute(deckId) → CardFormRoute(deckId, cardId)
DeckListRoute → DeckDetailRoute(deckId) → DeckFormRoute(deckId)
```

После редактирования колоды существующая форма выполняет обычный pop; детали получают новое название/описание из Room Flow. После удаления колоды из деталей вернуться к уже существующему DeckListRoute через pop до него, не создавать новый список поверх старого.

Системный возврат неизменённых экранов оставить NavHost. Платформенные transition callbacks оболочки не переопределять внутри новых destinations.

### 4.6. Реализовать состояние и логику деталей колоды

- [ ] `DeckDetailViewModel` сделать обычным AndroidX ViewModel с приватным MutableStateFlow и публичным StateFlow.
- [ ] Состояние экрана разделить на `Loading`, `Content(deck, cards)`, `NotFound` и `Error`.
- [ ] Пустую колоду представлять как Content с пустым списком: её название, описание и меню остаются доступными.
- [ ] Дополнительно хранить состояние верхнего меню колоды, подтверждения удаления колоды, `isMutating` и `operationError`. Меню карточки — локальное UI-состояние из пункта 4.8; для удаления карточки отдельный диалог и снимок не нужны.
- [ ] Через combine объединить ObserveDeckByIdUseCase(deckId) и ObserveCardsUseCase(deckId).
- [ ] До первого результата показать Loading; deck == null означает NotFound, а не пустую колоду.
- [ ] В Content брать список из Flow, а значения «Всего» и «Новые» вычислять по `cards.size`.
- [ ] Ошибка чтения переводит экран в Error с локализованным сообщением и Retry.
- [ ] Retry отменяет старую подписку и запускает одну новую; повторные нажатия не создают параллельные collect.

Intents и их поведение:

| Intent | Действие |
|---|---|
| Retry | Перезапустить загрузку. |
| BackClicked | Одноразовый NavigateBack. |
| AddCardClicked | NavigateToCardForm с текущим deckId и cardId = null. |
| CardClicked(cardId) | Открыть редактирование карточки текущей колоды. |
| OpenMenu / DismissMenu | Изменить состояние меню. |
| EditDeckClicked | Закрыть меню и отправить NavigateToDeckForm(deckId). |
| DeleteDeckClicked | Закрыть меню и открыть подтверждение удаления колоды. |
| DeleteCardClicked(cardId) | Сразу удалить карточку текущей колоды из БД. |
| CancelDeleteDeck | Закрыть подтверждение удаления колоды без записи в БД. |
| ConfirmDeleteDeck | Выполнить удаление колоды. |

- [ ] SideEffect использовать для навигации.
- [ ] Ошибку записи хранить в operationError и показывать inline рядом с содержимым. Очищать её при начале следующей операции; не превращать Content в полноэкранную ошибку загрузки.
- [ ] Не показывать пользователю `exception.message`, SQL или stack trace.
- [ ] `CancellationException` всегда пробрасывать.
- [ ] На время записи блокировать повторные изменяющие операции. Флаг выставлять синхронно при принятии intent, до запуска coroutine, чтобы двойное нажатие не запустило две записи.

Удаление колоды из деталей:

1. В bottom sheet показать текущее название и количество карточек: «Колода „{name}“ и все её карточки ({N}) будут удалены. Это нельзя отменить».
2. После подтверждения закрыть sheet, выставить isMutating и вызвать существующий DeleteDeckUseCase(deckId).
3. Карточки удаляет FK CASCADE; не перебирать их отдельными delete.
4. При успехе отправить NavigateToDeckList.
5. При ошибке снять isMutating, сохранить Content и показать сообщение «Не удалось удалить колоду».
6. Пока выполняется собственное удаление колоды, не показывать промежуточный NotFound из её Flow поверх завершающегося экрана.

### 4.7. Реализовать удаление карточки

Карточка удаляется из БД сразу по выбору пункта «Удалить» в dropdown-меню, открытом долгим нажатием на строку. Дополнительное подтверждение, snackbar удаления и undo не нужны.

- [ ] В DeleteCardClicked(cardId) проверить, что экран находится в Content, карточка относится к текущей колоде и isMutating == false.
- [ ] Синхронно выставить isMutating до запуска coroutine и очистить operationError. Повторные нажатия во время записи игнорировать.
- [ ] Вызвать DeleteCardUseCase(deckId, cardId); он делегирует удаление repository с ограничением по обоим ID.
- [ ] При true ничего дополнительно не удалять из UI: строку уберёт Room Flow, он же обновит счётчики.
- [ ] При false запись уже отсутствует; считать это завершённой операцией без ошибки и дождаться актуальных данных из Flow.
- [ ] При исключении сохранить список из Flow и показать inline «Не удалось удалить карточку». Пользователь может повторно открыть меню удержанием на строке и выбрать «Удалить».
- [ ] После успеха или ошибки снять isMutating; CancellationException пробросить.
- [ ] Не хранить снимок удалённой карточки, token, таймер, pending deletion или очередь восстановления.
- [ ] Не добавлять RestoreCardUseCase, метод восстановления в repository и отдельные DAO-вставки для undo.
- [ ] Переход на другой экран или пересоздание UI не запускают повторное удаление: запись выполняется только по intent пользователя.

### 4.8. Реализовать UI деталей, меню карточек и layout-контракт

Новые экраны должны использовать edge-to-edge bubble UI. Два слоя: полноэкранный scroll-контент и независимые безопасно расположенные кнопки поверх него.

#### Геометрия между shared и feature

- [ ] В `MainMenuScreen` измерять фактическую видимую высоту FloatingNavigationBar, отдельно от её системных и внешних отступов.
- [ ] Передавать в destinations новых экранов готовый `bottomControlsPadding: Dp` — расстояние от нижнего края viewport до доступного нижнего края экранных действий.
- [ ] Когда панель видима, значение включает navigation bar inset, существующий внешний нижний отступ панели, измеренную высоту самой панели и тематический зазор над ней.
- [ ] На форме при открытой IME скрывать FloatingNavigationBar; `bottomControlsPadding` рассчитывать по верхней границе IME с тематическим зазором. IME и navigationBars учитывать через максимум, не сумму.
- [ ] Условие «форма карточки открыта» получать из currentBackStackEntry контроллера колод и типизированного CardFormRoute. На другие существующие экраны политику скрытия панели не распространять.
- [ ] Измерение панели не должно зависеть от переданного в feature padding: не создавать цикл layout.
- [ ] Новые feature-экраны получают готовую геометрию. Не повторять внутри неё navigationBarsPadding/imePadding и не вычислять высоту глобальной панели самостоятельно.
- [ ] Последняя строка и низ формы получают внутренний scroll-padding с учётом фактической высоты своих FAB/action bar/snackbar; viewport остаётся полноэкранным.
- [ ] SnackbarHost размещать выше нижних действий feature с тематическим зазором: он не закрывает ни FAB/action bar, ни глобальную панель. Для этого использовать ту же измеренную геометрию.
- [ ] Верхние и горизонтальные safe insets учитывать отдельно для controls и внутреннего отступа контента. Не накладывать safeDrawingPadding на весь NavHost.
- [ ] Контракт передавать параметрами composable; не создавать зависимость feature от shared или глобальный singleton размеров.

#### DeckDetailContent

- [ ] Вынести сбор StateFlow, эффекты и lifecycle в DeckDetailScreen; Content принимает только state, callbacks/intents, modifier и layout-параметры.
- [ ] Вверху start разместить отдельную круглую кнопку назад, в end — круглую кнопку «⋮».
- [ ] Название колоды и необязательное описание разместить в начале LazyColumn. Пустое описание не занимает строку.
- [ ] После названия вывести статистику «Всего» и «Новые», затем карточки.
- [ ] Крупный заголовок при уходе из видимой области плавно переводить в компактную верхнюю bubble между кнопками. Прогресс привязать к фактическому положению заголовка в списке.
- [ ] Не объявлять оба визуальных варианта заголовка одновременно для accessibility.
- [ ] Ограничить ширину компактного заголовка свободным местом между боковыми кнопками; длинное название не перекрывает их.
- [ ] FAB «Добавить карточку» размещать над нижней панелью с использованием bottomControlsPadding.
- [ ] В меню оставить только «Редактировать колоду» и «Удалить колоду». CSV-пункт не показывать.
- [ ] Меню закрывается по нажатию вне него, по Back и перед открытием следующего экрана/sheet.
- [ ] Bottom sheet подтверждения удаления колоды — модальный; Back закрывает sheet, а не детали.
- [ ] Для Content с cards.isEmpty() показать простую иллюстрацию карточек, «В этой колоде пока нет карточек» и CTA «Добавить первую карточку». Создание ведёт в ту же форму, что FAB.
- [ ] Loading — skeleton шапки и нескольких строк; Error — сообщение и Retry; NotFound — «Колода не найдена» с возвратом к списку.
- [ ] Во всех состояниях сохранить доступную кнопку назад и безопасные отступы.

#### Строка карточки и dropdown-меню по удержанию

- [ ] `CardListItem` показывает term стилем titleMedium и definition стилем bodyMedium/цветом muted, максимум две строки с ellipsis.
- [ ] Пример в списке не показывать: он доступен в форме. Статусы Leitner пока отсутствуют.
- [ ] Использовать LazyColumn со стабильными ключами `card.id`; позицию прокрутки хранить через rememberLazyListState.
- [ ] На всю строку карточки установить `combinedClickable`: обычный тап открывает редактирование, долгое нажатие открывает стандартный Material 3 `DropdownMenu`.
- [ ] Меню содержит два пункта: «Редактировать» и «Удалить». Привязать popup к соответствующей строке через общий Box строки и DropdownMenu.
- [ ] В DeckDetailContent хранить один локальный `expandedCardMenuId: Long?` через remember; null означает, что меню карточек закрыты. При открытии меню другой карточки заменить ID.
- [ ] В CardListItem передавать признак раскрытого меню и callbacks открытия, закрытия, редактирования и удаления. Состояние меню конкретной строки не переносить в repository или ViewModel.
- [ ] Меню закрывается по нажатию вне него и системному Back; такой Back не закрывает детали колоды.
- [ ] При выборе «Редактировать» сначала очистить expandedCardMenuId, затем отправить CardClicked(cardId), как при обычном тапе.
- [ ] При выборе «Удалить» сначала очистить expandedCardMenuId, затем отправить DeleteCardClicked(cardId); карточка сразу удаляется из БД.
- [ ] Пока isMutating, блокировать повторное открытие меню и его действия.
- [ ] Если карточка исчезла из списка, сбросить её expandedCardMenuId. Открытое меню не восстанавливать после пересоздания UI.
- [ ] Добавить локализованный onLongClickLabel «Действия карточки» и accessibility custom action «Удалить карточку», отправляющий тот же DeleteCardClicked(cardId).
- [ ] Использовать стандартные DropdownMenu/DropdownMenuItem и текущую тему. Горизонтальные drag-обработчики, anchors и SwipeableCardRow не добавлять.

Оформление получать из RepetrailTheme: colors, typography, shapes, dimensions. Использовать существующие роли surface/ink/muted/accent/error, shapes.pill/card и elevation floating. Не добавлять новую палитру, blur-библиотеку и обязательные расширения токенов. Простые иконки и иллюстрацию разместить локально в feature, без импорта внутренних компонентов shared. Размер области нажатия получать из Material-компонента с его стандартной минимальной interactive area.

### 4.9. Реализовать состояние и логику формы карточки

- [ ] В CardFormViewModel прочитать deckId/cardId из SavedStateHandle.
- [ ] Разделить состояние на `Loading`, `Content`, `LoadError`, `DeckNotFound`, `CardNotFound`; сохранение выразить флагом `isSaving` внутри Content.
- [ ] В состоянии хранить term, definition, example как строки, исходные значения трёх полей, ошибки обязательных полей, ошибку сохранения и showDiscardDialog.
- [ ] `isEditMode = cardId != null`.
- [ ] `hasUnsavedChanges` вычислять для Content сравнением текущих строк с исходными; после загрузки и очистки формы это false. В Loading/LoadError/NotFound не включать перехват Back по dirty-состоянию.
- [ ] `canSave` истинно только для Content, непустых после trim term/definition и отсутствия isSaving.
- [ ] Пока Loading/LoadError/NotFound, не показывать редактируемую пустую форму с доступным сохранением.

Загрузка:

1. Для создания проверить существование deckId через GetDeckByIdUseCase. Если есть — Content с пустыми полями и пустыми исходными значениями.
2. Для редактирования сначала проверить колоду, затем получить карточку через GetCardByIdUseCase(deckId, cardId).
3. Чужой/несуществующий cardId — CardNotFound; не преобразовывать его в создание.
4. Скопировать тексты в текущие и исходные поля; null example представить в поле как пустую строку.
5. Исключение чтения — LoadError с Retry. Retry повторяет первоначальную загрузку; обычное сохранение после ошибки записи не перезагружает введённые поля.
6. Некорректные неположительные ID обрабатывать как отсутствие соответствующей записи.

Intents формы:

| Intent | Поведение |
|---|---|
| TermChanged / DefinitionChanged / ExampleChanged | Изменить соответствующую строку; не выполнять trim во время набора. |
| TermFocusLost / DefinitionFocusLost | Для пустого обязательного поля показать «Обязательное поле». |
| SaveClicked | Сохранить и вернуться в детали. |
| SaveAndAddAnotherClicked | Только создание: сохранить и очистить форму. |
| Retry | Повторить неудавшуюся начальную загрузку. |
| BackClicked | При isSaving игнорировать; иначе выйти сразу или открыть discard-sheet при изменениях. |
| ConfirmDiscard / CancelDiscard | Подтвердить выход или остаться с текущими данными. |

При изменении обязательного поля удалять его ошибку, как только значение становится непустым после trim. Смена фокуса — способ показать ошибку даже при неактивной кнопке сохранения.

Алгоритм сохранения:

1. Синхронно проверить Content, отсутствие isSaving и незавершённого подтверждения выхода. Повторный intent во время записи игнорировать.
2. При SaveAndAddAnotherClicked в режиме редактирования ничего не делать; эта кнопка также отсутствует в UI.
3. Проверить оба обязательных поля; записать ошибки и прекратить сохранение, если есть пустые значения.
4. Зафиксировать снимок введённых значений и выставить isSaving до запуска coroutine.
5. На время записи заблокировать поля и обе кнопки. Не разрешать выход из формы до результата операции; IME при этом можно закрыть.
6. Вызвать SaveCardUseCase с текущими ID и зафиксированным снимком полей.
7. При обычном успешном сохранении присвоить и текущим, и исходным полям одинаковые сохранённые значения после trim (example null представить пустой строкой), чтобы hasUnsavedChanges стал false. Отправить один эффект `SavedAndNavigateBack(isNew)`. До выполнения pop оставить isSaving = true: это блокирует повторное сохранение уже записанной карточки, если навигационный эффект ещё ожидает RESUMED.
8. При успешном «Сохранить и добавить ещё» очистить текущие и исходные поля, ошибки полей и сохранения; снять isSaving, оставить cardId = null.
9. После очистки отправить эффект `CardAddedAndContinue`: snackbar «Карточка добавлена», фокус в поле термина; клавиатура остаётся доступной для следующего ввода.
10. Не очищать форму и не выполнять навигацию до успешного завершения записи.
11. При DeckNotFoundException показать состояние отсутствующей колоды с возвратом к списку; при CardNotFoundException — сообщение об отсутствующей карточке с возвратом к деталям. Не делать insert вместо update.
12. При иной ошибке снять isSaving, оставить введённые строки и исходные значения, показать «Не удалось сохранить карточку». Повторное нажатие сохраняет тот же черновик.
13. CancellationException пробросить. Если запись успела завершиться, пересоздание UI само по себе не должно запускать её повторно.

При удалении колоды между её предварительной проверкой и записью FK не позволит создать сиротскую карточку. Если такая гонка вернулась как общая ошибка БД, сохранить введённый текст и показать ошибку сохранения; не восстанавливать колоду автоматически.

### 4.10. Реализовать UI формы, возврат и сообщения о сохранении

#### CardFormContent и поля

- [ ] Экранный wrapper собирает state через collectAsStateWithLifecycle; контент принимает явные state и intents.
- [ ] Круглая кнопка назад расположена поверх контента; заголовок — «Новая карточка» или «Редактирование карточки».
- [ ] Форма прокручивается целиком; термин — текстовое поле, определение и пример — многострочные.
- [ ] У примера подпись «Пример (необязательно)» и локализованный placeholder.
- [ ] Не обрезать ввод и не преобразовывать регистр.
- [ ] Ошибки обязательных полей показывать под соответствующими полями, цветом error.
- [ ] «Сохранить» — основное действие. «Сохранить и добавить ещё» — вторичное, только при создании.
- [ ] Кнопки доступны при canSave; попытка программного Save intent также проходит валидацию ViewModel.
- [ ] Нижние действия расположить отдельной floating action bar над bottomControlsPadding. При недостатке ширины кнопки размещать вертикально, без обрезания подписей.
- [ ] Измерять высоту action bar и учитывать её во внутреннем нижнем padding прокрутки; при открытой IME форма и действия остаются достижимыми.
- [ ] Loading показать skeleton полей; LoadError — сообщение и Retry; NotFound — понятное сообщение и действие возврата.
- [ ] Общая ошибка сохранения остаётся рядом с формой; она не заменяет введённый текст пустым экраном.
- [ ] CardAddedAndContinue обрабатывать после очистки State: запрос фокуса термина и локальный snackbar. Не помещать FocusRequester или keyboard controller в ViewModel.
- [ ] Для сохранённой и затем очищенной формы Back не открывает discard-sheet.

#### Единая обработка выхода

Использовать существующий подход DeckFormScreen, а не отдельный платформенный back-handler:

- [ ] Кнопка назад отправляет BackClicked; во время isSaving она неактивна.
- [ ] При неизменённой форме без IME и без isSaving системный возврат отдавать NavHost, сохраняя интерактивную анимацию.
- [ ] NavigationBackHandler активировать для IME, изменённой формы или isSaving, пока discard-sheet не открыт.
- [ ] В onBackCompleted сначала скрыть IME, если она видима. Иначе при isSaving остаться на экране, в остальных случаях отправить BackClicked.
- [ ] Завершённый жест изменённой формы открывает discard-sheet; отменённый жест не меняет состояние и не показывает подтверждение.
- [ ] В sheet показать «Отменить изменения?» / «Несохранённые изменения будут потеряны» и действия «Остаться» / «Выйти».
- [ ] Back/нажатие вне sheet выполняет CancelDiscard; ConfirmDiscard отправляет NavigateBack.
- [ ] Во время sheet не выполнять второй pop через обработчик формы.
- [ ] Переключение таба не считать discard: ViewModel и черновик остаются в стеке.
- [ ] После сохранения и подтверждённого discard использовать обычный popBackStack без saveState = true.

#### Сообщение после обычного сохранения

Для «Карточка добавлена» после возврата использовать локальный результат между соседними записями навигационного стека:

1. На SavedAndNavigateBack destination формы записывает в SavedStateHandle предыдущей записи строковый результат под ключом `card_form_result`: `created` или `updated`.
2. После записи результата выполняет один popBackStack.
3. Wrapper деталей читает результат только в RESUMED, сразу сбрасывает ключ в null и обрабатывает сообщение.
4. `created` показывает «Карточка добавлена»; `updated` показывает «Карточка сохранена».
5. Этот результат нужен только для snackbar. Карточки и счётчики обновляются из Room Flow, без ручного reload.
6. Для «Сохранить и добавить ещё» результат в предыдущую запись не отправлять: пользователь остаётся в форме и получает локальный snackbar.
7. Ключ и строковые значения определить в одном месте внутри feature:card. Не создавать глобальную шину событий и не менять общую MVI-архитектуру.

Эффекты собирать одним коллектором на ViewModel внутри repeatOnLifecycle(RESUMED). Для suspending отображения уведомлений использовать отдельную UI-coroutine, чтобы ожидание snackbar не блокировало навигацию. Получение результата деталей также не должно блокировать их основной коллектор side effects.

### 4.11. Добавить ресурсы и завершить интеграцию

- [ ] Все новые тексты разместить в Compose Multiplatform resources модуля `feature:card`: values для EN, values-ru для RU.
- [ ] Использовать отдельные префиксы `deck_detail_*`, `card_form_*`, `card_delete_*`.
- [ ] Локализовать заголовки, labels, placeholders, ошибки, кнопки, содержимое sheets, snackbar и accessibility-описания.
- [ ] Для названия колоды/термина и количества в сообщениях использовать форматные аргументы ресурсов; не собирать локализованную фразу конкатенацией.
- [ ] Для количества карточек использовать plural resources с нужными формами EN/RU.
- [ ] Пользовательские тексты хранить как данные; не добавлять название колоды или карточки в strings.xml.
- [ ] Осмысленные иконки имеют локализованные contentDescription; декоративные иконки — null без дублирования подписи кнопки.
- [ ] Добавить простые previews контентных composable с готовым state и пустыми callbacks, без DI/Room/NavController. Достаточно характерных состояний: детали с карточками, пустая колода, форма; не строить отдельный preview-framework.
- [ ] Оставить существующий вывод cardCount в DeckCard: создание и удаление обновляют его через уже имеющийся Flow.
- [ ] Удалить заглушку обработки NavigateToDeckDetail и убедиться, что в новых кнопках не осталось пустых обработчиков.
- [ ] Не подключать старый демонстрационный `shared/.../screens/cards/CardsScreen.kt` вместо рабочего списка колод.
- [ ] Обновить описание шага 4 в MASTER_PLAN: ссылка на этот документ, фактически реализованная функциональность и статус.
- [ ] В USER_FLOW сохранить уточнение: удержание на карточке открывает dropdown-меню «Редактировать / Удалить»; выбор «Удалить» сразу удаляет карточку без подтверждения, snackbar и undo. Урок/Leitner/CSV подключаются последующими шагами.
- [ ] Не отмечать шаги 5–7 выполненными из-за появления их будущей точки входа.

## 4. Карта изменений по слоям

| Слой | Файлы/изменения |
|---|---|
| Domain | Card, CardRepository, четыре use cases карточек, domain-ошибки отсутствующих записей, ObserveDeckByIdUseCase и новый метод DeckRepository. |
| Data | Дополнение FlashcardDao, реактивный запрос одной колоды в DeckDao, CardMapper, CardRepositoryImpl, метод DeckRepositoryImpl. |
| Feature card | Маршруты/destinations, CardModule, MVI деталей и формы, UI, dropdown-меню по удержанию, ресурсы. |
| Feature deck | Callback открытия деталей через DecksScreen/deckDestinations; регистрация ObserveDeckByIdUseCase. |
| Shared | Зависимость модуля, регистрация repository и cardModule, подключение destinations/callbacks, передача измеренной геометрии панели и политика IME для формы карточки. |
| Gradle | Новый build.gradle.kts и include модуля; версии библиотек без изменений. |
| Документация | Актуализация шага 4 и user flow по фактической реализации. |

## 5. Конечный результат реализации

Шаг считается реализованным, когда в production-коде соединён весь путь:

```text
Список колод
  → Детали колоды из Room
    → Создание карточки → запись → возврат и обновлённый список
    → Создание карточки → «Сохранить и добавить ещё» → чистая форма
    → Редактирование карточки → update исходной записи
    → Удержание на карточке → dropdown-меню → «Удалить» → delete → обновлённый список
    → Редактирование колоды → актуальные название и описание
    → Удаление колоды → каскадное удаление → существующий список колод
```

У формы есть валидация, защита от повторной записи, сохранение введённых данных при ошибке и подтверждение выхода с изменениями. У деталей есть Loading, Content/Empty, Error и NotFound. Количество карточек в списке колод берётся из БД. Все переходы используют существующий стек таба и текущую MVI-схему.
