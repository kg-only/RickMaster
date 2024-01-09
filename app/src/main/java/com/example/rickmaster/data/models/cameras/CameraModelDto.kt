package com.example.rickmaster.data.models.cameras

import kotlinx.serialization.Serializable

@Serializable
data class CameraModelDto(
    var success: Boolean,
    var data: CameraData? = CameraData()
)
