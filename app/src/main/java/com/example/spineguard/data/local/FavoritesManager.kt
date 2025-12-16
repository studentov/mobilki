package com.example.spineguard.data.local

import android.content.Context

object FavoritesManager {

    private const val PREFS_NAME = "exercise_favorites"
    private const val KEY_SET = "favorites_set"

    private fun getPrefs(context: Context) =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun isFavorite(context: Context, exerciseId: String): Boolean {
        val set = getPrefs(context).getStringSet(KEY_SET, emptySet()) ?: emptySet()
        return set.contains(exerciseId)
    }

    fun toggleFavorite(context: Context, exerciseId: String): Boolean {
        val prefs = getPrefs(context)
        val current = prefs.getStringSet(KEY_SET, emptySet())?.toMutableSet() ?: mutableSetOf()

        val nowFavorite: Boolean
        if (current.contains(exerciseId)) {
            current.remove(exerciseId)
            nowFavorite = false
        } else {
            current.add(exerciseId)
            nowFavorite = true
        }

        prefs.edit()
            .putStringSet(KEY_SET, current)
            .apply()

        return nowFavorite
    }
}
