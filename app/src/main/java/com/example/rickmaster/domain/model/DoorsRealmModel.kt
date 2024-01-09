package com.example.rickmaster.domain.model

import io.realm.RealmList
import io.realm.RealmObject

open class DoorsModelDtoRealm(
    var success: Boolean? = null,
    var data: RealmList<DoorsDataRealm> = RealmList()
) : RealmObject()

open class DoorsDataRealm(
    var name: String = "",
    var room: String? = null,
    var snapshot: String? = null,
    var id: Int = 0,
    var favorites: Boolean = false
) : RealmObject()