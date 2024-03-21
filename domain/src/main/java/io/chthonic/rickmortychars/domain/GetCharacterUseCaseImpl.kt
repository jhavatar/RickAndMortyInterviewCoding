package io.chthonic.rickmortychars.domain

import io.chthonic.rickmortychars.domain.dataapi.RickMortyRepository
import io.chthonic.rickmortychars.domain.ktx.toPresentationModel
import io.chthonic.rickmortychars.domain.presentationapi.GetCharacterUseCase
import io.chthonic.rickmortychars.domain.presentationapi.models.CharacterInfo
import javax.inject.Inject

internal class GetCharacterUseCaseImpl @Inject constructor(
    private val rickMortyRepository: RickMortyRepository
) : GetCharacterUseCase {
    override suspend fun execute(characterId: Int): CharacterInfo? =
        rickMortyRepository.getCharacter(characterId)?.toPresentationModel()
}