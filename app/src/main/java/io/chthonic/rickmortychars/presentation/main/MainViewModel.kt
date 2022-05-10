package io.chthonic.rickmortychars.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import io.chthonic.rickmortychars.domain.GetCharactersUsecase
import io.chthonic.rickmortychars.domain.model.CharacterInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    getCharactersUsecase: GetCharactersUsecase
) : ViewModel() {

    private val _loadingIsVisible = MutableStateFlow<Boolean>(true)
    val loadingIsVisible: StateFlow<Boolean>
        get() = _loadingIsVisible

    val characterInfosToDisplay: Flow<PagingData<CharacterInfo>> =
        getCharactersUsecase.execute()
            .cachedIn(viewModelScope)

    fun onLoadingStatesChanged(loadStates: CombinedLoadStates) {
        val showLoading = loadStates.refresh is LoadState.Loading
        _loadingIsVisible.value = showLoading
    }
}