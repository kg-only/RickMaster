package com.example.rickmaster.presentation.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.rickmaster.R

enum class TabElements {
    CAMERAS,
    DOORS
}

@Composable
fun defineText(tab: TabElements): String {
    return when (tab) {
        TabElements.CAMERAS -> stringResource(id = R.string.cameras)
        TabElements.DOORS -> stringResource(id = R.string.doors)
    }
}
