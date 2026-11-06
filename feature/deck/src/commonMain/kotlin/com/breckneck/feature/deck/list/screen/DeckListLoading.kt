package com.breckneck.feature.deck.list.screen

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.tooling.preview.Preview
import com.breckneck.core.ui.designsystem.theme.RepetrailTheme

@Composable
internal fun DeckListLoading(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "Deck loading")
    val color by transition.animateColor(
        initialValue = RepetrailTheme.colors.surfaceVariant,
        targetValue = RepetrailTheme.colors.line,
        animationSpec = infiniteRepeatable(
            animation = tween(),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "Skeleton color",
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .clearAndSetSemantics {
                progressBarRangeInfo = ProgressBarRangeInfo.Indeterminate
            },
        contentPadding = PaddingValues(RepetrailTheme.dimensions.spacings.large),
    ) {
        items(count = 4) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RepetrailTheme.shapes.card,
                color = color,
            ) {
                Column(modifier = Modifier.padding(RepetrailTheme.dimensions.spacings.medium)) {
                    // Empty text measures the same font-scaled lines as a deck card.
                    Text(text = "", style = RepetrailTheme.typography.titleMedium)

                    Spacer(Modifier.height(RepetrailTheme.dimensions.spacings.extraSmall))

                    Text(text = "", minLines = 3, style = RepetrailTheme.typography.bodySmall)
                }
            }

            Spacer(Modifier.height(RepetrailTheme.dimensions.spacings.medium))
        }
    }
}

@Preview
@Composable
private fun DeckListLoadingPreview() {
    RepetrailTheme {
        DeckListLoading()
    }
}
