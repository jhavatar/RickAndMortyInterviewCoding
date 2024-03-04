package io.chthonic.rickmortychars.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.chthonic.rickmortychars.presentation.character.CharacterScreen
import io.chthonic.rickmortychars.presentation.characterlist.CharacterListScreen
import io.chthonic.rickmortychars.presentation.nav.Destination

@Composable
fun AppContainerNavHost(
    appContainerState: AppContainerState,
    padding: PaddingValues
) = NavHost(
    navController = appContainerState.navController,
    startDestination = Destination.CharacterList.route,
    modifier = androidx.compose.ui.Modifier.padding(padding)
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