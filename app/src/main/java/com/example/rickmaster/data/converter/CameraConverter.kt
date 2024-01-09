package com.example.rickmaster.data.converter

import com.example.rickmaster.data.models.cameras.CameraData
import com.example.rickmaster.data.models.cameras.CameraModelDto
import com.example.rickmaster.data.models.cameras.Cameras
import com.example.rickmaster.domain.model.CameraDataRealm
import com.example.rickmaster.domain.model.CameraModelDtoRealm
import com.example.rickmaster.domain.model.CamerasRealm
import io.realm.RealmList


fun CameraModelDto.toRealmModel(): CameraModelDtoRealm {
    return CameraModelDtoRealm(
        success = this.success,
        data = this.data?.toRealmModel()
    )
}

fun CameraData.toRealmModel(): CameraDataRealm {
    return CameraDataRealm(
        room = RealmList(*this.room.toTypedArray()),
        cameras = RealmList(*this.cameras.map { it?.toRealmModel() }.toTypedArray())
    )
}

fun Cameras.toRealmModel(): CamerasRealm {
    return CamerasRealm(
        name = this.name,
        snapshot = this.snapshot,
        room = this.room,
        id = this.id,
        favorites = this.favorites,
        rec = this.rec
    )
}

fun CameraModelDtoRealm.toDto(): CameraModelDto {
    return CameraModelDto(
        success = this.success,
        data = this.data?.toDto()
    )
}

fun CameraDataRealm.toDto(): CameraData {
    return CameraData(
        room = this.room.toList(),
        cameras = this.cameras.map { it.toDto() }
    )
}

fun CamerasRealm.toDto(): Cameras {
    return Cameras(
        name = this.name,
        snapshot = this.snapshot,
        room = this.room,
        id = this.id,
        favorites = this.favorites,
        rec = this.rec
    )
}