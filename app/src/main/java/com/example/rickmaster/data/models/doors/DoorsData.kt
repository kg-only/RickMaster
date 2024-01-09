package com.example.rickmaster.data.models.doors

import kotlinx.serialization.Serializable

@Serializable
data class DoorsData(
    var name: String,
    var room: String?=null,
    var snapshot: String?=null,
    var id: Int,
    var favorites: Boolean
)