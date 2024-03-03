package io.chthonic.rickmortychars.domain

import androidx.paging.PagingData
import io.chthonic.rickmortychars.domain.dataapi.RickMortyRepository
import io.chthonic.rickmortychars.domain.models.CharacterInfo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCharacterListUsecase @Inject constructor(
    private val rickMortyRepository: RickMortyRepository
) {
    fun execute(): Flow<PagingData<CharacterInfo>> =
        rickMortyRepository.getCharacters()
}