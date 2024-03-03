package io.chthonic.rickmortychars.presentation.character

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import coil.compose.AsyncImage
import io.chthonic.rickmortychars.R
import io.chthonic.rickmortychars.presentation.ktx.collectAsStateLifecycleAware

@Composable
fun CharacterScreen(
    characterId: Int?,
    updateAppBarTitle: (String?) -> Unit
) {
    val viewModel =
        hiltViewModel<CharacterViewModel, CharacterViewModel.CharacterViewModelFactory> { factory ->
            factory.create(characterId)
        }

    val state = viewModel.state.collectAsStateLifecycleAware(
        initial = CharacterViewModel.State(),
        scope = viewModel.viewModelScope
    ).value

    LaunchedEffect(state.titleToShow) {
        updateAppBarTitle(state.titleToShow)
    }

    CharacterScreenContent(
        state.imageUrlToShow
    )
}

@Composable
private fun CharacterScreenContent(url: String) {
    AsyncImage(
        model = url,
        placeholder = painterResource(R.drawable.rickmortyplaceholder),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.Black),
    )
}