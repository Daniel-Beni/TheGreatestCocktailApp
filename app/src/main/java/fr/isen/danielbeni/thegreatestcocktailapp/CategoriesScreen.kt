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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.danielbeni.thegreatestcocktailapp.dataClasses.CategoryListResponse
import fr.isen.danielbeni.thegreatestcocktailapp.dataClasses.DrinkCategory
import fr.isen.danielbeni.thegreatestcocktailapp.network.NetworkManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun CategoriesScreen(modifier: Modifier = Modifier) {
    val categories = remember { mutableStateOf<List<DrinkCategory>>(emptyList()) }
    val isLoading = remember { mutableStateOf(true) }
    val context = LocalContext.current

    // LaunchedEffect(Unit) = exécuté une seule fois quand le composable apparaît
    LaunchedEffect(Unit) {
        NetworkManager.api.getCategories().enqueue(object : Callback<CategoryListResponse> {
            override fun onResponse(
                call: Call<CategoryListResponse>,
                response: Response<CategoryListResponse>
            ) {
                categories.value = response.body()?.drinks ?: emptyList()
                isLoading.value = false
            }

            override fun onFailure(call: Call<CategoryListResponse>, t: Throwable) {
                Log.e("CategoriesScreen", "Erreur réseau: ${t.message}")
                isLoading.value = false
            }
        })
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
            items(categories.value) { category ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val intent = Intent(context, DrinksActivity::class.java)
                            intent.putExtra("CATEGORY_NAME", category.strCategory)
                            context.startActivity(intent)
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = category.strCategory ?: "Inconnu",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Voir les boissons",
                            tint = colorResource(R.color.grey)
                        )
                    }
                }
            }
        }
    }
}