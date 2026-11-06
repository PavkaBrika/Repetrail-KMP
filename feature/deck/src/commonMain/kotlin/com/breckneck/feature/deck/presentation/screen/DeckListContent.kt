package com.breckneck.feature.deck.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.breckneck.core.domain.model.Deck
import com.breckneck.core.ui.designsystem.theme.RepetrailTheme
import com.breckneck.feature.deck.presentation.DeckListIntent
import com.breckneck.feature.deck.presentation.DeckListState
import com.breckneck.feature.deck.presentation.DeckListUiState
import org.jetbrains.compose.resources.stringResource
import repetrail.core.ui.designsystem.generated.resources.Res
import repetrail.core.ui.designsystem.generated.resources.deck_list_create_button
import repetrail.core.ui.designsystem.generated.resources.deck_list_delete_dialog_cancel
import repetrail.core.ui.designsystem.generated.resources.deck_list_delete_dialog_confirm
import repetrail.core.ui.designsystem.generated.resources.deck_list_delete_dialog_message
import repetrail.core.ui.designsystem.generated.resources.deck_list_delete_dialog_title
import repetrail.core.ui.designsystem.generated.resources.deck_list_empty_subtitle
import repetrail.core.ui.designsystem.generated.resources.deck_list_empty_title
import repetrail.core.ui.designsystem.generated.resources.deck_list_eyebrow
import repetrail.core.ui.designsystem.generated.resources.deck_list_retry_button
import repetrail.core.ui.designsystem.generated.resources.deck_list_subtitle
import repetrail.core.ui.designsystem.generated.resources.deck_list_title

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DecksContent(
    state: DeckListState,
    onIntent: (DeckListIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = RepetrailTheme.colors.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onIntent(DeckListIntent.CreateDeckClicked) },
                containerColor = RepetrailTheme.colors.accent,
                contentColor = RepetrailTheme.colors.surface,
                shape = RepetrailTheme.shapes.pill,
            ) {
                Text(
                    text = "+",
                    style = RepetrailTheme.typography.headingMedium,
                )
            }
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            when (val uiState = state.uiState) {
                DeckListUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(color = RepetrailTheme.colors.accent)
                    }
                }

                is DeckListUiState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(RepetrailTheme.dimensions.spacings.large),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = uiState.message,
                            style = RepetrailTheme.typography.bodyMedium,
                            color = RepetrailTheme.colors.ink,
                            textAlign = TextAlign.Center,
                        )

                        Spacer(Modifier.height(RepetrailTheme.dimensions.spacings.medium))

                        Button(
                            onClick = { onIntent(DeckListIntent.Retry) },
                            colors = ButtonDefaults.buttonColors(containerColor = RepetrailTheme.colors.accent),
                        ) {
                            Text(
                                text = stringResource(Res.string.deck_list_retry_button),
                                style = RepetrailTheme.typography.bodyMedium,
                            )
                        }
                    }
                }

                DeckListUiState.Empty -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(RepetrailTheme.dimensions.spacings.extraLarge),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = stringResource(Res.string.deck_list_empty_title),
                            style = RepetrailTheme.typography.titleMedium,
                            color = RepetrailTheme.colors.ink,
                            textAlign = TextAlign.Center,
                        )

                        Spacer(Modifier.height(RepetrailTheme.dimensions.spacings.small))

                        Text(
                            text = stringResource(Res.string.deck_list_empty_subtitle),
                            style = RepetrailTheme.typography.bodyMedium,
                            color = RepetrailTheme.colors.muted,
                            textAlign = TextAlign.Center,
                        )

                        Spacer(Modifier.height(RepetrailTheme.dimensions.spacings.large))

                        Button(
                            onClick = { onIntent(DeckListIntent.CreateDeckClicked) },
                            colors = ButtonDefaults.buttonColors(containerColor = RepetrailTheme.colors.accent),
                        ) {
                            Text(
                                text = stringResource(Res.string.deck_list_create_button),
                                style = RepetrailTheme.typography.bodyMedium,
                            )
                        }
                    }
                }

                is DeckListUiState.Content -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        // 112.dp clearance above the floating action button and bottom navigation bar
                        contentPadding = PaddingValues(
                            start = RepetrailTheme.dimensions.spacings.large,
                            top = RepetrailTheme.dimensions.spacings.large,
                            end = RepetrailTheme.dimensions.spacings.large,
                            bottom = 112.dp + WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding(),
                        ),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        item(key = "heading") {
                            Column {
                                Text(
                                    text = stringResource(Res.string.deck_list_eyebrow),
                                    color = RepetrailTheme.colors.muted,
                                    style = RepetrailTheme.typography.eyebrow,
                                )

                                Spacer(Modifier.height(RepetrailTheme.dimensions.spacings.extraSmall))

                                Text(
                                    text = stringResource(Res.string.deck_list_title),
                                    color = RepetrailTheme.colors.ink,
                                    style = RepetrailTheme.typography.headingLarge,
                                )

                                Spacer(Modifier.height(RepetrailTheme.dimensions.spacings.extraSmall))

                                Text(
                                    text = stringResource(Res.string.deck_list_subtitle),
                                    color = RepetrailTheme.colors.muted,
                                    style = RepetrailTheme.typography.bodyMedium,
                                )

                                Spacer(Modifier.height(RepetrailTheme.dimensions.spacings.small))
                            }
                        }

                        itemsIndexed(
                            items = uiState.decks,
                            key = { _, deck -> deck.id },
                        ) { index, deck ->
                            DeckCard(
                                deck = deck,
                                index = index + 1,
                                onDeckClick = { id -> onIntent(DeckListIntent.DeckClicked(deckId = id)) },
                                onEditClick = { id -> onIntent(DeckListIntent.EditDeckClicked(deckId = id)) },
                                onDeleteClick = { deckToDelete -> onIntent(DeckListIntent.DeleteDeckClicked(deck = deckToDelete)) },
                            )
                        }
                    }
                }
            }
        }
    }

    val deleteDeckDialogState = state.deleteDeckDialogState
    val deckToDelete = deleteDeckDialogState.deckToDelete
    if (deleteDeckDialogState.isVisible && deckToDelete != null) {
        ModalBottomSheet(
            onDismissRequest = { onIntent(DeckListIntent.CancelDelete) },
            containerColor = RepetrailTheme.colors.surface,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = RepetrailTheme.dimensions.spacings.large,
                        vertical = RepetrailTheme.dimensions.spacings.medium,
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(Res.string.deck_list_delete_dialog_title),
                    style = RepetrailTheme.typography.headingMedium,
                    color = RepetrailTheme.colors.ink,
                    textAlign = TextAlign.Center,
                )

                Spacer(Modifier.height(RepetrailTheme.dimensions.spacings.small))

                Text(
                    text = stringResource(
                        Res.string.deck_list_delete_dialog_message,
                        deckToDelete.name,
                        deckToDelete.cardCount,
                    ),
                    style = RepetrailTheme.typography.bodyMedium,
                    color = RepetrailTheme.colors.muted,
                    textAlign = TextAlign.Center,
                )

                Spacer(Modifier.height(RepetrailTheme.dimensions.spacings.large))

                Button(
                    onClick = { onIntent(DeckListIntent.ConfirmDelete) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = RepetrailTheme.colors.accent),
                ) {
                    Text(
                        text = stringResource(Res.string.deck_list_delete_dialog_confirm),
                        style = RepetrailTheme.typography.bodyMedium,
                    )
                }

                Spacer(Modifier.height(RepetrailTheme.dimensions.spacings.small))

                OutlinedButton(
                    onClick = { onIntent(DeckListIntent.CancelDelete) },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = stringResource(Res.string.deck_list_delete_dialog_cancel),
                        style = RepetrailTheme.typography.bodyMedium,
                        color = RepetrailTheme.colors.ink,
                    )
                }

                Spacer(Modifier.height(RepetrailTheme.dimensions.spacings.medium))
            }
        }
    }
}

@Preview
@Composable
private fun DecksContentPreview() {
    RepetrailTheme {
        DecksContent(
            state = DeckListState.initial()
                .copy(
                    uiState = DeckListUiState.Content(
                        decks = listOf(
                            Deck(
                                id = 1L,
                                name = "English · Travel Phrases",
                                description = "Airport, hotel, and restaurant basics",
                                cardCount = 30,
                                createdAt = 0L,
                                updatedAt = 0L,
                            ),
                            Deck(
                                id = 2L,
                                name = "Biology · Cell Structure",
                                description = "Organelles, mitochondria, DNA",
                                cardCount = 15,
                                createdAt = 0L,
                                updatedAt = 0L,
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
private fun DecksContentEmptyPreview() {
    RepetrailTheme {
        DecksContent(
            state = DeckListState.initial().copy(uiState = DeckListUiState.Empty),
            onIntent = {},
        )
    }
}

@Preview
@Composable
private fun DecksContentErrorPreview() {
    RepetrailTheme {
        DecksContent(
            state = DeckListState.initial()
                .copy(uiState = DeckListUiState.Error(message = "Failed to load decks")),
            onIntent = {},
        )
    }
}
