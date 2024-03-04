package io.chthonic.rickmortychars.domain.ktx

import io.chthonic.rickmortychars.domain.presentationapi.models.CharacterInfo

fun io.chthonic.rickmortychars.domain.dataapi.models.CharacterInfo.toPresentationModel() =
    CharacterInfo(
        id = id,
        name = name,
        image = image,
    )