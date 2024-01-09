package com.example.rickmaster.data.models.cameras

import kotlinx.serialization.Serializable

@Serializable
data class CameraData(
    var room: List<String> = listOf(),
    var cameras: List<Cameras?> = listOf()
)