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
class CharacterViewModel @Inject constructor(
    savedState: SavedStateHandle,
    getCharacterUsecase: GetCharacterUseCase
) : ViewModel() {

    private val charArgument: Destination.Character.CharacterArgument? =
        savedState[Destination.Character.ARGUMENT_KEY]

    private val _imageUrlToShow = MutableStateFlow(charArgument?.imageUrl)
    val imageUrlToShow: StateFlow<String?>
        get() = _imageUrlToShow.asStateFlow()

    private val _titleToShow = MutableStateFlow<String?>(null)
    val titleToShow: StateFlow<String?>
        get() = _titleToShow.asStateFlow()

    init {
        viewModelScope.launch {
            charArgument?.id?.let {
                getCharacterUsecase.execute(it)?.let {
                    _titleToShow.value = it.name
                }
            }
        }
    }
}