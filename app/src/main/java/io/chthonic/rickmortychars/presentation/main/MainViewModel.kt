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

    private val _errorMessageToDisplay = MutableStateFlow<String?>(null)
    val errorMessageToDisplay: StateFlow<String?>
        get() = _errorMessageToDisplay

    val characterInfosToDisplay: Flow<PagingData<CharacterInfo>> =
        getCharactersUsecase.execute()
            .cachedIn(viewModelScope)

    fun onLoadingStatesChanged(loadStates: CombinedLoadStates) {
        _loadingIsVisible.value = loadStates.refresh is LoadState.Loading
        _errorMessageToDisplay.value = when (val appendState = loadStates.append) {
            is LoadState.Error -> appendState.error.message ?: "Loading characters failed"
            else -> null
        }
    }

    fun onErrorDisplayed() {
        _errorMessageToDisplay.value = null
    }
}