package com.breckneck.feature.flashcard.detail.screen

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import com.breckneck.core.domain.model.Flashcard
import com.breckneck.core.ui.designsystem.theme.RepetrailTheme
import org.jetbrains.compose.resources.stringResource
import repetrail.feature.flashcard.generated.resources.Res
import repetrail.feature.flashcard.generated.resources.flashcard_action_delete
import repetrail.feature.flashcard.generated.resources.flashcard_action_edit
import repetrail.feature.flashcard.generated.resources.flashcard_actions
import repetrail.feature.flashcard.generated.resources.flashcard_delete_accessibility

@Composable
internal fun FlashcardListItem(
    flashcard: Flashcard,
    menuExpanded: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    onOpenMenu: () -> Unit,
    onDismissMenu: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val longClickLabel = stringResource(Res.string.flashcard_actions)
    val deleteLabel = stringResource(Res.string.flashcard_delete_accessibility)

    Box(modifier = modifier.fillMaxWidth()) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RepetrailTheme.shapes.card,
            color = RepetrailTheme.colors.surface,
            shadowElevation = RepetrailTheme.dimensions.elevations.card,
        ) {
            Column(
                modifier = Modifier
                    .semantics {
                        if (enabled) {
                            customActions = listOf(CustomAccessibilityAction(deleteLabel) {
                                onDelete()
                                true
                            })
                        }
                    }
                    .combinedClickable(
                        enabled = enabled,
                        onClick = onClick,
                        onLongClickLabel = longClickLabel,
                        onLongClick = onOpenMenu,
                    )
                    .padding(RepetrailTheme.dimensions.spacings.medium),
            ) {
                Text(
                    text = flashcard.term,
                    style = RepetrailTheme.typography.titleMedium,
                    color = RepetrailTheme.colors.ink,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    text = flashcard.definition,
                    modifier = Modifier.padding(top = RepetrailTheme.dimensions.spacings.extraSmall),
                    style = RepetrailTheme.typography.bodyMedium,
                    color = RepetrailTheme.colors.muted,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }

        DropdownMenu(expanded = menuExpanded, onDismissRequest = onDismissMenu) {
            DropdownMenuItem(
                text = { Text(stringResource(Res.string.flashcard_action_edit)) },
                enabled = enabled,
                onClick = onClick,
            )
            DropdownMenuItem(
                text = { Text(stringResource(Res.string.flashcard_action_delete)) },
                enabled = enabled,
                onClick = onDelete,
            )
        }
    }
}
