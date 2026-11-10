package com.breckneck.feature.flashcard.detail.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.hideFromAccessibility
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.breckneck.core.domain.model.Deck
import com.breckneck.core.domain.model.Flashcard
import com.breckneck.core.ui.designsystem.theme.RepetrailTheme
import com.breckneck.feature.flashcard.detail.DeckDetailIntent
import com.breckneck.feature.flashcard.detail.DeckDetailState
import com.breckneck.feature.flashcard.detail.DeckDetailUiState
import org.jetbrains.compose.resources.stringResource
import repetrail.feature.flashcard.generated.resources.*

@Composable
internal fun DeckDetailContent(
    state: DeckDetailState,
    onIntent: (DeckDetailIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()

    val density = LocalDensity.current
    val safePadding = WindowInsets.safeDrawing.asPaddingValues()
    val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() +
            RepetrailTheme.dimensions.spacings.medium
    val topPadding = safePadding.calculateTopPadding() +
            RepetrailTheme.dimensions.navBarItemHeight +
            RepetrailTheme.dimensions.spacings.large
    val topPaddingPx = with(density) { topPadding.roundToPx() }

    val titleProgress by remember(listState, topPaddingPx) {
        derivedStateOf {
            val heading = listState.layoutInfo.visibleItemsInfo.firstOrNull { it.key == "heading" }
            if (heading == null) {
                if (listState.firstVisibleItemIndex > 0) 1f else 0f
            } else {
                ((topPaddingPx - heading.offset).toFloat() / heading.size.coerceAtLeast(1)).coerceIn(
                    0f,
                    1f
                )
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(RepetrailTheme.colors.background)
    ) {
        when (state.uiState) {
            DeckDetailUiState.Loading -> DeckDetailLoading(topPadding, bottomPadding)

            DeckDetailUiState.Error -> DeckDetailError(
                onRetry = { onIntent(DeckDetailIntent.Retry) },
            )

            is DeckDetailUiState.Empty -> DeckDetailEmpty(
                state = state.uiState,
                listState = listState,
                topPadding = topPadding,
                bottomControlsPadding = bottomPadding,
                titleProgress = titleProgress,
                onIntent = onIntent,
            )

            is DeckDetailUiState.Content -> DeckDetailFlashcardList(
                state = state.uiState,
                listState = listState,
                topPadding = topPadding,
                bottomControlsPadding = bottomPadding,
                titleProgress = titleProgress,
                onIntent = onIntent,
            )
        }

        DeckDetailTopControls(
            state = state,
            titleProgress = titleProgress,
            onIntent = onIntent,
            modifier = Modifier.align(Alignment.TopCenter),
        )

        if (state.showDeleteDeckDialog) {
            DeckDetailDeleteDialog(
                state = state,
                onIntent = onIntent,
            )
        }
    }
}

@Composable
private fun DeckDetailLoading(topPadding: Dp, bottomControlsPadding: Dp) {
    val layoutDirection = LocalLayoutDirection.current
    val safePadding = WindowInsets.safeDrawing.asPaddingValues()

    Column(
        modifier = Modifier.padding(
            start = RepetrailTheme.dimensions.spacings.large +
                    safePadding.calculateStartPadding(layoutDirection),
            top = topPadding,
            end = RepetrailTheme.dimensions.spacings.large +
                    safePadding.calculateEndPadding(layoutDirection),
            bottom = bottomControlsPadding,
        ),
        verticalArrangement = Arrangement.spacedBy(RepetrailTheme.dimensions.spacings.medium),
    ) {
        repeat(3) {
            Surface(
                modifier = Modifier.fillMaxWidth()
                    .height(RepetrailTheme.dimensions.navBarItemHeight),
                shape = RepetrailTheme.shapes.card,
                color = RepetrailTheme.colors.surfaceVariant,
            ) {}
        }
    }
}

@Composable
private fun DeckDetailError(onRetry: () -> Unit) {
    DeckDetailMessage(
        message = stringResource(Res.string.deck_detail_error),
        actionLabel = stringResource(Res.string.deck_detail_retry),
        onAction = onRetry,
    )
}

@Composable
private fun DeckDetailMessage(
    message: String,
    actionLabel: String,
    onAction: () -> Unit,
) {
    val layoutDirection = LocalLayoutDirection.current
    val safePadding = WindowInsets.safeDrawing.asPaddingValues()

    Column(
        modifier = Modifier.fillMaxSize().padding(
            start = RepetrailTheme.dimensions.spacings.large +
                    safePadding.calculateStartPadding(layoutDirection),
            end = RepetrailTheme.dimensions.spacings.large +
                    safePadding.calculateEndPadding(layoutDirection),
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = message,
            style = RepetrailTheme.typography.bodyMedium,
            color = RepetrailTheme.colors.ink,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(RepetrailTheme.dimensions.spacings.medium))

        Button(onClick = onAction) {
            Text(actionLabel)
        }
    }
}

@Composable
private fun DeckDetailEmpty(
    state: DeckDetailUiState.Empty,
    listState: LazyListState,
    topPadding: Dp,
    bottomControlsPadding: Dp,
    titleProgress: Float,
    onIntent: (DeckDetailIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current
    val safePadding = WindowInsets.safeDrawing.asPaddingValues()
    var actionHeight by remember { mutableStateOf(0) }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = RepetrailTheme.dimensions.spacings.large + safePadding.calculateStartPadding(
                    layoutDirection
                ),
                top = topPadding,
                end = RepetrailTheme.dimensions.spacings.large + safePadding.calculateEndPadding(
                    layoutDirection
                ),
                bottom = bottomControlsPadding +
                        with(density) { actionHeight.toDp() } +
                        RepetrailTheme.dimensions.spacings.large,
            ),
            verticalArrangement = Arrangement.spacedBy(RepetrailTheme.dimensions.spacings.medium),
        ) {
            item(key = "heading") {
                DeckDetailHeading(deck = state.deck, flashcardCount = 0, titleProgress = titleProgress)
            }

            item(key = "empty") {
                DeckDetailEmptyPlaceholder(
                    onAddFlashcard = { onIntent(DeckDetailIntent.AddFlashcardClicked) },
                )
            }
        }

        Button(
            onClick = { onIntent(DeckDetailIntent.AddFlashcardClicked) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    end = RepetrailTheme.dimensions.spacings.large +
                            safePadding.calculateEndPadding(layoutDirection),
                    bottom = bottomControlsPadding,
                )
                .onSizeChanged { actionHeight = it.height },
            colors = ButtonDefaults.buttonColors(containerColor = RepetrailTheme.colors.accent),
            shape = RepetrailTheme.shapes.pill,
        ) {
            Text(stringResource(Res.string.deck_detail_add))
        }
    }
}

@Composable
private fun DeckDetailFlashcardList(
    state: DeckDetailUiState.Content,
    listState: LazyListState,
    topPadding: Dp,
    bottomControlsPadding: Dp,
    titleProgress: Float,
    onIntent: (DeckDetailIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current
    val safePadding = WindowInsets.safeDrawing.asPaddingValues()
    var actionHeight by remember { mutableStateOf(0) }
    var expandedFlashcardId by remember { mutableStateOf<Long?>(null) }

    LaunchedEffect(state.flashcards) {
        if (state.flashcards.none { it.id == expandedFlashcardId }) {
            expandedFlashcardId = null
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = RepetrailTheme.dimensions.spacings.large + safePadding.calculateStartPadding(
                    layoutDirection
                ),
                top = topPadding,
                end = RepetrailTheme.dimensions.spacings.large + safePadding.calculateEndPadding(
                    layoutDirection
                ),
                bottom = bottomControlsPadding + with(density) { actionHeight.toDp() } + RepetrailTheme.dimensions.spacings.large,
            ),
            verticalArrangement = Arrangement.spacedBy(RepetrailTheme.dimensions.spacings.medium),
        ) {
            item(key = "heading") {
                DeckDetailHeading(
                    deck = state.deck,
                    flashcardCount = state.flashcards.size,
                    titleProgress = titleProgress,
                )
            }

            items(items = state.flashcards, key = { it.id }) { flashcard ->
                FlashcardListItem(
                    flashcard = flashcard,
                    menuExpanded = expandedFlashcardId == flashcard.id,
                    enabled = true,
                    onClick = {
                        expandedFlashcardId = null
                        onIntent(DeckDetailIntent.FlashcardClicked(flashcard.id))
                    },
                    onOpenMenu = { expandedFlashcardId = flashcard.id },
                    onDismissMenu = { expandedFlashcardId = null },
                    onDelete = {
                        expandedFlashcardId = null
                        onIntent(DeckDetailIntent.DeleteFlashcardClicked(flashcard.id))
                    },
                )
            }
        }

        Button(
            onClick = { onIntent(DeckDetailIntent.AddFlashcardClicked) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    end = RepetrailTheme.dimensions.spacings.large + safePadding.calculateEndPadding(
                        layoutDirection
                    ),
                    bottom = bottomControlsPadding,
                )
                .onSizeChanged { actionHeight = it.height },
            colors = ButtonDefaults.buttonColors(containerColor = RepetrailTheme.colors.accent),
            shape = RepetrailTheme.shapes.pill,
        ) {
            Text(stringResource(Res.string.deck_detail_add))
        }
    }
}

@Composable
private fun DeckDetailHeading(
    deck: Deck,
    flashcardCount: Int,
    titleProgress: Float,
) {
    Column {
        Text(
            text = deck.name,
            modifier = Modifier.semantics { if (titleProgress >= 0.5f) hideFromAccessibility() },
            style = RepetrailTheme.typography.headingLarge,
            color = RepetrailTheme.colors.ink,
        )
        val description = deck.description
        if (!description.isNullOrBlank()) {
            Text(
                text = description,
                modifier = Modifier.padding(top = RepetrailTheme.dimensions.spacings.small),
                style = RepetrailTheme.typography.bodyMedium,
                color = RepetrailTheme.colors.muted,
            )
        }

        Row(modifier = Modifier.padding(top = RepetrailTheme.dimensions.spacings.medium)) {
            Text(
                stringResource(Res.string.deck_detail_total, flashcardCount),
                color = RepetrailTheme.colors.muted
            )
            Spacer(Modifier.width(RepetrailTheme.dimensions.spacings.large))
            Text(
                stringResource(Res.string.deck_detail_new, flashcardCount),
                color = RepetrailTheme.colors.muted
            )
        }
    }
}

@Composable
private fun DeckDetailEmptyPlaceholder(
    onAddFlashcard: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = RepetrailTheme.dimensions.spacings.extraLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "▣",
            modifier = Modifier.clearAndSetSemantics {},
            style = RepetrailTheme.typography.headingLarge,
            color = RepetrailTheme.colors.accent,
        )

        Text(
            text = stringResource(Res.string.deck_detail_empty),
            modifier = Modifier.padding(top = RepetrailTheme.dimensions.spacings.medium),
            color = RepetrailTheme.colors.muted,
            style = RepetrailTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
        )

        OutlinedButton(
            onClick = onAddFlashcard,
            modifier = Modifier.padding(top = RepetrailTheme.dimensions.spacings.medium),
        ) {
            Text(stringResource(Res.string.deck_detail_add_first))
        }
    }
}

@Composable
private fun DeckDetailTopControls(
    state: DeckDetailState,
    titleProgress: Float,
    onIntent: (DeckDetailIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val layoutDirection = LocalLayoutDirection.current
    val safePadding = WindowInsets.safeDrawing.asPaddingValues()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = RepetrailTheme.dimensions.spacings.medium + safePadding.calculateStartPadding(
                    layoutDirection
                ),
                end = RepetrailTheme.dimensions.spacings.medium + safePadding.calculateEndPadding(
                    layoutDirection
                ),
                top = safePadding.calculateTopPadding() + RepetrailTheme.dimensions.spacings.small,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val backLabel = stringResource(Res.string.deck_detail_back)
        Surface(
            shape = RepetrailTheme.shapes.pill,
            color = RepetrailTheme.colors.surface,
            shadowElevation = RepetrailTheme.dimensions.elevations.floating
        ) {
            IconButton(
                onClick = { onIntent(DeckDetailIntent.BackClicked) },
                modifier = Modifier.semantics { contentDescription = backLabel }) {
                Text("‹", style = RepetrailTheme.typography.headingLarge)
            }
        }

        Box(
            modifier = Modifier.weight(1f)
                .padding(horizontal = RepetrailTheme.dimensions.spacings.small),
            contentAlignment = Alignment.Center
        ) {
            if (state.deck != null && titleProgress > 0f) {
                Surface(
                    modifier = Modifier.alpha(titleProgress)
                        .semantics { if (titleProgress < 0.5f) hideFromAccessibility() },
                    shape = RepetrailTheme.shapes.pill,
                    color = RepetrailTheme.colors.surface,
                    shadowElevation = RepetrailTheme.dimensions.elevations.floating,
                ) {
                    Text(
                        text = state.deck?.name.orEmpty(),
                        modifier = Modifier.padding(
                            horizontal = RepetrailTheme.dimensions.spacings.medium,
                            vertical = RepetrailTheme.dimensions.spacings.small
                        ),
                        style = RepetrailTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }

        if (state.deck != null) {
            Box {
                val menuLabel = stringResource(Res.string.deck_detail_menu)
                Surface(
                    shape = RepetrailTheme.shapes.pill,
                    color = RepetrailTheme.colors.surface,
                    shadowElevation = RepetrailTheme.dimensions.elevations.floating
                ) {
                    IconButton(
                        onClick = { onIntent(DeckDetailIntent.OpenMenu) },
                        modifier = Modifier.semantics { contentDescription = menuLabel }) {
                        Text("⋮", style = RepetrailTheme.typography.headingMedium)
                    }
                }
                DropdownMenu(
                    expanded = state.showMenu,
                    onDismissRequest = { onIntent(DeckDetailIntent.DismissMenu) }) {
                    DropdownMenuItem(
                        text = { Text(stringResource(Res.string.deck_detail_edit_deck)) },
                        onClick = { onIntent(DeckDetailIntent.EditDeckClicked) })
                    DropdownMenuItem(
                        text = { Text(stringResource(Res.string.deck_detail_delete_deck)) },
                        onClick = { onIntent(DeckDetailIntent.DeleteDeckClicked) })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DeckDetailDeleteDialog(
    state: DeckDetailState,
    onIntent: (DeckDetailIntent) -> Unit,
) {
    val deck = state.deck ?: return

    ModalBottomSheet(
        onDismissRequest = { onIntent(DeckDetailIntent.CancelDeleteDeck) },
        containerColor = RepetrailTheme.colors.surface,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(
                horizontal = RepetrailTheme.dimensions.spacings.large,
                vertical = RepetrailTheme.dimensions.spacings.medium
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                stringResource(Res.string.deck_detail_delete_title),
                style = RepetrailTheme.typography.headingMedium
            )
            Text(
                stringResource(
                    Res.string.deck_detail_delete_message,
                    deck.name,
                    (state.uiState as? DeckDetailUiState.Content)?.flashcards?.size ?: 0,
                ),
                modifier = Modifier.padding(vertical = RepetrailTheme.dimensions.spacings.medium),
                style = RepetrailTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
            )
            Button(
                onClick = { onIntent(DeckDetailIntent.ConfirmDeleteDeck) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(Res.string.deck_detail_delete))
            }
            OutlinedButton(
                onClick = { onIntent(DeckDetailIntent.CancelDeleteDeck) },
                modifier = Modifier.fillMaxWidth()
                    .padding(top = RepetrailTheme.dimensions.spacings.small)
            ) {
                Text(stringResource(Res.string.deck_detail_cancel))
            }
        }
    }
}

@Preview
@Composable
private fun DeckWithFlashcardsPreview() {
    RepetrailTheme {
        val deck = Deck(
            id = 1,
            name = "English · Travel",
            description = "Useful expressions",
            createdAt = 0,
            updatedAt = 0
        )
        DeckDetailContent(
            state = DeckDetailState(
                uiState = DeckDetailUiState.Content(
                    deck = deck,
                    flashcards = listOf(
                        Flashcard(
                            id = 1,
                            deckId = 1,
                            term = "Departure",
                            definition = "The act of leaving",
                            example = null,
                            createdAt = 0
                        ),
                        Flashcard(
                            id = 2,
                            deckId = 1,
                            term = "Arrival",
                            definition = "The act of reaching a place",
                            example = null,
                            createdAt = 0
                        ),
                    ),
                ),
            ),
            onIntent = {},
        )
    }
}

@Preview
@Composable
private fun EmptyDeckPreview() {
    RepetrailTheme {
        val deck = Deck(
            id = 1,
            name = "English · Travel",
            description = null,
            createdAt = 0,
            updatedAt = 0
        )
        DeckDetailContent(
            state = DeckDetailState(
                uiState = DeckDetailUiState.Empty(
                    deck = deck,
                ),
            ),
            onIntent = {},
        )
    }
}
