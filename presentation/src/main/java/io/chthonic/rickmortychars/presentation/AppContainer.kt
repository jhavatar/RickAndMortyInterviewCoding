package io.chthonic.rickmortychars.presentation

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Preview
@Composable
fun AppContainer() {
    val appContainerState = rememberAppContainerState()
    Scaffold(
        scaffoldState = appContainerState.scaffoldState,
        topBar = {
            // your top bar
            val appBarTitle = appContainerState.showAppBarTitle.collectAsStateWithLifecycle()
            TopAppBar(title = { Text(appBarTitle.value ?: "") })
        },
        floatingActionButton = {
            // your floating action button
        },
        drawerContent = null,
        content = { padding ->
            // your page content
            AppContainerNavHost(
                appContainerState = appContainerState,
                padding = padding
            )
        },
        bottomBar = {
            // your bottom bar composable
        }
    )
}