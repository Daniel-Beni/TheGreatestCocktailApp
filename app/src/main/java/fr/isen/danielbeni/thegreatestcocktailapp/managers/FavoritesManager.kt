package fr.isen.danielbeni.thegreatestcocktailapp.managers
import fr.isen.danielbeni.thegreatestcocktailapp.dataClasses.Drink
import android.content.Context
import com.google.gson.Gson


/**
 * FavoritesManager — Gère le stockage des cocktails favoris dans SharedPreferences.
 *
 * SharedPreferences = fichier XML local sur le téléphone, persiste entre les sessions.
 * On y stocke une chaîne JSON qui représente la liste des Drink favoris.
 *
 * Pourquoi une classe et pas un object ?
 * → On utilise une classe standard. On l'instancie là où on en a besoin.
 *   C'est léger (pas d'état interne), chaque instance lit/écrit le même fichier partagé.
 */

class FavoritesManager() {

    companion object {
        // nom du fichier SharedPreferences (créé dans /data/data/package/shared_prefs/)
        private const val PREFS_NAME = "favorites"

        // clé sous laquelle on stocke le Json
        private const val PREFS_KEY = "favorites"

    }

    /**
     * Récupère la liste des cocktails favoris.
     *
     * Flux :
     * 1. Ouvre le fichier SharedPreferences "favorites"
     * 2. Lit la chaîne JSON stockée sous la clé "favorites"
     * 3. Si null (aucun favori sauvegardé) → retourne liste vide
     * 4. Sinon, Gson désérialise le JSON en Array<Drink>, converti en List
     *
     * @param context nécessaire pour accéder à SharedPreferences (Android l'exige)
     * @return Liste des Drink favoris (peut être vide, jamais null)
     */
    fun getFavorites(context: Context): List<Drink> {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = sharedPreferences.getString(PREFS_KEY, null)
            ?: return emptyList()

        // fromJson avec Array<Drink>::class.java → parse le JSON en tableau Java de Drink
        // .toList() → convertit en List Kotlin immuable
        return Gson().fromJson(json, Array<Drink>::class.java).toList()
    }


    /**
     * Ajoute ou supprime un cocktail des favoris (toggle).
     *
     * Logique :
     * - Si le drink est déjà en favori (même idDrink) → on le SUPPRIME
     * - Si le drink n'est pas en favori → on l'AJOUTE
     * - Puis on réécrit toute la liste en JSON dans SharedPreferences
     *
     * @param drink Le cocktail à ajouter/supprimer
     * @param context Nécessaire pour accéder à SharedPreferences
     */


    fun toggleFavorite(drink: Drink, context: Context) {

        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = sharedPreferences.getString(PREFS_KEY, null)

        // si aucun favori n'existe encore, on la liste avec ce seul drink
        if (json ==null){
            sharedPreferences.edit()
                .putString(PREFS_KEY, Gson().toJson(listOf(drink)))
                .apply() // apply() = écriture asynchrone (non-bloquante)
                return

        }
        // Désérialise la liste existante en MutableList pour pouvoir modifier
        val list = Gson().fromJson(json,
        Array<Drink>::class.java).toMutableList()

        // vérifie si ce cocktail est déjà en favori( comparaison idDrink)
        val alreadyFavorite = list.any {it.idDrink == drink.idDrink}

    if (alreadyFavorite){
        // supprime toutes les occurrences de ce drink (par sécurité)
        list.removeAll { it.idDrink == drink.idDrink }
    } else {
        // ajoute le nouveau drink à la liste
        list.add(drink)

    }
        // réécrit toute la liste en JSON
        sharedPreferences.edit()
            .putString(PREFS_KEY, Gson().toJson(list))
            .apply()

    }

    /**
     * Vérifie si un cocktail est dans les favoris.
     *
     * @param drink Le cocktail à vérifier
     * @param context Nécessaire pour accéder à SharedPreferences
     * @return true si le drink est en favori, false sinon
     */


    fun isFavorite(drink: Drink, context: Context): Boolean {

        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = sharedPreferences.getString(PREFS_KEY, null)
            ?: return false

        val list = Gson().fromJson(json, Array<Drink>::class.java).toList()
        //any{} retourne true dès qu'un element satisfait la condition
        return list.any { it.idDrink == drink.idDrink

        }

  }
}



