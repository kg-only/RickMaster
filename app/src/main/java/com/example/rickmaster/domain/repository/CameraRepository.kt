package com.example.rickmaster.domain.repository

import com.example.rickmaster.data.models.cameras.CameraModelDto
import com.example.rickmaster.util.Resource
import kotlinx.coroutines.flow.Flow

interface CameraRepository {
    fun fetchCameras(): Flow<Resource<CameraModelDto>>
    fun fetchCameraFromDataBase(): Flow<CameraModelDto?>
    suspend fun setFavorite(camId: Int): CameraModelDto?
}