package com.example.rickmaster.presentation.doors

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.rickmaster.data.models.doors.DoorsModelDto
import com.example.rickmaster.presentation.doors.dialog.CustomAlertDialog
import com.example.rickmaster.util.ShowToast
import com.example.rickmaster.util.TWO_ICON_OFFSET
import com.example.rickmaster.util.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun DoorsListScreen(viewModel: DoorsViewModel = hiltViewModel()) {
    val doorState by viewModel.doorsState.collectAsState()
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (doorState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
        } else if (doorState.error.isNotEmpty()) {
            ShowToast(doorState.error)
            viewModel.getDataFromDb()
        } else {
            doorState.doorsData?.let { data -> DoorList(viewModel, data) }
        }
    }
}

@Composable
private fun DoorList(viewModel: DoorsViewModel, item: DoorsModelDto) {
    val revealedCardIds by viewModel.revealedCardIdsList.collectAsState()
    var showEditDialog by remember { mutableStateOf(false) }
    var selectedCardToEditId by remember { mutableIntStateOf(0) }
    val doorList = item.data

    SwipeRefresh(
        modifier = Modifier.fillMaxSize(),
        state = rememberSwipeRefreshState(isRefreshing = false),
        onRefresh = { viewModel.getDoorsList() }) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(count = doorList.size) {
                val currentItem = doorList[it]

                ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                    val (card, actionIcon) = createRefs()

                    DoorActionRow(
                        modifier = Modifier
                            .constrainAs(actionIcon) {
                                top.linkTo(parent.top)
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom)
                            }, isFavorite = currentItem.favorites,
                        onEdit = {
                            showEditDialog = true
                            selectedCardToEditId = currentItem.id
                        },
                        onFavorite = { viewModel.edit(currentItem.id) }
                    )

                    DoorCardDraggableElement(
                        modifier = Modifier
                            .constrainAs(card) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                bottom.linkTo(parent.bottom)
                            },
                        item = currentItem,
                        isRevealed = revealedCardIds.contains(currentItem.id),
                        cardOffset = TWO_ICON_OFFSET.dp(),
                        onExpand = { viewModel.onItemExpanded(currentItem.id) },
                        onCollapse = { viewModel.onItemCollapsed(currentItem.id) },
                    )
                }
            }
        }
    }

    if (showEditDialog) CustomAlertDialog(
        onConfirm = { newName ->
            Log.e("###", "LOG NAHUI $newName")
            viewModel.edit(selectedCardToEditId, newName)
            viewModel.clearExpandedItems()
            showEditDialog = false
        },
        onDismiss = { showEditDialog = false }
    )
}