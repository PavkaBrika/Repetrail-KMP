package com.breckneck.feature.deck.form.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.breckneck.core.ui.designsystem.theme.RepetrailTheme
import com.breckneck.feature.deck.form.DeckFormIntent
import com.breckneck.feature.deck.form.DeckFormState
import com.breckneck.feature.deck.form.DeckFormUiState
import org.jetbrains.compose.resources.stringResource
import repetrail.core.ui.designsystem.generated.resources.Res
import repetrail.core.ui.designsystem.generated.resources.deck_form_back_button
import repetrail.core.ui.designsystem.generated.resources.deck_form_description_label
import repetrail.core.ui.designsystem.generated.resources.deck_form_description_placeholder
import repetrail.core.ui.designsystem.generated.resources.deck_form_discard_dialog_cancel
import repetrail.core.ui.designsystem.generated.resources.deck_form_discard_dialog_confirm
import repetrail.core.ui.designsystem.generated.resources.deck_form_discard_dialog_message
import repetrail.core.ui.designsystem.generated.resources.deck_form_discard_dialog_title
import repetrail.core.ui.designsystem.generated.resources.deck_form_name_label
import repetrail.core.ui.designsystem.generated.resources.deck_form_name_placeholder
import repetrail.core.ui.designsystem.generated.resources.deck_form_save_button
import repetrail.core.ui.designsystem.generated.resources.deck_form_title_create
import repetrail.core.ui.designsystem.generated.resources.deck_form_title_edit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DeckFormContent(
    state: DeckFormState,
    onIntent: (DeckFormIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = RepetrailTheme.colors.background,
    ) { paddingValues ->
        if (state.uiState == DeckFormUiState.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = RepetrailTheme.colors.accent)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(
                        start = RepetrailTheme.dimensions.spacings.large,
                        top = RepetrailTheme.dimensions.spacings.medium,
                        end = RepetrailTheme.dimensions.spacings.large,
                        // 112.dp clearance above bottom navigation bar and floating action button area
                        bottom = 112.dp + WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding(),
                    ),
                verticalArrangement = Arrangement.spacedBy(RepetrailTheme.dimensions.spacings.medium),
            ) {
                TextButton(
                    onClick = { onIntent(DeckFormIntent.BackClicked) },
                ) {
                    Text(
                        text = stringResource(Res.string.deck_form_back_button),
                        color = RepetrailTheme.colors.accent,
                        style = RepetrailTheme.typography.bodyMedium,
                    )
                }

                Text(
                    text = stringResource(
                        if (state.isEditMode) Res.string.deck_form_title_edit else Res.string.deck_form_title_create,
                    ),
                    color = RepetrailTheme.colors.ink,
                    style = RepetrailTheme.typography.headingLarge,
                )

                Spacer(Modifier.height(RepetrailTheme.dimensions.spacings.small))

                OutlinedTextField(
                    value = state.name,
                    onValueChange = { name -> onIntent(DeckFormIntent.NameChanged(name)) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(Res.string.deck_form_name_label)) },
                    placeholder = { Text(stringResource(Res.string.deck_form_name_placeholder)) },
                    isError = state.nameError != null,
                    supportingText = state.nameError?.let { errorText ->
                        {
                            Text(
                                text = errorText,
                                color = RepetrailTheme.colors.accent,
                                style = RepetrailTheme.typography.bodySmall,
                            )
                        }
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RepetrailTheme.colors.accent,
                        unfocusedBorderColor = RepetrailTheme.colors.surfaceVariant,
                        focusedLabelColor = RepetrailTheme.colors.accent,
                        unfocusedLabelColor = RepetrailTheme.colors.muted,
                        focusedTextColor = RepetrailTheme.colors.ink,
                        unfocusedTextColor = RepetrailTheme.colors.ink,
                    ),
                )

                OutlinedTextField(
                    value = state.description,
                    onValueChange = { desc -> onIntent(DeckFormIntent.DescriptionChanged(desc)) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(Res.string.deck_form_description_label)) },
                    placeholder = { Text(stringResource(Res.string.deck_form_description_placeholder)) },
                    minLines = 3,
                    maxLines = 5,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RepetrailTheme.colors.accent,
                        unfocusedBorderColor = RepetrailTheme.colors.surfaceVariant,
                        focusedLabelColor = RepetrailTheme.colors.accent,
                        unfocusedLabelColor = RepetrailTheme.colors.muted,
                        focusedTextColor = RepetrailTheme.colors.ink,
                        unfocusedTextColor = RepetrailTheme.colors.ink,
                    ),
                )

                Spacer(Modifier.height(RepetrailTheme.dimensions.spacings.large))

                Button(
                    onClick = { onIntent(DeckFormIntent.SaveClicked) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = state.name.isNotBlank() && (state.uiState != DeckFormUiState.Saving),
                    colors = ButtonDefaults.buttonColors(containerColor = RepetrailTheme.colors.accent),
                ) {
                    if (state.uiState == DeckFormUiState.Saving) {
                        // Compact progress indicator sized for button height
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = RepetrailTheme.colors.surface,
                            strokeWidth = 2.dp,
                        )
                    } else {
                        Text(
                            text = stringResource(Res.string.deck_form_save_button),
                            style = RepetrailTheme.typography.bodyMedium,
                        )
                    }
                }
            }
        }
    }

    if (state.showDiscardDialog) {
        ModalBottomSheet(
            onDismissRequest = { onIntent(DeckFormIntent.CancelDiscard) },
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
                    text = stringResource(Res.string.deck_form_discard_dialog_title),
                    style = RepetrailTheme.typography.headingMedium,
                    color = RepetrailTheme.colors.ink,
                    textAlign = TextAlign.Center,
                )

                Spacer(Modifier.height(RepetrailTheme.dimensions.spacings.small))

                Text(
                    text = stringResource(Res.string.deck_form_discard_dialog_message),
                    style = RepetrailTheme.typography.bodyMedium,
                    color = RepetrailTheme.colors.muted,
                    textAlign = TextAlign.Center,
                )

                Spacer(Modifier.height(RepetrailTheme.dimensions.spacings.large))

                Button(
                    onClick = { onIntent(DeckFormIntent.ConfirmDiscard) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = RepetrailTheme.colors.accent),
                ) {
                    Text(
                        text = stringResource(Res.string.deck_form_discard_dialog_confirm),
                        style = RepetrailTheme.typography.bodyMedium,
                    )
                }

                Spacer(Modifier.height(RepetrailTheme.dimensions.spacings.small))

                OutlinedButton(
                    onClick = { onIntent(DeckFormIntent.CancelDiscard) },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = stringResource(Res.string.deck_form_discard_dialog_cancel),
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
private fun DeckFormContentCreatePreview() {
    RepetrailTheme {
        DeckFormContent(
            state = DeckFormState(),
            onIntent = {},
        )
    }
}

@Preview
@Composable
private fun DeckFormContentEditPreview() {
    RepetrailTheme {
        DeckFormContent(
            state = DeckFormState(
                deckId = 1L,
                name = "English · Basic Course",
                description = "Common phrases",
                originalName = "English · Basic Course",
                originalDescription = "Common phrases",
            ),
            onIntent = {},
        )
    }
}
