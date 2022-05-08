package io.chthonic.rickmortychars.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.chthonic.rickmortychars.domain.GetCharactersUsecase
import io.chthonic.rickmortychars.domain.model.CharacterInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCharactersUsecase: GetCharactersUsecase
) : ViewModel() {

    private val _loadingIsVisible = MutableStateFlow<Boolean>(false)
    val loadingIsVisible: StateFlow<Boolean>
        get() = _loadingIsVisible

    private val _characterInfosToDisplay = MutableStateFlow<List<CharacterInfo>?>(null)
    val characterInfosToDisplay: StateFlow<List<CharacterInfo>?>
        get() = _characterInfosToDisplay

    init {
        viewModelScope.launch {
            getCharacters()
        }
    }

    private suspend fun getCharacters() {
        _loadingIsVisible.value = true
        try {
            _characterInfosToDisplay.value = getCharactersUsecase.execute()
        } catch (e: Exception) {
            Log.e(MainViewModel::class.java.simpleName, "getCharactersUsecase failed", e)
            _characterInfosToDisplay.value = emptyList()
        }

        viewModelScope.launch {
            // Wait for results to render before hide loading indicator
            delay(250)
            _loadingIsVisible.value = false
        }
    }
}