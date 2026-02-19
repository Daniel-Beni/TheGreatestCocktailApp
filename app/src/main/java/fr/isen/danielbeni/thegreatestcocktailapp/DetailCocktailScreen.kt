
package fr.isen.danielbeni.thegreatestcocktailapp
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.layout.size
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.border
import androidx.compose.material3.Text
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.Row
import fr.isen.danielbeni.thegreatestcocktailapp.models.Category
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.material3.Card
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.layout.height

@Composable
fun DetailCocktailScreen(modifier: Modifier = Modifier){
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
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // image Circulaire du cocktail
            Image(
                painter = painterResource(R.drawable.yoghurt_cooler),
                contentDescription = "Yoghurt Cooler",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .border(2.dp, colorResource(R.color.white), CircleShape)
            )

            // Titre du cocktail
            Text(
                text = "Yoghurt Cooler",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.white)
            )
            // Badges de catégories
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                CategoryBadge(Category.OTHER)
                CategoryBadge(Category.NON_ALCOHOLIC)
            }
            // Type de verre
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
                    text = "Highball Glass",
                    color = colorResource(R.color.grey),
                    fontStyle = FontStyle.Italic,
                    fontSize = 16.sp

                )

            }
            // Card Ingrédients
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
                    Text("• 1 tasse de yaourt")
                    Text("• 1 tasse d'eau")
                    Text("• 2 c. à soupe de sucre")
                    Text("• 1/4 c. à café de cumin en poudre")
                    Text("• Une pincée de sel")
                    Text("• Feuilles de menthe fraîche")
                    Text("• Glaçons")
                }
            }


            // Card Préparation
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
                    Text(
                        "Mélangez le yaourt avec l'eau froide et le sucre jusqu'à l'obtention d'un mélange homogène. " +
                                "Ajoutez une pincée de sel et de cumin en poudre, puis mélangez bien. " +
                                "Versez dans un grand verre (type highball) rempli de glaçons. " +
                                "Décorez avec des feuilles de menthe fraîche et saupoudrez légèrement de cumin sur le dessus. " +
                                "Servez immédiatement."
                    )
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
