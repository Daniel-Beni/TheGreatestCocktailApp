package fr.isen.danielbeni.thegreatestcocktailapp.models

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import fr.isen.danielbeni.thegreatestcocktailapp.R
enum class Category {
    BEER,
    COCKTAIL,
    COCOA,
    COFFEE,
    LIQUOR,
    ORDINARY_DRINK,
    PUNCH,
    SHAKE,
    SHOT,
    SOFT_DRINK,
    ALCOHOLIC,
    NON_ALCOHOLIC,
    OTHER;

    companion object {
        fun allCategories(): List<Category> {
            return listOf(
                BEER,
                COCKTAIL,
                COCOA,
                COFFEE,
                LIQUOR,
                ORDINARY_DRINK,
                PUNCH,
                SHAKE,
                SHOT,
                SOFT_DRINK,
                OTHER
            )

        }

        fun label(category: Category): String {
            return when (category) {
                ALCOHOLIC -> "Alcoholic"
                NON_ALCOHOLIC -> "Non alcoholic"
                OTHER -> "Other / Unknown"
                BEER -> "Beer"
                COCKTAIL -> "Cocktail"
                COCOA -> "Cocoa"
                COFFEE -> "Coffee / Tea"
                LIQUOR -> "Homemade Liquor"
                ORDINARY_DRINK -> "Ordinary Drink"
                PUNCH -> "Punch / Party Drink"
                SHAKE -> "Shake"
                SHOT -> "Shot"
                SOFT_DRINK -> "Soft Drink"
            }
        }
        @Composable
        fun gradientColors(category: Category): List<Color> {
            return when (category) {
                ALCOHOLIC, NON_ALCOHOLIC -> listOf(
                    colorResource(R.color.amber_400),
                    colorResource(R.color.deep_orange_400)
                )
                BEER, COCKTAIL, SHAKE, SHOT -> listOf(
                    colorResource(R.color.purple_200),
                    colorResource(R.color.purple_500)
                )
                else -> listOf(
                    colorResource(R.color.teal_200),
                    colorResource(R.color.teal_700)
                )
            }
        }
    }
}