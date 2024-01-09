package com.example.rickmaster.util

import android.content.res.Resources
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun ShowToast(message: String) {
    val context = LocalContext.current
    val duration: Int = Toast.LENGTH_SHORT
    Toast.makeText(context, message, duration).show()
}

fun Float.dp(): Float = this * density + 0.5f

val density: Float
    get() = Resources.getSystem().displayMetrics.density

const val ACTION_ITEM_SIZE = 56
const val ONE_ICON_OFFSET = 56F // we have 3 icons in a row, so that's 56 * 3
const val TWO_ICON_OFFSET = 112F // we have 3 icons in a row, so that's 56 * 3
