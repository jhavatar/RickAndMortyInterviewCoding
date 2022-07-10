package io.chthonic.rickmortychars.presentation.characters

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import io.chthonic.rickmortychars.domain.model.CharacterInfo
import io.chthonic.rickmortychars.presentation.ktx.items
import io.chthonic.rickmortychars.presentation.theme.WhiteTrans50
import io.chthonic.rickmortychars.presentation.view.LoadingProgress
import kotlinx.coroutines.flow.flowOf

@Composable
fun CharactersScreen(
    viewModel: CharactersViewModel = hiltViewModel(),
    showSnackbar: (String, SnackbarDuration) -> Unit,
) {
    val lazyCharInfoItems = viewModel.characterInfosToDisplay.collectAsLazyPagingItems()
    MainContent(lazyCharInfoItems)
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
private fun MainContent(lazyCharInfoItems: LazyPagingItems<CharacterInfo>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
    ) {
        items(lazyCharInfoItems) { item ->
            item?.let {
                CharacterItem(it)
            }
        }
    }
}

@Preview
@Composable
private fun CharacterItem(
    @PreviewParameter(
        CharInfoPreviewProvider::class,
        1
    ) charInfo: CharacterInfo
) {
    Box {
        AsyncImage(
            model = charInfo.image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(256.dp)
                .background(Color.Black),
        )
        Box(
            Modifier
                .align(Alignment.BottomEnd)
                .padding(PaddingValues(start = 16.dp, top = 8.dp, bottom = 8.dp, end = 8.dp))
        ) {
            Text(
                text = "${charInfo.id} ${charInfo.name}",
                maxLines = 1,
                color = MaterialTheme.colors.primary,
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
private fun PreviewMainContent() {
    MainContent(
        flowOf(
            PagingData.from(
                CharInfoPreviewProvider().values.toList()
            )
        ).collectAsLazyPagingItems()
    )
}

private class CharInfoPreviewProvider : PreviewParameterProvider<CharacterInfo> {
    override val values: Sequence<CharacterInfo> = sequenceOf(
        CharacterInfo(id = 0, name = "Rick", image = ""),
        CharacterInfo(id = 1, name = "Morty", image = "")
    )
}