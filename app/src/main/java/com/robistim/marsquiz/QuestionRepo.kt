package com.robistim.marsquiz

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.Collections

data class Question(val text: String, val choices: List<String>, val correctIndex: Int)

object QuestionRepo {
    private var cached: List<Question>? = null

    fun loadAll(context: Context): List<Question> {
        cached?.let { return it }
        val input = context.assets.open("questions.json")
        val reader = BufferedReader(InputStreamReader(input))
        val json = reader.readText()
        reader.close()
        val type = object : TypeToken<List<Question>>() {}.type
        val list: List<Question> = Gson().fromJson(json, type)
        cached = list
        return list
    }

    fun loadRandomFive(context: Context): List<Question> {
        val all = loadAll(context).toMutableList()
        all.shuffle()
        return all.take(5)
    }
}
