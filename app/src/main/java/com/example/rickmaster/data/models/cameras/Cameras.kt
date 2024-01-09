package com.example.rickmaster.data.models.cameras

import kotlinx.serialization.Serializable

@Serializable
data class Cameras(
    var name: String,
    var snapshot: String,
    var room: String?,
    var id: Int,
    var favorites: Boolean,
    var rec: Boolean,
)
