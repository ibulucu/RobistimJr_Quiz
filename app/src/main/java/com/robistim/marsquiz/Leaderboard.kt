package com.robistim.marsquiz

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object Leaderboard {
    private const val PREF = "leaderboard"
    private const val KEY = "scores"
    private const val LIMIT = 10

    data class Entry(val name: String, val score: Int, val timestamp: Long)

    fun load(context: Context): List<Entry> {
        val sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        val json = sp.getString(KEY, "[]") ?: "[]"
        val type = object : TypeToken<List<Entry>>() {}.type
        return Gson().fromJson(json, type) ?: emptyList()
    }

    fun save(context: Context, entry: Entry) {
        val list = load(context).toMutableList()
        list.add(entry)
        val sorted = list.sortedWith(compareByDescending<Entry> { it.score }.thenBy { it.timestamp }).take(LIMIT)
        val sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        sp.edit().putString(KEY, Gson().toJson(sorted)).apply()
    }

    fun clear(context: Context) {
        val sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        sp.edit().remove(KEY).apply()
    }
}
