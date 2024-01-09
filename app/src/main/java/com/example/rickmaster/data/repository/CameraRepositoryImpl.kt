package com.example.rickmaster.data.repository

import com.example.rickmaster.api.ApiRoutes
import com.example.rickmaster.data.converter.toDto
import com.example.rickmaster.data.converter.toRealmModel
import com.example.rickmaster.data.models.cameras.CameraModelDto
import com.example.rickmaster.domain.model.CameraModelDtoRealm
import com.example.rickmaster.domain.model.CamerasRealm
import com.example.rickmaster.domain.repository.CameraRepository
import com.example.rickmaster.util.Resource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.realm.Realm
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CameraRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient,
    private val realm: Realm
) : CameraRepository {

    override fun fetchCameras(): Flow<Resource<CameraModelDto>> = flow {
        return@flow try {
            emit(Resource.Loading(null))
            val response: CameraModelDto = httpClient.get(ApiRoutes.CAMERAS).body()
            val toDataBaseModel = response.toRealmModel()
            realm.executeTransactionAsync { it.insertOrUpdate(toDataBaseModel) } // save to db
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error("Failed to fetch cameras: ${e.message}"))
        }
    }

    override fun fetchCameraFromDataBase(): Flow<CameraModelDto?> = flow {
        val dbResult = realm.where(CameraModelDtoRealm::class.java).findFirst()
        return@flow if (dbResult != null) emit(dbResult.toDto())
        else emit(null)
    }

    override suspend fun setFavorite(camId: Int): CameraModelDto? {
        val deferred = CompletableDeferred<CameraModelDto?>()

        realm.executeTransactionAsync({ realm ->
            val updatedCamera: CamerasRealm? =
                realm.where(CamerasRealm::class.java).equalTo("id", camId).findFirst()

            updatedCamera?.let { it.favorites = !it.favorites }
        }, {
            val result = realm.where(CameraModelDtoRealm::class.java).findFirst()?.toDto()
            deferred.complete(result)
        }, {
            deferred.completeExceptionally(it)
        })

        return deferred.await()
    }
}