package io.chthonic.rickmortychars.domain

import io.chthonic.rickmortychars.data.RickMortyRepository
import io.chthonic.rickmortychars.domain.model.CharacterInfo
import javax.inject.Inject

class GetCharactersUsecase @Inject constructor(
    private val rickMortyRepository: RickMortyRepository
) {
    suspend fun execute(): List<CharacterInfo> =
        rickMortyRepository.getCharacters()
}