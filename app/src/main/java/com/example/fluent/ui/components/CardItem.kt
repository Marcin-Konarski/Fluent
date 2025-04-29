package com.example.fluent.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.fluent.data.Word
import com.example.fluent.ui.theme.LocalAppTheme

@Composable
fun CardItem(
    modifier: Modifier = Modifier,
    word: Word
) {
    val cardWidth = 300.dp
    val cardHeight = 200.dp
    val appTheme = LocalAppTheme.current

    Card(
        modifier = modifier.then(
            Modifier
                .width(cardWidth)
                .height(cardHeight)
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 20.dp),
        colors = CardDefaults.cardColors(containerColor = appTheme.primaryColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = word.word,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 32.sp
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = word.translation,
                style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    color = Color.LightGray,
                    fontSize = 24.sp
                )
            )
        }
    }
}


@Composable
fun FlashCardsContent(
    cardsData: List<Word>,
    onMoveToBack: (Int) -> Unit
) {
    if (cardsData.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                "No cards available",
                modifier = Modifier.align(Alignment.Center)
            )
        }
        return
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(top = 100.dp),
        contentAlignment = Alignment.Center
    ) {
        // Use the word.id as the key to ensure stable identity
        cardsData.forEachIndexed { idx, word ->
            key(word.id) {
                SwipeableCard(
                    order = idx,
                    word = word,
                    totalCards = cardsData.size,
                    onMoveToBack = { onMoveToBack(idx) }
                )
            }
        }
    }
}

@Composable
fun SwipeableCard(
    order: Int,
    word: Word,
    totalCards: Int,
    onMoveToBack: () -> Unit
) {
    val spacing = 8.dp
    val maxVisibleCards = 8

    // Keep track of this card's current order in the stack
    val currentOrder = remember(order, word.id) { order }

    val animatedYOffset by animateDpAsState(
        targetValue = if (currentOrder < maxVisibleCards) {
            spacing * currentOrder
        } else {
            spacing * (maxVisibleCards - 1)
        }
    )

    Box(
        modifier = Modifier
            .offset { IntOffset(x = 0, y = animatedYOffset.roundToPx()) }
            .swipeToBack { onMoveToBack() }
            // Update zIndex calculation to ensure proper stacking
            .zIndex((totalCards - currentOrder).toFloat())
    ) {
        CardItem(word = word)
    }
}