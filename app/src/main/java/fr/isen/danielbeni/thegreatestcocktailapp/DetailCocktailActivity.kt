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
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import fr.isen.danielbeni.thegreatestcocktailapp.dataClasses.CocktailResponse
import fr.isen.danielbeni.thegreatestcocktailapp.dataClasses.Drink
import fr.isen.danielbeni.thegreatestcocktailapp.network.NetworkManager
import fr.isen.danielbeni.thegreatestcocktailapp.ui.theme.TheGreatestCocktailAppTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailCocktailActivity : ComponentActivity() {

    // État observable pour le cocktail chargé depuis l'API
    private val cocktail = mutableStateOf<Drink?>(null)

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val drinkName = intent.getStringExtra("DRINK_NAME") ?: "Cocktail"

        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            TheGreatestCocktailAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            // Affiche le vrai nom du cocktail une fois chargé
                            title = { Text(cocktail.value?.strDrink ?: drinkName) },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = colorResource(R.color.purple_700),
                                titleContentColor = colorResource(R.color.white),
                                actionIconContentColor = colorResource(R.color.white)
                            ),
                            actions = {
                                IconButton(onClick = {
                                    Toast.makeText(
                                        context,
                                        "${cocktail.value?.strDrink ?: drinkName} ajouté aux favoris !",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.FavoriteBorder,
                                        contentDescription = "Ajouter aux favoris"
                                    )
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    // Passe le cocktail chargé à l'écran de détail
                    DetailCocktailScreen(
                        modifier = Modifier.padding(innerPadding),
                        drink = cocktail.value
                    )
                }
            }
        }
    }

    // Appelé chaque fois que l'Activity devient visible → charge le détail
    override fun onStart() {
        super.onStart()
        val drinkId = intent.getStringExtra("DRINK_ID") ?: return
        fetchDrinkDetail(drinkId)
    }

    private fun fetchDrinkDetail(drinkId: String) {
        NetworkManager.api.getDrinkDetail(drinkId).enqueue(
            object : Callback<CocktailResponse> {
                override fun onResponse(
                    call: Call<CocktailResponse>,
                    response: Response<CocktailResponse>
                ) {
                    cocktail.value = response.body()?.drinks?.firstOrNull()
                    Log.d("DetailCocktailActivity", "Cocktail chargé: ${cocktail.value?.strDrink}")
                }

                override fun onFailure(call: Call<CocktailResponse>, t: Throwable) {
                    Log.e("DetailCocktailActivity", "Erreur: ${t.message}")
                }
            }
        )
    }
}