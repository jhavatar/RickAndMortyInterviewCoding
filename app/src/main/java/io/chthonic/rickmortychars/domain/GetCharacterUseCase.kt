package io.chthonic.rickmortychars.domain

import io.chthonic.rickmortychars.domain.dataapi.RickMortyRepository
import javax.inject.Inject

class GetCharacterUseCase @Inject constructor(
    private val rickMortyRepository: RickMortyRepository
) {
    suspend fun execute(characterId: Int) = rickMortyRepository.getCharacter(characterId)
}