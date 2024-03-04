package io.chthonic.rickmortychars.domain.presentationapi

import io.chthonic.rickmortychars.domain.presentationapi.models.CharacterInfo

interface GetCharacterUseCase {
    suspend fun execute(characterId: Int): CharacterInfo?
}