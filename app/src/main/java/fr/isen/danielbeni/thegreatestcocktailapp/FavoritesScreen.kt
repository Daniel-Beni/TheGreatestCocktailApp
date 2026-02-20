package fr.isen.danielbeni.thegreatestcocktailapp

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import coil3.compose.AsyncImage
import fr.isen.danielbeni.thegreatestcocktailapp.dataClasses.Drink
import fr.isen.danielbeni.thegreatestcocktailapp.managers.FavoritesManager

@Composable
fun FavoritesScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val favoritesManager = remember { FavoritesManager() }

    // État observable pour la liste des favoris
    val favorites = remember { mutableStateOf<List<Drink>>(emptyList()) }

    /**
     * Pourquoi un LifecycleEventObserver au lieu de LaunchedEffect ?
     *
     * LaunchedEffect(Unit) ne s'exécute qu'UNE SEULE FOIS quand le composable apparaît.
     * Si l'utilisateur ajoute un favori dans DetailCocktailActivity puis revient
     * sur cet onglet, LaunchedEffect ne se relancera PAS → la liste sera périmée.
     *
     * LifecycleEventObserver écoute les événements du cycle de vie :
     * → ON_RESUME est déclenché à chaque fois qu'on revient sur l'écran
     * → On recharge les favoris à chaque retour = toujours à jour
     */
    val lifecycleOwner = LocalLifecycleOwner.current
    androidx.compose.runtime.DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                // Recharge les favoris à chaque fois que l'écran redevient visible
                favorites.value = favoritesManager.getFavorites(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        // onDispose = appelé quand le composable quitte la composition (nettoyage)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    if (favorites.value.isEmpty()) {
        // Message quand aucun favori
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Aucun favori pour le moment",
                    fontSize = 18.sp,
                    fontStyle = FontStyle.Italic,
                    color = colorResource(R.color.grey)
                )
                Text(
                    text = "Appuyez sur l'emoji like pour en ajouter",
                    fontSize = 14.sp,
                    color = colorResource(R.color.grey),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    } else {
        // Liste des favoris — même style que DrinksScreen
        LazyColumn(
            modifier = modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(favorites.value) { drink ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            // Ouvre le détail du cocktail favori
                            val intent = Intent(context, DetailCocktailActivity::class.java)
                            intent.putExtra("DRINK_ID", drink.idDrink)
                            intent.putExtra("DRINK_NAME", drink.strDrink)
                            context.startActivity(intent)
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Miniature du cocktail favori
                            AsyncImage(
                                model = drink.strDrinkThumb,
                                contentDescription = drink.strDrink,
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            )
                            Text(
                                text = drink.strDrink ?: "Inconnu",
                                fontSize = 16.sp
                            )
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Voir le détail",
                            tint = colorResource(R.color.grey)
                        )
                    }
                }
            }
        }
    }
}