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
}