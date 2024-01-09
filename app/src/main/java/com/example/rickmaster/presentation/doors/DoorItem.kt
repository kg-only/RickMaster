package com.example.rickmaster.presentation.doors

import android.annotation.SuppressLint
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.rickmaster.R
import com.example.rickmaster.data.models.cameras.Cameras
import com.example.rickmaster.data.models.doors.DoorsData
import com.example.rickmaster.presentation.cameras.ANIMATION_DURATION
import com.example.rickmaster.presentation.cameras.MIN_DRAG_AMOUNT
import com.example.rickmaster.presentation.ui.theme.colorBackground
import com.example.rickmaster.presentation.ui.theme.colorSurfaceOver
import com.example.rickmaster.presentation.ui.theme.colorSurfaceOverlay
import kotlin.math.roundToInt


@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun DoorCardDraggableElement(
    modifier: Modifier,
    item: DoorsData,
    isRevealed: Boolean,
    cardOffset: Float,
    onExpand: () -> Unit,
    onCollapse: () -> Unit,
) {
    val transitionState = remember {
        MutableTransitionState(isRevealed).apply {
            targetState = !isRevealed
        }
    }
    val transition = updateTransition(transitionState, "cardTransition")
    val offsetTransition by transition.animateFloat(
        label = "cardOffsetTransition",
        transitionSpec = { tween(durationMillis = ANIMATION_DURATION) },
        targetValueByState = { if (isRevealed) -cardOffset else 0f }, // Change here
    )
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .offset { IntOffset(offsetTransition.roundToInt(), 0) }
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    when {
                        dragAmount >= MIN_DRAG_AMOUNT -> onCollapse() // Change here
                        dragAmount < -MIN_DRAG_AMOUNT -> onExpand() // Change here
                    }
                }
            },
        content = {
            DoorsCardContent(item)
        }
    )
}

@Composable
fun DoorsCardContent(item: DoorsData) {
    Column{
        if (item.snapshot != null)
            AsyncImage(
                model = item.snapshot,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .weight(1f)
            ) {
                Text(text = item.name, fontSize = 16.sp)
                if (item.snapshot != null)
                    Text(
                        text = stringResource(id = R.string.online),
                        fontSize = 14.sp,
                        color = Color.LightGray
                    )
            }
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                modifier = Modifier.padding(end = 20.dp)
            )
        }
    }
}