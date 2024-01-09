package com.example.rickmaster.domain.model

import io.realm.RealmList
import io.realm.RealmObject

open class CameraModelDtoRealm(
    var success: Boolean = false,
    var data: CameraDataRealm? = CameraDataRealm() // Provide a default instance
) : RealmObject()

open class CameraDataRealm(
    var room: RealmList<String> = RealmList(),
    var cameras: RealmList<CamerasRealm> = RealmList()
) : RealmObject()

open class CamerasRealm(
    var name: String = "",
    var snapshot: String = "",
    var room: String? = null,
    var id: Int = 0,
    var favorites: Boolean = false,
    var rec: Boolean = false
) : RealmObject()
