package fr.isen.danielbeni.thegreatestcocktailapp

import android.content.Intent
import android.util.Log
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import fr.isen.danielbeni.thegreatestcocktailapp.dataClasses.CocktailResponse
import fr.isen.danielbeni.thegreatestcocktailapp.dataClasses.DrinkFilterResponse
import fr.isen.danielbeni.thegreatestcocktailapp.dataClasses.DrinkPreview
import fr.isen.danielbeni.thegreatestcocktailapp.network.NetworkManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Enum pour les modes de recherche.
 * Permet de basculer entre recherche par nom et par ingrédient.
 */
enum class SearchMode(val label: String) {
    BY_NAME("Par nom"),
    BY_INGREDIENT("Par ingrédient")
}

@Composable
fun SearchScreen(modifier: Modifier = Modifier) {
    // Texte saisi par l'utilisateur dans la barre de recherche
    val query = remember { mutableStateOf("") }

    // Mode de recherche sélectionné (nom ou ingrédient)
    val searchMode = remember { mutableStateOf(SearchMode.BY_NAME) }

    // Résultats sous forme de DrinkPreview (format commun aux deux modes)
    val results = remember { mutableStateOf<List<DrinkPreview>>(emptyList()) }

    // États de chargement et message "aucun résultat"
    val isLoading = remember { mutableStateOf(false) }
    val hasSearched = remember { mutableStateOf(false) }

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    /**
     * Fonction de recherche — appelée quand l'utilisateur valide.
     *
     * Selon le mode sélectionné, appelle un endpoint différent :
     * - BY_NAME → searchByName() → CocktailResponse (Drink complets)
     * - BY_INGREDIENT → searchByIngredient() → DrinkFilterResponse (DrinkPreview)
     *
     * Dans les deux cas, on convertit les résultats en List<DrinkPreview>
     * pour un affichage uniforme dans la LazyColumn.
     */
    fun performSearch() {
        val searchText = query.value.trim()
        if (searchText.isEmpty()) return

        isLoading.value = true
        hasSearched.value = true
        keyboardController?.hide()

        when (searchMode.value) {
            SearchMode.BY_NAME -> {
                NetworkManager.api.searchByName(searchText).enqueue(
                    object : Callback<CocktailResponse> {
                        override fun onResponse(
                            call: Call<CocktailResponse>,
                            response: Response<CocktailResponse>
                        ) {
                            // Convertit les Drink complets en DrinkPreview pour l'affichage
                            // On extrait juste les 3 champs nécessaires pour la liste
                            val drinks = response.body()?.drinks ?: emptyList()
                            results.value = drinks.map { drink ->
                                DrinkPreview(
                                    idDrink = drink.idDrink,
                                    strDrink = drink.strDrink,
                                    strDrinkThumb = drink.strDrinkThumb
                                )
                            }
                            isLoading.value = false
                        }

                        override fun onFailure(call: Call<CocktailResponse>, t: Throwable) {
                            Log.e("SearchScreen", "Erreur recherche par nom: ${t.message}")
                            results.value = emptyList()
                            isLoading.value = false
                        }
                    }
                )
            }

            SearchMode.BY_INGREDIENT -> {
                NetworkManager.api.searchByIngredient(searchText).enqueue(
                    object : Callback<DrinkFilterResponse> {
                        override fun onResponse(
                            call: Call<DrinkFilterResponse>,
                            response: Response<DrinkFilterResponse>
                        ) {
                            // Déjà au format DrinkPreview, on utilise directement
                            results.value = response.body()?.drinks ?: emptyList()
                            isLoading.value = false
                        }

                        override fun onFailure(call: Call<DrinkFilterResponse>, t: Throwable) {
                            Log.e("SearchScreen", "Erreur recherche par ingrédient: ${t.message}")
                            results.value = emptyList()
                            isLoading.value = false
                        }
                    }
                )
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // ── Barre de recherche ──────────────────────────────
        OutlinedTextField(
            value = query.value,
            onValueChange = { query.value = it },
            label = {
                Text(
                    if (searchMode.value == SearchMode.BY_NAME)
                        "Nom du cocktail..."
                    else
                        "Nom de l'ingrédient..."
                )
            },
            leadingIcon = {
                Icon(Icons.Filled.Search, contentDescription = "Rechercher")
            },
            trailingIcon = {
                // Bouton X pour effacer le texte
                if (query.value.isNotEmpty()) {
                    IconButton(onClick = {
                        query.value = ""
                        results.value = emptyList()
                        hasSearched.value = false
                    }) {
                        Icon(Icons.Filled.Clear, contentDescription = "Effacer")
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            // keyboardOptions configure le clavier virtuel
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search  // affiche l'icône 🔍 sur le clavier
            ),
            // keyboardActions définit l'action quand l'utilisateur appuie sur 🔍
            keyboardActions = KeyboardActions(
                onSearch = { performSearch() }
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // ── Toggle mode de recherche (FilterChip) ───────────
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            SearchMode.entries.forEach { mode ->
                FilterChip(
                    selected = searchMode.value == mode,
                    onClick = {
                        searchMode.value = mode
                        // Relance la recherche si on change de mode avec du texte
                        if (query.value.isNotBlank()) {
                            performSearch()
                        }
                    },
                    label = { Text(mode.label) }
                )
            }
        }

        // ── Zone de résultats ───────────────────────────────
        when {
            isLoading.value -> {
                // Loader pendant la recherche
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            hasSearched.value && results.value.isEmpty() -> {
                // Aucun résultat trouvé
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Aucun résultat pour \"${query.value}\"",
                        fontSize = 16.sp,
                        fontStyle = FontStyle.Italic,
                        color = colorResource(R.color.grey)
                    )
                }
            }

            results.value.isNotEmpty() -> {
                // Liste des résultats — même style que DrinksScreen
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(results.value) { drink ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
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

            else -> {
                // État initial — message d'aide
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Rechercher un cocktail",
                            fontSize = 18.sp,
                            fontStyle = FontStyle.Italic,
                            color = colorResource(R.color.grey)
                        )
                        Text(
                            text = "par nom ou par ingrédient",
                            fontSize = 14.sp,
                            color = colorResource(R.color.grey),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }
}