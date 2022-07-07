package io.chthonic.rickmortychars.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.chthonic.rickmortychars.R
import io.chthonic.rickmortychars.presentation.characters.CharactersScreen

@Preview
@Composable
fun AppContainer() {
    val appContainerState = rememberAppContainerState()
    Scaffold(
        scaffoldState = appContainerState.scaffoldState,
        topBar = {
            // your top bar
            TopAppBar(title = { Text(stringResource(R.string.app_name)) })
        },
        floatingActionButton = {
            // your floating action button
        },
        drawerContent = null,
        content = { padding ->
            // your page content
            NavHost(
                navController = appContainerState.navController,
                startDestination = Destination.Characters.route,
                modifier = Modifier.padding(padding)
            ) {
                composable(Destination.Characters.route) { CharactersScreen(showSnackbar = appContainerState::showSnackbar) }
            }
        },
        bottomBar = {
            // your bottom bar composable
        }
    )
}

sealed class Destination(val route: String) {
    object Characters : Destination("characters")
}