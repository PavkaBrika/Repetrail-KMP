---
name: repetrail-mvp
description: Guidelines and principles for lean, pragmatic MVP development in Repetrail. Anti-overengineering rules, clean state modeling without premature safety rituals, minimal composable parameter signatures, and straightforward coroutines.
---

# Repetrail MVP: Принципы прагматичной разработки

Руководство по написанию чистого, прямолинейного и поддерживаемого кода на стадии MVP. Правила защищают кодовую базу от оверинжиниринга, выдуманных граничных случаев, параноидальных обёрток и раздутых сигнатур.

---

## 1. Анти-оверинжиниринг: без параноидального «мега-сейва»

На этапе MVP код должен решать текущую задачу кратчайшим и очевидным путём. Не защищайся от проблем, которых ещё не существует.

- **Никаких искусственных блокировок без реальной конкурентности:**  
  Не добавляй флаги вроде `isMutating`, `isProcessing`, `isBusy` на каждое действие. Пока нет фоновой синхронизации или подтверждённого race condition, блокировки создают шум в стейте, усложняют UI-условия и отладку.
- **Никаких выдуманных ошибок операций (`OperationError`):**  
  Локальные операции Room/SQLite (удаление, вставка, чтение) детерминированы. Не городи отдельные enum-ошибки (`DeleteDeckError`, `DeleteFlashcardError`) на каждый чих «на случай, если упадёт диск». Ошибки добавляются только под реальные продуктовые требования.
- **Не оборачивай локальные вызовы в ритуальные `try / catch`:**  
  Flow и запросы к локальной базе не требуют защитных `try-catch` с перехватом `CancellationException` и сбросом в состояние ошибки. Пиши вызов напрямую. Если корутина отменяется — дай ей штатно отмениться.

```kotlin
// ❌ Избыточно (паранойя):
private fun deleteFlashcard(id: Long) {
    if (state.value.isMutating) return
    mutableState.update { it.copy(isMutating = true, operationError = null) }
    viewModelScope.launch {
        try {
            deleteFlashcardUseCase(deckId, id)
        } catch (e: CancellationException) {
            throw e
        } catch (_: Exception) {
            mutableState.update { it.copy(operationError = OperationError.Delete) }
        } finally {
            mutableState.update { it.copy(isMutating = false) }
        }
    }
}

// ✅ Для MVP (прямо и понятно):
private fun deleteFlashcard(id: Long) {
    viewModelScope.launch {
        deleteFlashcardUseCase(deckId, id)
    }
}
```

---

## 2. Моделирование состояния: данные там, где они гарантированы

- **Избавляйся от `null` через структуру состояний:**  
  Если для отображения `Content` или `Empty` сущность (например, `Deck`) обязана существовать — храни её внутри соответствующих вариантов `UiState`, а не nullable-полем в корневом `State`.
- **Используй смарт-каст вместо безопасных вызовов (`?.let`):**  
  Когда данные лежат внутри ветки sealed-интерфейса, компилятор гарантирует их наличие. В UI-коде не должно быть цепочек `state.deck?.let { ... }` внутри ветки, которая и так означает, что колода загружена.
- **Вычисляемые свойства для удобства:**  
  Если свойство требуется в общих элементах (например, в заголовке или тулбаре), добавь computed-геттер в `ScreenState`, возвращающий данные из активного `UiState`, вместо дублирования поля в конструкторе.

```kotlin
// ✅ Чистая модель:
sealed interface ScreenUiState {
    data object Loading : ScreenUiState
    data object Error : ScreenUiState
    data class Empty(val deck: Deck) : ScreenUiState
    data class Content(val deck: Deck, val flashcards: List<Flashcard>) : ScreenUiState
}

data class ScreenState(
    val uiState: ScreenUiState = ScreenUiState.Loading,
    val showMenu: Boolean = false,
) {
    val deck: Deck?
        get() = when (val ui = uiState) {
            is ScreenUiState.Empty -> ui.deck
            is ScreenUiState.Content -> ui.deck
            ScreenUiState.Loading, ScreenUiState.Error -> null
        }
}
```

---

## 3. Сигнатуры Composable: передавай `State`, не дублируй параметры

- **Единый источник истины в аргументах:**  
  Если дочерний компонент получает `state: ScreenState`, **запрещено** параллельно передавать поля, которые уже есть в этом стейте (`deck`, `flashcards`, `uiState`). Компонент сам забирает нужные данные из `state`.
- **Не раздувай список параметров:**  
  Разделяй аргументы на две группы:
  1. `state: ScreenState` — данные экрана.
  2. `onIntent: (ScreenIntent) -> Unit` — обработка событий.
  Плюс необходимые layout-параметры (например, `listState`, `modifier`).
- **Не заставляй родительский экран готовить данные за детей:**  
  Корневой экран не должен вычислять промежуточные переменные (`val deck = ...`, `val count = ...`) только ради того, чтобы скормить их в дочерний компонент.

```kotlin
// ❌ Плохо: дублирование данных в аргументах
DeckDetailFlashcardList(
    deck = uiState.deck,
    flashcards = uiState.flashcards,
    state = state,
    onIntent = onIntent,
)

// ✅ Чисто: компонент сам берёт данные из стейта
DeckDetailFlashcardList(
    state = state,
    onIntent = onIntent,
)
```

---

## 4. Прямой доступ к свойствам: без «переменных ради переменных»

- **Не перекладывай данные из `state` в локальные переменные:**  
  Не объявляй в начале Composable пачку промежуточных `val` (`val deck = state.deck`, `val items = state.items`, `val showDialog = state.showDialog`), которые просто дублируют поля стейта. Это лишний визуальный шум и перекладывание из кармана в карман.
- **Не бойся использовать `state.property` напрямую:**  
  Обращение по месту (`state.deck?.name`, `if (state.showDeleteDialog)`) делает код прозрачным: сразу видно источник правды и от каких именно полей зависит UI.
- **Когда локальная переменная оправдана:**  
  Только если это результат сложного/дорогого вычисления, смарт-каст с ранним выходом (`val deck = state.deck ?: return`) или значение для `remember` / `derivedStateOf`. Во всех остальных случаях читай поля прямо из `state`.

```kotlin
// ❌ Плохо: распаковка стейта в пачку локальных val ради val
@Composable
fun DeckDetailContent(state: DeckDetailState) {
    val deck = state.deck
    val isDialogVisible = state.showDeleteDeckDialog
    val showMenu = state.showMenu
    ...
    if (isDialogVisible && deck != null) { ... }
}

// ✅ Чисто: прямое использование стейта по месту
@Composable
fun DeckDetailContent(state: DeckDetailState) {
    ...
    if (state.showDeleteDeckDialog) {
        DeckDetailDeleteDialog(state = state)
    }
}
```

---

## 5. Самодостаточность компонентов (Layout и Scopes)

- **Не распространяй `BoxScope` / `RowScope` наружу без необходимости:**  
  Не объявляй внутренние компоненты экрана как `BoxScope.MyComponent(...)` только ради того, чтобы сделать `Modifier.align(...)` на вложенной кнопке.
- **Внутренний контейнер внутри компонента:**  
  Сделай компонент самостоятельным: помести внутрь свой собственный `Box(modifier = modifier.fillMaxSize())`, внутри которого располагаются список и плавающий FAB.
- **Внешний API:**  
  Компонент должен принимать обычный `modifier: Modifier = Modifier` и применять его к корневому макету.

---

## 6. Никаких флагов «на будущее» и недоделок-заглушек

- **Не протаскивай неактивные фичи через параметры:**  
  Флаги вроде `canOpenForm: Boolean = false`, `canEdit: Boolean = false`, прокинутые сквозь 5 уровней вложенности «на потом», создают иллюзию гибкости, но на деле ломают кликабельность и захламляют код.
- **Правило готовности:**  
  Если экран умеет обрабатывать намерение — поддерживай его сразу и держи элементы активными. Если фича не существует — удали кнопку, а не блокируй её искусственным флагом.

---

## 7. Чек-лист проверки кода на MVP

Перед тем как завершить изменения в экранах и ViewModel, проверь себя:

1. **Нет ли паранойи?** — Не перехвачены ли штатные корутины без необходимости? Нет ли лишних флагов блокировок (`isMutating`)?
2. **Нет ли дублирования аргументов?** — Не передаются ли в Composable одновременно `state` и свойства из него?
3. **Нет ли «переменных ради переменных»?** — Не распаковываются ли поля стейта в локальные `val` в начале Composable без реальной необходимости?
4. **Нет ли лишних `null`?** — Если данные гарантированно есть в загруженном состоянии, они являются `non-null` полями этого состояния?
5. **Легко ли читать код новому разработчику?** — Понятен ли поток данных с первого взгляда без мысленного отслеживания 10 флагов состояния?
