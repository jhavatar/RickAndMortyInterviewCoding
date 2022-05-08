package io.chthonic.rickmortychars.domain

import androidx.paging.PagingData
import io.chthonic.rickmortychars.data.RickMortyRepository
import io.chthonic.rickmortychars.domain.model.CharacterInfo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCharactersUsecase @Inject constructor(
    private val rickMortyRepository: RickMortyRepository
) {
    fun execute(): Flow<PagingData<CharacterInfo>> =
        rickMortyRepository.getCharacters()
}