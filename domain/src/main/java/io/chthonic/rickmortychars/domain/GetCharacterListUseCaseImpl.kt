package io.chthonic.rickmortychars.domain

import androidx.paging.PagingData
import androidx.paging.map
import io.chthonic.rickmortychars.domain.presentationapi.GetCharacterListUseCase
import io.chthonic.rickmortychars.domain.presentationapi.models.CharacterInfo
import io.chthonic.rickmortychars.domain.dataapi.RickMortyRepository
import io.chthonic.rickmortychars.domain.ktx.toPresentationModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class GetCharacterListUseCaseImpl @Inject constructor(
    private val rickMortyRepository: RickMortyRepository
) : GetCharacterListUseCase {
    override fun execute(): Flow<PagingData<CharacterInfo>> =
        rickMortyRepository.getCharacters().map {
            it.map { info ->
                info.toPresentationModel()
            }
        }
}