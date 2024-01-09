package com.example.rickmaster.presentation.doors

import com.example.rickmaster.data.models.doors.DoorsModelDto

data class DoorsState(
    var isLoading: Boolean = false,
    val doorsData: DoorsModelDto? = null,
    val error: String = ""
)