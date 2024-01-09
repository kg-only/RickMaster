package com.example.rickmaster.data.converter

import com.example.rickmaster.data.models.doors.DoorsData
import com.example.rickmaster.data.models.doors.DoorsModelDto
import com.example.rickmaster.domain.model.DoorsDataRealm
import com.example.rickmaster.domain.model.DoorsModelDtoRealm
import io.realm.RealmList

fun DoorsModelDto.toRealmModel(): DoorsModelDtoRealm {
    return DoorsModelDtoRealm(
        success = this.success,
        data = RealmList<DoorsDataRealm>().apply {
            addAll(this@toRealmModel.data.map { it.toRealmModel() })
        }
    )
}

fun DoorsData.toRealmModel(): DoorsDataRealm {
    return DoorsDataRealm(
        name = this.name,
        room = this.room,
        snapshot = this.snapshot,
        id = this.id,
        favorites = this.favorites
    )
}

fun DoorsModelDtoRealm.toDto(): DoorsModelDto {
    return DoorsModelDto(
        success = this.success,
        data = this.data.map { it.toDto() }
    )
}

fun DoorsDataRealm.toDto(): DoorsData {
    return DoorsData(
        name = this.name,
        room = this.room,
        snapshot = this.snapshot,
        id = this.id,
        favorites = this.favorites
    )
}