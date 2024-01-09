package com.example.rickmaster.presentation.cameras

import com.example.rickmaster.data.models.cameras.CameraModelDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class CameraState(
    var isLoading: Boolean = false,
    val cameraData: CameraModelDto? = null,
    val error: String = ""
)
