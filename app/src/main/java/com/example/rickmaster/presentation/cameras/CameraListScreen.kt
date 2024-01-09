package com.example.rickmaster.presentation.cameras

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.rickmaster.data.models.cameras.CameraData
import com.example.rickmaster.util.ACTION_ITEM_SIZE
import com.example.rickmaster.util.ONE_ICON_OFFSET
import com.example.rickmaster.util.ShowToast
import com.example.rickmaster.util.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState



@Composable
fun CameraListScreen(viewModel: CameraViewModel = hiltViewModel()) {
    val cameraState by viewModel.cameraState.collectAsState()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (cameraState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
        } else if (cameraState.error.isNotEmpty()) {
            ShowToast(cameraState.error)
            viewModel.getDataFromDb()
        } else {
            cameraState.cameraData?.data?.let { data -> CameraList(viewModel, data) }
        }
    }
}

@Composable
private fun CameraList(viewModel: CameraViewModel, item: CameraData) {
    val revealedCardIds by viewModel.revealedCardIdsList.collectAsState()
    val cameraList = item.cameras
    SwipeRefresh(
        modifier = Modifier.fillMaxSize(),
        state = rememberSwipeRefreshState(isRefreshing = false),
        onRefresh = { viewModel.getCameraList() }) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(count = cameraList.size) {
                val currentItem = cameraList[it]
                currentItem?.let {
                    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                        val (card, actionIcon) = createRefs()

                        CameraActionRow(
                            modifier = Modifier
                                .constrainAs(actionIcon) {
                                    top.linkTo(parent.top)
                                    end.linkTo(parent.end)
                                    bottom.linkTo(parent.bottom)
                                }, isFavorite = currentItem.favorites,
                            onFavorite = { viewModel.setFavorite(currentItem.id) }
                        )
                        CameraCardDraggableElement(
                            modifier = Modifier
                                .constrainAs(card) {
                                    top.linkTo(parent.top)
                                    start.linkTo(parent.start)
                                    bottom.linkTo(parent.bottom)
                                },
                            item = currentItem,
                            isRevealed = revealedCardIds.contains(currentItem.id),
                            cardOffset = ONE_ICON_OFFSET.dp(),
                            onExpand = { viewModel.onItemExpanded(currentItem.id) },
                            onCollapse = { viewModel.onItemCollapsed(currentItem.id) },
                        )
                    }
                }
            }
        }
    }
}