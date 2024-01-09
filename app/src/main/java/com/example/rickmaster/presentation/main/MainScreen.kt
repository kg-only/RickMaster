package com.example.rickmaster.presentation.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rickmaster.R
import com.example.rickmaster.presentation.cameras.CameraListScreen
import com.example.rickmaster.presentation.doors.DoorsListScreen
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen() {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState( pageCount = {TabElements.values().size})
    val selectedTabIndex = remember { derivedStateOf { pagerState.currentPage } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = stringResource(id = R.string.my_house), fontSize = 18.sp)
        Spacer(modifier = Modifier.height(30.dp))

        TabRow(selectedTabIndex = selectedTabIndex.value, modifier = Modifier.fillMaxWidth()) {
            TabElements.values().forEachIndexed { index, tabElements ->
                Tab(selected = selectedTabIndex.value == index,
                    onClick = {
                        scope.launch { pagerState.animateScrollToPage(tabElements.ordinal) }
                    },
                    text = { Text(text = defineText(tabElements)) }
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { page ->
            when (page) {
                0 -> CameraListScreen()
                1 -> DoorsListScreen()
            }
        }
    }
}

