package io.chthonic.rickmortychars.presentation.character

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.chthonic.rickmortychars.domain.GetCharacterUseCase
import io.chthonic.rickmortychars.presentation.Destination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel constructor(
    savedState: SavedStateHandle,
    getCharacterUsecase: GetCharacterUseCase,
    initStateState: State
) : ViewModel() {

    @Inject
    constructor(
        savedState: SavedStateHandle,
        getCharacterUsecase: GetCharacterUseCase
    ) : this(
        savedState = savedState,
        getCharacterUsecase = getCharacterUsecase,
        initStateState = State()
    )

    data class State(
        val imageUrlToShow: String = "",
        val titleToShow: String = ""
    )

    private val charArgument: Destination.Character.CharacterArgument? =
        savedState[Destination.Character.ARGUMENT_KEY]

    private val _state = MutableStateFlow(
        initStateState.copy(
            imageUrlToShow = charArgument?.imageUrl ?: initStateState.imageUrlToShow
        )
    )
    val state: StateFlow<State> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            charArgument?.id?.let {
                getCharacterUsecase.execute(it)?.let {
                    _state.value = state.value.copy(titleToShow = it.name)
                }
            }
        }
    }
}