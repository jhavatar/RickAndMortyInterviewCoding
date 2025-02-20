package io.chthonic.rickmortychars.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.chthonic.rickmortychars.presentation.character.CharacterScreen
import io.chthonic.rickmortychars.presentation.characterlist.CharacterListScreen
import io.chthonic.rickmortychars.presentation.nav.Destination

@Composable
fun AppContainerNavHost(
    appContainerState: AppContainerState,
    modifier: Modifier,
) = NavHost(
    navController = appContainerState.navController,
    startDestination = Destination.CharacterList.route,
    modifier = modifier,
) {
    composable(
        route = Destination.CharacterList.route,
    ) {
        CharacterListScreen(
            showSnackbar = appContainerState::showSnackbar,
            navController = appContainerState.navController,
            updateAppBarTitle = appContainerState::updateAppBarTitle
        )
    }
    composable(
        route = Destination.Character.route,
        arguments = Destination.Character.arguments,
    ) { backStackEntry ->
        val charId = Destination.Character.getCharId(backStackEntry.arguments)
        CharacterScreen(
            characterId = charId,
            updateAppBarTitle = appContainerState::updateAppBarTitle,
        )
    }
}