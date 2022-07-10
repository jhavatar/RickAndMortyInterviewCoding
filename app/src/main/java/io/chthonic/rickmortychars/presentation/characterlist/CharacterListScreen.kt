package io.chthonic.rickmortychars.presentation.characterlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.bundleOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import io.chthonic.rickmortychars.R
import io.chthonic.rickmortychars.domain.models.CharacterInfo
import io.chthonic.rickmortychars.presentation.Destination
import io.chthonic.rickmortychars.presentation.ktx.collectAsStateLifecycleAware
import io.chthonic.rickmortychars.presentation.ktx.items
import io.chthonic.rickmortychars.presentation.ktx.navigateWithObject
import io.chthonic.rickmortychars.presentation.theme.WhiteTrans50
import io.chthonic.rickmortychars.presentation.views.LoadingProgress
import kotlinx.coroutines.flow.flowOf

@Composable
fun CharacterListScreen(
    viewModel: CharacterListViewModel = hiltViewModel(),
    showSnackbar: (String, SnackbarDuration) -> Unit,
    updateAppBarTitle: (String?) -> Unit,
    navController: NavController
) {
    val context = LocalContext.current
    LaunchedEffect(viewModel) {
        updateAppBarTitle(context.resources.getString(R.string.app_name))
    }

    viewModel.navigate.collectAsStateLifecycleAware(
        initial = null,
        scope = viewModel.viewModelScope
    ).value?.let { navTarget ->
        when (navTarget) {
            is CharacterListViewModel.NavigationTarget.CharacterScreen -> {
                viewModel.onNavigationObserved()
                navController.navigateWithObject(
                    route = Destination.Character.route,
                    arguments = bundleOf(
                        Destination.Character.ARGUMENT_KEY to navTarget.characterArgument
                    )
                )
            }
        }
    }

    val lazyCharInfoItems = viewModel.characterListToDisplay.collectAsLazyPagingItems()
    CharacterListContent(lazyCharInfoItems) { charInfo ->
        viewModel.onCharacterClick(charInfo)
    }

    when (val loadState = lazyCharInfoItems.loadState.refresh) {
        is LoadState.Loading -> LoadingProgress()
        is LoadState.Error -> showSnackbar(
            loadState.error.message ?: "Loading characters failed",
            SnackbarDuration.Short
        )
        else -> {}
    }
    when (val loadState = lazyCharInfoItems.loadState.append) {
        is LoadState.Error -> showSnackbar(
            loadState.error.message ?: "Loading characters failed",
            SnackbarDuration.Short
        )
        else -> {}
    }
}

@Composable
private fun CharacterListContent(
    lazyCharInfoItems: LazyPagingItems<CharacterInfo>,
    onClick: (CharacterInfo) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxWidth().fillMaxHeight()
    ) {
        items(lazyCharInfoItems) { item ->
            item?.let {
                CharacterItem(it) { onClick(it) }
            }
        }
    }
}

@Composable
private fun CharacterItem(
    charInfo: CharacterInfo,
    onClick: () -> Unit
) {
    Box() {
        AsyncImage(
            model = charInfo.image,
            placeholder = painterResource(R.drawable.rickmoryplaceholder),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(256.dp)
                .background(Color.Black)
                .clickable {
                    onClick()
                },
        )
        Box(
            Modifier
                .align(Alignment.BottomEnd)
                .padding(PaddingValues(start = 16.dp, top = 8.dp, bottom = 8.dp, end = 8.dp))
        ) {
            Text(
                text = "${charInfo.id} ${charInfo.name}",
                maxLines = 1,
                color = MaterialTheme.colors.primaryVariant,
                fontSize = 16.sp,
                modifier = Modifier
                    .background(WhiteTrans50)
                    .padding(2.dp)
            )
        }
    }
}

@Preview
@Composable
private fun PreviewCharacterListContent() {
    CharacterListContent(
        flowOf(
            PagingData.from(
                CharInfoPreviewProvider().values.toList()
            )
        ).collectAsLazyPagingItems()
    ) {}
}

@Preview
@Composable
private fun PreviewCharacterItem() {
    CharacterItem(
        CharInfoPreviewProvider().values.first()
    ) {}
}

private class CharInfoPreviewProvider : PreviewParameterProvider<CharacterInfo> {
    override val values: Sequence<CharacterInfo> = sequenceOf(
        CharacterInfo(id = 1, name = "Rick", image = ""),
        CharacterInfo(id = 2, name = "Morty", image = "")
    )
}