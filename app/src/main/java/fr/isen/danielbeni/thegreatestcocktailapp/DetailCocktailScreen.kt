package fr.isen.danielbeni.thegreatestcocktailapp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import fr.isen.danielbeni.thegreatestcocktailapp.dataClasses.Drink
import fr.isen.danielbeni.thegreatestcocktailapp.models.Category

@Composable
fun DetailCocktailScreen(modifier: Modifier = Modifier, drink: Drink? = null) {
    Box(
        Modifier
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        colorResource(R.color.purple_500),
                        colorResource(R.color.purple_700)
                    )
                )
            )
            .fillMaxSize()
    ) {
        if (drink == null) {
            // Affiche un loader pendant le chargement
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = colorResource(R.color.white))
            }
        } else {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Image circulaire du cocktail (depuis le web avec Coil)
                AsyncImage(
                    model = drink.strDrinkThumb,
                    contentDescription = drink.strDrink,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                        .border(2.dp, colorResource(R.color.white), CircleShape)
                )

                // Titre du cocktail
                Text(
                    text = drink.strDrink ?: "Cocktail inconnu",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.white)
                )

                // Badges de catégories
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    drink.strCategory?.let { categoryName ->
                        CategoryBadge(Category.fromApiName(categoryName))
                    }
                    drink.strAlcoholic?.let { alcoholicType ->
                        if (alcoholicType == "Alcoholic") {
                            CategoryBadge(Category.ALCOHOLIC)
                        } else {
                            CategoryBadge(Category.NON_ALCOHOLIC)
                        }
                    }
                }

                // Type de verre
                drink.strGlass?.let { glass ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = "Glass type",
                            tint = colorResource(R.color.grey),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = glass,
                            color = colorResource(R.color.grey),
                            fontStyle = FontStyle.Italic,
                            fontSize = 16.sp
                        )
                    }
                }

                // Card Ingrédients
                val ingredients = drink.ingredientList()
                if (ingredients.isNotEmpty()) {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = stringResource(R.string.ingredients_title),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            ingredients.forEach { (ingredient, measure) ->
                                Text("• $measure $ingredient".trim())
                            }
                        }
                    }
                }

                // Card Préparation
                drink.strInstructions?.let { instructions ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = stringResource(R.string.preparation_title),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            // Préfère les instructions en français si disponibles
                            Text(drink.strInstructionsFR ?: instructions)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryBadge(category: Category) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(
                Brush.horizontalGradient(
                    Category.gradientColors(category)
                )
            )
    ) {
        Text(
            text = Category.label(category),
            fontSize = 16.sp,
            color = colorResource(R.color.white),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}