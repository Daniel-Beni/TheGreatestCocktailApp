package fr.isen.danielbeni.thegreatestcocktailapp.dataClasses

import com.google.gson.annotations.SerializedName

// réponse de random.php et lookup.php
data class CocktailResponse(
    @SerializedName("drinks")
    val drinks: List<Drink>?
)

// réponse de list.php?c=list
data class CategoryListResponse(
    @SerializedName("drinks")
    val drinks: List<DrinkCategory>?
)

// réponse de filter.php?c=XXX
data class DrinkFilterResponse(
    @SerializedName("drinks")
    val drinks: List<DrinkPreview>?
)