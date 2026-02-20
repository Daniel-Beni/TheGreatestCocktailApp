package fr.isen.danielbeni.thegreatestcocktailapp.dataClasses

import com.google.gson.annotations.SerializedName

data class DrinkCategory(
    @SerializedName("strCategory")
    val strCategory: String?
)