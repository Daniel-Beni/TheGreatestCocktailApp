package fr.isen.danielbeni.thegreatestcocktailapp.network

import fr.isen.danielbeni.thegreatestcocktailapp.dataClasses.CategoryListResponse
import fr.isen.danielbeni.thegreatestcocktailapp.dataClasses.CocktailResponse
import fr.isen.danielbeni.thegreatestcocktailapp.dataClasses.DrinkFilterResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    // GET https://www.thecocktaildb.com/api/json/v1/1/random.php
    @GET("random.php")
    fun getRandomCocktail(): Call<CocktailResponse>

    // GET https://www.thecocktaildb.com/api/json/v1/1/list.php?c=list
    @GET("list.php?c=list")
    fun getCategories(): Call<CategoryListResponse>

    // GET https://www.thecocktaildb.com/api/json/v1/1/filter.php?c=Beer
    @GET("filter.php")
    fun getDrinksByCategory(
        @Query("c") category: String
    ): Call<DrinkFilterResponse>

    // GET https://www.thecocktaildb.com/api/json/v1/1/lookup.php?i=11007
    @GET("lookup.php")
    fun getDrinkDetail(
        @Query("i") drinkId: String
    ): Call<CocktailResponse>

    // ── Section 6 — Recherche ────────────────────

    // GET https://www.thecocktaildb.com/api/json/v1/1/search.php?s=margarita
    // Recherche par nom → retourne des Drink COMPLETS
    @GET("search.php")
    fun searchByName(
        @Query("s") name: String
    ): Call<CocktailResponse>

    // GET https://www.thecocktaildb.com/api/json/v1/1/filter.php?i=Vodka
    // Recherche par ingrédient → retourne des DrinkPreview (id, nom, thumb seulement)
    @GET("filter.php")
    fun searchByIngredient(
        @Query("i") ingredient: String
    ): Call<DrinkFilterResponse>
}