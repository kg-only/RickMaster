package com.example.rickmaster.presentation.doors

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.rickmaster.util.ACTION_ITEM_SIZE

@Composable
fun DoorActionRow(
    modifier: Modifier,
    isFavorite: Boolean,
    onEdit: () -> Unit,
    onFavorite: () -> Unit
) {
    Row(
        modifier = modifier.padding(vertical = 8.dp)
    ) {

        IconButton(
            modifier = Modifier.size(ACTION_ITEM_SIZE.dp),
            onClick = onEdit,
            content = {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    tint = Color.Gray,
                    contentDescription = "s",
                )
            }
        )

        IconButton(
            modifier = Modifier.size(ACTION_ITEM_SIZE.dp),
            onClick = onFavorite,
            content = {
                if (isFavorite)
                    Icon(
                        imageVector = Icons.Filled.Star,
                        tint = Color.Gray,
                        contentDescription = "s",
                    )
                else
                    Icon(
                        imageVector = Icons.Default.Add,
                        tint = Color.Gray,
                        contentDescription = "s",
                    )
            }
        )
    }
}