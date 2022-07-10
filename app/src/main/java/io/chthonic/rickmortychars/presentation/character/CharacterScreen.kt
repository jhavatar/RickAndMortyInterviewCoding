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
    viewModel: CharacterViewModel = hiltViewModel(),
    updateAppBarTitle: (String?) -> Unit
) {
    viewModel.titleToShow.collectAsStateLifecycleAware(
        initial = null,
        scope = viewModel.viewModelScope
    ).value.let {
        LaunchedEffect(it) {
            updateAppBarTitle(it)
        }
    }
    CharacterScreenContent(
        viewModel.imageUrlToShow.collectAsStateLifecycleAware(
            initial = viewModel.imageUrlToShow.value
        ).value ?: ""
    )
}

@Composable
fun CharacterScreenContent(url: String) {
    AsyncImage(
        model = url,
        placeholder = painterResource(R.drawable.rickmoryplaceholder),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.Black),
    )
}