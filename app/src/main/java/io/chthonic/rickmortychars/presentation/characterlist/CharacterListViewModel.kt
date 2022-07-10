package io.chthonic.rickmortychars.presentation.characterlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import io.chthonic.rickmortychars.domain.GetCharacterListUsecase
import io.chthonic.rickmortychars.domain.models.CharacterInfo
import io.chthonic.rickmortychars.presentation.Destination
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CharacterListViewModel @Inject constructor(
    getCharacterListUsecase: GetCharacterListUsecase
) : ViewModel() {

    val characterListToDisplay: Flow<PagingData<CharacterInfo>> =
        getCharacterListUsecase.execute()
            .cachedIn(viewModelScope)

    private val _navigate = MutableStateFlow<NavigationTarget?>(null)
    val navigate: StateFlow<NavigationTarget?>
        get() = _navigate.asStateFlow()

    fun onCharacterClick(charInfo: CharacterInfo) {
        _navigate.value = NavigationTarget.CharacterScreen(
            Destination.Character.CharacterArgument(
                id = charInfo.id,
                imageUrl = charInfo.image
            )
        )
    }

    fun onNavigationObserved() {
        _navigate.value = null
    }

    sealed class NavigationTarget() {
        data class CharacterScreen(
            val characterArgument: Destination.Character.CharacterArgument
        ) : NavigationTarget()
    }
}