package io.chthonic.rickmortychars.presentation

import android.os.Parcelable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.chthonic.rickmortychars.presentation.character.CharacterScreen
import io.chthonic.rickmortychars.presentation.characterlist.CharacterListScreen
import kotlinx.parcelize.Parcelize

@Composable
fun AppContainerNavHost(
    appContainerState: AppContainerState,
    padding: PaddingValues
) = NavHost(
    navController = appContainerState.navController,
    startDestination = Destination.CharacterList.route,
    modifier = androidx.compose.ui.Modifier.padding(padding)
) {
    composable(Destination.CharacterList.route) {
        CharacterListScreen(
            showSnackbar = appContainerState::showSnackbar,
            navController = appContainerState.navController,
            updateAppBarTitle = appContainerState::updateAppBarTitle

        )
    }
    composable(Destination.Character.route) {
        CharacterScreen(updateAppBarTitle = appContainerState::updateAppBarTitle)
    }
}

sealed class Destination(val route: String) {
    object CharacterList : Destination("characterlist")

    object Character : Destination("character") {
        const val ARGUMENT_KEY: String = "charArg"

        @Parcelize
        data class CharacterArgument(
            val id: Int,
            val imageUrl: String,
        ) : Parcelable
    }
}