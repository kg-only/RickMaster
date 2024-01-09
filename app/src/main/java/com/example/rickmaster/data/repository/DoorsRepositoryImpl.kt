package com.example.rickmaster.data.repository

import com.example.rickmaster.api.ApiRoutes
import com.example.rickmaster.data.converter.toDto
import com.example.rickmaster.data.converter.toRealmModel
import com.example.rickmaster.data.models.doors.DoorsModelDto
import com.example.rickmaster.domain.model.DoorsDataRealm
import com.example.rickmaster.domain.model.DoorsModelDtoRealm
import com.example.rickmaster.domain.repository.DoorsRepository
import com.example.rickmaster.util.Resource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.realm.Realm
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DoorsRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient,
    private val realm: Realm
) : DoorsRepository {
    override fun fetchDoors(): Flow<Resource<DoorsModelDto>> = flow {
        return@flow try {
            emit(Resource.Loading(null))
            val response: DoorsModelDto = httpClient.get(ApiRoutes.DOORS).body()
            val toDataBaseModel = response.toRealmModel()
            realm.executeTransactionAsync { it.insertOrUpdate(toDataBaseModel) } // save to db
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error("Failed to fetch cameras: ${e.message}"))
        }
    }

    override fun fetchDoorsFromDataBase(): Flow<DoorsModelDto?> = flow {
        val dbResult = realm.where(DoorsModelDtoRealm::class.java).findFirst()
        return@flow if (dbResult != null) emit(dbResult.toDto())
        else emit(null)
    }

    override suspend fun edit(doorId: Int, newName: String?): DoorsModelDto? {
        val deferred = CompletableDeferred<DoorsModelDto?>()

        realm.executeTransactionAsync({ realm ->
            val updateDoor: DoorsDataRealm? =
                realm.where(DoorsDataRealm::class.java).equalTo("id", doorId).findFirst()
            if (newName != null) updateDoor?.let { it.name = newName }
            else updateDoor?.let { it.favorites = !it.favorites }
        }, {
            val result = realm.where(DoorsModelDtoRealm::class.java).findFirst()?.toDto()
            deferred.complete(result)
        }, {
            deferred.completeExceptionally(it)
        })

        return deferred.await()
    }
}
