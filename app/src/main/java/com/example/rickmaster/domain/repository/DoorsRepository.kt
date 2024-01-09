package com.example.rickmaster.domain.repository

import com.example.rickmaster.data.models.doors.DoorsModelDto
import com.example.rickmaster.util.Resource
import kotlinx.coroutines.flow.Flow

interface DoorsRepository {
    fun fetchDoors(): Flow<Resource<DoorsModelDto>>
    fun fetchDoorsFromDataBase(): Flow<DoorsModelDto?>
    suspend fun edit(doorId: Int, newName: String?): DoorsModelDto?
}