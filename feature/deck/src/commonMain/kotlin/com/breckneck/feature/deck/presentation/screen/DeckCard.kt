package com.breckneck.feature.deck.presentation.screen

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.breckneck.core.domain.model.Deck
import com.breckneck.core.ui.designsystem.theme.RepetrailTheme
import org.jetbrains.compose.resources.stringResource
import repetrail.core.ui.designsystem.generated.resources.Res
import repetrail.core.ui.designsystem.generated.resources.deck_card_count_few
import repetrail.core.ui.designsystem.generated.resources.deck_card_count_many
import repetrail.core.ui.designsystem.generated.resources.deck_card_count_one
import repetrail.core.ui.designsystem.generated.resources.deck_card_count_zero
import repetrail.core.ui.designsystem.generated.resources.deck_list_action_delete
import repetrail.core.ui.designsystem.generated.resources.deck_list_action_edit

@Composable
fun DeckCard(
    deck: Deck,
    index: Int,
    onDeckClick: (Long) -> Unit,
    onEditClick: (Long) -> Unit,
    onDeleteClick: (Deck) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showMenu by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { onDeckClick(deck.id) },
                onLongClick = { showMenu = true },
            ),
        shape = RepetrailTheme.shapes.card,
        color = RepetrailTheme.colors.surface,
        shadowElevation = RepetrailTheme.dimensions.elevations.card,
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = RepetrailTheme.dimensions.spacings.medium,
                vertical = RepetrailTheme.dimensions.spacings.medium,
            ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                modifier = Modifier.padding(end = RepetrailTheme.dimensions.spacings.medium),
                shape = RepetrailTheme.shapes.pill,
                color = RepetrailTheme.colors.selectedTabBackground,
            ) {
                Text(
                    text = index.toString().padStart(length = 2, padChar = '0'),
                    modifier = Modifier.padding(
                        horizontal = RepetrailTheme.dimensions.spacings.small,
                        vertical = RepetrailTheme.dimensions.spacings.extraSmall,
                    ),
                    color = RepetrailTheme.colors.accent,
                    style = RepetrailTheme.typography.badge,
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = deck.name,
                    color = RepetrailTheme.colors.ink,
                    style = RepetrailTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                val description = deck.description
                if (!description.isNullOrBlank()) {
                    Spacer(Modifier.height(RepetrailTheme.dimensions.spacings.extraSmall))

                    Text(
                        text = description,
                        color = RepetrailTheme.colors.muted,
                        style = RepetrailTheme.typography.bodySmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                Spacer(Modifier.height(RepetrailTheme.dimensions.spacings.extraSmall))

                Text(
                    text = formatCardCountText(count = deck.cardCount),
                    color = RepetrailTheme.colors.muted,
                    style = RepetrailTheme.typography.bodySmall,
                )
            }

            Spacer(Modifier.width(RepetrailTheme.dimensions.spacings.small))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "›",
                    color = RepetrailTheme.colors.muted,
                    style = RepetrailTheme.typography.headingMedium,
                )

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = stringResource(Res.string.deck_list_action_edit),
                                style = RepetrailTheme.typography.bodyMedium,
                                color = RepetrailTheme.colors.ink,
                            )
                        },
                        onClick = {
                            showMenu = false
                            onEditClick(deck.id)
                        },
                    )

                    DropdownMenuItem(
                        text = {
                            Text(
                                text = stringResource(Res.string.deck_list_action_delete),
                                style = RepetrailTheme.typography.bodyMedium,
                                color = RepetrailTheme.colors.accent,
                            )
                        },
                        onClick = {
                            showMenu = false
                            onDeleteClick(deck)
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun formatCardCountText(count: Int): String {
    val remainder10 = count % 10
    val remainder100 = count % 100
    return when {
        count == 0 -> stringResource(Res.string.deck_card_count_zero)
        (remainder100 in 11..14) -> stringResource(Res.string.deck_card_count_many, count)
        remainder10 == 1 -> stringResource(Res.string.deck_card_count_one, count)
        remainder10 in 2..4 -> stringResource(Res.string.deck_card_count_few, count)
        else -> stringResource(Res.string.deck_card_count_many, count)
    }
}

@Preview
@Composable
private fun DeckCardPreview() {
    RepetrailTheme {
        DeckCard(
            deck = Deck(
                id = 1L,
                name = "English · Basic Course",
                description = "Common phrases for travel and communication",
                cardCount = 24,
                createdAt = 0L,
                updatedAt = 0L,
            ),
            index = 1,
            onDeckClick = {},
            onEditClick = {},
            onDeleteClick = {},
        )
    }
}
