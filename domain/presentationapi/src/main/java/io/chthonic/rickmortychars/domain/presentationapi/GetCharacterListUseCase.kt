package io.chthonic.rickmortychars.domain.presentationapi

import androidx.paging.PagingData
import io.chthonic.rickmortychars.domain.presentationapi.models.CharacterInfo
import kotlinx.coroutines.flow.Flow

interface GetCharacterListUseCase {
    fun execute(): Flow<PagingData<CharacterInfo>>
}