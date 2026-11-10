package com.breckneck.feature.flashcard.form.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.breckneck.core.ui.designsystem.components.RepetrailTextField
import com.breckneck.core.ui.designsystem.theme.RepetrailTheme
import com.breckneck.feature.flashcard.form.FlashcardFormIntent
import com.breckneck.feature.flashcard.form.FlashcardFormState
import com.breckneck.feature.flashcard.form.FlashcardFormUiState
import org.jetbrains.compose.resources.stringResource
import repetrail.feature.flashcard.generated.resources.Res
import repetrail.feature.flashcard.generated.resources.deck_detail_back
import repetrail.feature.flashcard.generated.resources.flashcard_form_definition_label
import repetrail.feature.flashcard.generated.resources.flashcard_form_definition_placeholder
import repetrail.feature.flashcard.generated.resources.flashcard_form_discard_confirm
import repetrail.feature.flashcard.generated.resources.flashcard_form_discard_message
import repetrail.feature.flashcard.generated.resources.flashcard_form_discard_stay
import repetrail.feature.flashcard.generated.resources.flashcard_form_discard_title
import repetrail.feature.flashcard.generated.resources.flashcard_form_edit_title
import repetrail.feature.flashcard.generated.resources.flashcard_form_example_label
import repetrail.feature.flashcard.generated.resources.flashcard_form_example_placeholder
import repetrail.feature.flashcard.generated.resources.flashcard_form_new_title
import repetrail.feature.flashcard.generated.resources.flashcard_form_save
import repetrail.feature.flashcard.generated.resources.flashcard_form_save_and_add
import repetrail.feature.flashcard.generated.resources.flashcard_form_term_label
import repetrail.feature.flashcard.generated.resources.flashcard_form_term_placeholder

private val BottomClearanceEditMode = 80.dp
private val BottomClearanceCreateMode = 132.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FlashcardFormContent(
    state: FlashcardFormState,
    onIntent: (FlashcardFormIntent) -> Unit,
    focusRequester: FocusRequester? = null,
    snackbarHostState: SnackbarHostState? = null,
) {
    val layoutDirection = LocalLayoutDirection.current
    val safePadding = WindowInsets.safeDrawing.asPaddingValues()
    val bottomClearance = if (state.isEditMode) BottomClearanceEditMode else BottomClearanceCreateMode

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(RepetrailTheme.colors.background),
    ) {
        if (state.uiState == FlashcardFormUiState.Loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = RepetrailTheme.colors.accent)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(
                        start = safePadding.calculateStartPadding(layoutDirection) +
                            RepetrailTheme.dimensions.spacings.large,
                        end = safePadding.calculateEndPadding(layoutDirection) +
                            RepetrailTheme.dimensions.spacings.large,
                        top = safePadding.calculateTopPadding() +
                            RepetrailTheme.dimensions.iconSizes.large +
                            RepetrailTheme.dimensions.spacings.extraLarge,
                        bottom = safePadding.calculateBottomPadding() + bottomClearance,
                    ),
            ) {
                Text(
                    text = stringResource(
                        if (state.isEditMode) Res.string.flashcard_form_edit_title else Res.string.flashcard_form_new_title,
                    ),
                    color = RepetrailTheme.colors.ink,
                    style = RepetrailTheme.typography.headingLarge,
                )

                Spacer(Modifier.height(RepetrailTheme.dimensions.spacings.large))

                RepetrailTextField(
                    value = state.term,
                    onValueChange = { onIntent(FlashcardFormIntent.TermChanged(it)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(if (focusRequester != null) Modifier.focusRequester(focusRequester) else Modifier),
                    label = { Text(stringResource(Res.string.flashcard_form_term_label)) },
                    placeholder = { Text(stringResource(Res.string.flashcard_form_term_placeholder)) },
                    isError = state.termError != null,
                    supportingText = state.termError?.let { errorText ->
                        {
                            Text(
                                text = errorText,
                                color = RepetrailTheme.colors.accent,
                                style = RepetrailTheme.typography.bodySmall,
                            )
                        }
                    },
                    singleLine = true,
                )

                Spacer(Modifier.height(RepetrailTheme.dimensions.spacings.medium))

                RepetrailTextField(
                    value = state.definition,
                    onValueChange = { onIntent(FlashcardFormIntent.DefinitionChanged(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(Res.string.flashcard_form_definition_label)) },
                    placeholder = { Text(stringResource(Res.string.flashcard_form_definition_placeholder)) },
                    isError = state.definitionError != null,
                    supportingText = state.definitionError?.let { errorText ->
                        {
                            Text(
                                text = errorText,
                                color = RepetrailTheme.colors.accent,
                                style = RepetrailTheme.typography.bodySmall,
                            )
                        }
                    },
                    minLines = 3,
                    maxLines = 5,
                )

                Spacer(Modifier.height(RepetrailTheme.dimensions.spacings.medium))

                RepetrailTextField(
                    value = state.example,
                    onValueChange = { onIntent(FlashcardFormIntent.ExampleChanged(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(Res.string.flashcard_form_example_label)) },
                    placeholder = { Text(stringResource(Res.string.flashcard_form_example_placeholder)) },
                    minLines = 2,
                    maxLines = 4,
                )
            }
        }

        val backLabel = stringResource(Res.string.deck_detail_back)
        Surface(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(
                    start = safePadding.calculateStartPadding(layoutDirection) +
                        RepetrailTheme.dimensions.spacings.medium,
                    top = safePadding.calculateTopPadding() +
                        RepetrailTheme.dimensions.spacings.small,
                ),
            shape = RepetrailTheme.shapes.pill,
            color = RepetrailTheme.colors.surface,
            shadowElevation = RepetrailTheme.dimensions.elevations.floating,
        ) {
            IconButton(
                onClick = { onIntent(FlashcardFormIntent.BackClicked) },
                enabled = state.uiState != FlashcardFormUiState.Saving,
                modifier = Modifier.semantics { contentDescription = backLabel },
            ) {
                Text("‹", style = RepetrailTheme.typography.headingLarge)
            }
        }

        if (state.uiState != FlashcardFormUiState.Loading) {
            val canSave = (state.term.isNotBlank()) &&
                (state.definition.isNotBlank()) &&
                (state.uiState != FlashcardFormUiState.Saving)

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(
                        start = safePadding.calculateStartPadding(layoutDirection) +
                            RepetrailTheme.dimensions.spacings.large,
                        end = safePadding.calculateEndPadding(layoutDirection) +
                            RepetrailTheme.dimensions.spacings.large,
                        bottom = safePadding.calculateBottomPadding() +
                            RepetrailTheme.dimensions.spacings.small,
                    ),
                verticalArrangement = Arrangement.spacedBy(RepetrailTheme.dimensions.spacings.small),
            ) {
                Button(
                    onClick = { onIntent(FlashcardFormIntent.SaveClicked) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = canSave,
                    colors = ButtonDefaults.buttonColors(containerColor = RepetrailTheme.colors.accent),
                    shape = RepetrailTheme.shapes.pill,
                ) {
                    if (state.uiState == FlashcardFormUiState.Saving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = RepetrailTheme.colors.surface,
                            strokeWidth = 2.dp,
                        )
                    } else {
                        Text(
                            text = stringResource(Res.string.flashcard_form_save),
                            style = RepetrailTheme.typography.bodyMedium,
                        )
                    }
                }

                if (!state.isEditMode) {
                    OutlinedButton(
                        onClick = { onIntent(FlashcardFormIntent.SaveAndAddAnotherClicked) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = canSave,
                        shape = RepetrailTheme.shapes.pill,
                    ) {
                        Text(
                            text = stringResource(Res.string.flashcard_form_save_and_add),
                            style = RepetrailTheme.typography.bodyMedium,
                            color = if (canSave) RepetrailTheme.colors.ink else RepetrailTheme.colors.muted,
                        )
                    }
                }
            }
        }

        if (snackbarHostState != null) {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(
                        bottom = safePadding.calculateBottomPadding() +
                            bottomClearance +
                            RepetrailTheme.dimensions.spacings.small,
                    ),
            )
        }
    }

    if (state.showDiscardDialog) {
        ModalBottomSheet(
            onDismissRequest = { onIntent(FlashcardFormIntent.CancelDiscard) },
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
                    text = stringResource(Res.string.flashcard_form_discard_title),
                    style = RepetrailTheme.typography.headingMedium,
                    color = RepetrailTheme.colors.ink,
                    textAlign = TextAlign.Center,
                )

                Spacer(Modifier.height(RepetrailTheme.dimensions.spacings.small))

                Text(
                    text = stringResource(Res.string.flashcard_form_discard_message),
                    style = RepetrailTheme.typography.bodyMedium,
                    color = RepetrailTheme.colors.muted,
                    textAlign = TextAlign.Center,
                )

                Spacer(Modifier.height(RepetrailTheme.dimensions.spacings.large))

                Button(
                    onClick = { onIntent(FlashcardFormIntent.ConfirmDiscard) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = RepetrailTheme.colors.accent),
                    shape = RepetrailTheme.shapes.pill,
                ) {
                    Text(
                        text = stringResource(Res.string.flashcard_form_discard_confirm),
                        style = RepetrailTheme.typography.bodyMedium,
                    )
                }

                Spacer(Modifier.height(RepetrailTheme.dimensions.spacings.small))

                OutlinedButton(
                    onClick = { onIntent(FlashcardFormIntent.CancelDiscard) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RepetrailTheme.shapes.pill,
                ) {
                    Text(
                        text = stringResource(Res.string.flashcard_form_discard_stay),
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
private fun FlashcardFormContentCreatePreview() {
    RepetrailTheme {
        FlashcardFormContent(
            state = FlashcardFormState(),
            onIntent = {},
        )
    }
}

@Preview
@Composable
private fun FlashcardFormContentEditPreview() {
    RepetrailTheme {
        FlashcardFormContent(
            state = FlashcardFormState(
                deckId = 1L,
                flashcardId = 10L,
                term = "Apple",
                definition = "A fruit",
                example = "I eat an apple",
                originalTerm = "Apple",
                originalDefinition = "A fruit",
                originalExample = "I eat an apple",
            ),
            onIntent = {},
        )
    }
}
