package fr.isen.danielbeni.thegreatestcocktailapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.isen.danielbeni.thegreatestcocktailapp.ui.theme.TheGreatestCocktailAppTheme

class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val navController = rememberNavController()

            // Définition des 3 onglets
            val randomTab = TabBarItem(
                title = stringResource(R.string.tab_random),
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home
            )
            val categoriesTab = TabBarItem(
                title = stringResource(R.string.tab_categories),
                selectedIcon = Icons.Filled.Menu,
                unselectedIcon = Icons.Outlined.Menu
            )
            val favoritesTab = TabBarItem(
                title = stringResource(R.string.tab_favorites),
                selectedIcon = Icons.Filled.Favorite,
                unselectedIcon = Icons.Outlined.Favorite
            )
            val tabItems = listOf(randomTab, categoriesTab, favoritesTab)

            TheGreatestCocktailAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(stringResource(R.string.app_name))
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = colorResource(R.color.purple_700),
                                titleContentColor = colorResource(R.color.white),
                                actionIconContentColor = colorResource(R.color.white)
                            ),
                            actions = {
                                IconButton(onClick = {
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.favorite_added),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.FavoriteBorder,
                                        contentDescription = "Add to favorites"
                                    )
                                }
                            }
                        )
                    },
                    bottomBar = {
                        BottomNavigationBar(tabItems, navController)
                    }
                ) { innerPadding ->
                    // NavHost = le conteneur qui affiche l'écran correspondant à l'onglet
                    NavHost(
                        navController = navController,
                        startDestination = randomTab.title
                    ) {
                        composable(randomTab.title) {
                            DetailCocktailScreen(
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                        composable(categoriesTab.title) {
                            CategoriesScreen(
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                        composable(favoritesTab.title) {
                            FavoritesScreen(
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart - activity est visible")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume - activity est en premier plan")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause - activity est en pause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop - activity est en arrière plan")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart - activity redevient visible")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy - activity est détruite")
    }
}