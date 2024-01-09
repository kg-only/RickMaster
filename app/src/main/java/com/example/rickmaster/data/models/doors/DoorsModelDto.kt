package com.example.rickmaster.data.models.doors

import kotlinx.serialization.Serializable

@Serializable
data class DoorsModelDto(
    var success: Boolean? = null,
    var data: List<DoorsData> = listOf()
)