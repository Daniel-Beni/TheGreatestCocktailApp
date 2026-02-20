package fr.isen.danielbeni.thegreatestcocktailapp

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import fr.isen.danielbeni.thegreatestcocktailapp.dataClasses.DrinkFilterResponse
import fr.isen.danielbeni.thegreatestcocktailapp.dataClasses.DrinkPreview
import fr.isen.danielbeni.thegreatestcocktailapp.network.NetworkManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun DrinksScreen(modifier: Modifier = Modifier, categoryName: String = "Cocktail") {
    val drinks = remember { mutableStateOf<List<DrinkPreview>>(emptyList()) }
    val isLoading = remember { mutableStateOf(true) }
    val context = LocalContext.current

    LaunchedEffect(categoryName) {
        NetworkManager.api.getDrinksByCategory(categoryName).enqueue(
            object : Callback<DrinkFilterResponse> {
                override fun onResponse(
                    call: Call<DrinkFilterResponse>,
                    response: Response<DrinkFilterResponse>
                ) {
                    drinks.value = response.body()?.drinks ?: emptyList()
                    isLoading.value = false
                }

                override fun onFailure(call: Call<DrinkFilterResponse>, t: Throwable) {
                    Log.e("DrinksScreen", "Erreur réseau: ${t.message}")
                    isLoading.value = false
                }
            }
        )
    }

    if (isLoading.value) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            modifier = modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(drinks.value) { drink ->
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
                            // Miniature du cocktail
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