package io.chthonic.rickmortychars.presentation.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import io.chthonic.rickmortychars.domain.GetCharactersUsecase
import io.chthonic.rickmortychars.domain.model.CharacterInfo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    getCharactersUsecase: GetCharactersUsecase
) : ViewModel() {

    val characterInfosToDisplay: Flow<PagingData<CharacterInfo>> =
        getCharactersUsecase.execute()
            .cachedIn(viewModelScope)
}