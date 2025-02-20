package io.chthonic.rickmortychars.presentation.characterlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import io.chthonic.rickmortychars.domain.presentationapi.models.CharacterInfo
import io.chthonic.rickmortychars.presentation.R
import io.chthonic.rickmortychars.presentation.ktx.items
import io.chthonic.rickmortychars.presentation.nav.Destination
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

    viewModel.navigateSideEffect.collectAsStateWithLifecycle().value?.let { sideEffect ->
        when (val navTarget = sideEffect.getContentIfNotHandled()) {
            is CharacterListViewModel.NavigationTarget.CharacterScreen -> {
                navController.navigate(Destination.Character.buildUniqueRoute(navTarget.characterId))
            }

            else -> { // ignore
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
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        items(
            lazyCharInfoItems,
            itemKey = { it.id },
        ) { item ->
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
    Box {
        AsyncImage(
            model = charInfo.image,
            placeholder = painterResource(R.drawable.rickmortyplaceholder),
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
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 16.sp,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
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
        CharacterInfo(
            id = 1,
            name = "Rick",
            image = ""
        ),
        CharacterInfo(
            id = 2,
            name = "Morty",
            image = ""
        )
    )
}