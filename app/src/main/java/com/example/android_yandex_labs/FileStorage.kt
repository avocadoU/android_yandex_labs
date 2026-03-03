package com.example.android_yandex_labs

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.io.*

class FileStorage(private val context: Context) {

    private val _items = mutableListOf<TodoItem>()
    val items: List<TodoItem>
        get() = _items.toList()

    fun addItem(item: TodoItem) {
        if (_items.any { it.uid == item.uid }) {
            updateItem(item)
        } else {
            _items.add(item)
        }
    }

    fun updateItem(item: TodoItem) {
        val index = _items.indexOfFirst { it.uid == item.uid }
        if (index != -1) {
            _items[index] = item
        }
    }

    fun removeItem(uid: String): Boolean {
        return _items.removeAll { it.uid == uid }
    }

    fun saveToFile(filename: String): Boolean {
        return try {
            val jsonArray = JSONArray()
            _items.forEach {
                jsonArray.put(it.toJson())
            }

            context.openFileOutput(filename, Context.MODE_PRIVATE).use { outputStream ->
                outputStream.write(jsonArray.toString().toByteArray())
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun loadFromFile(filename: String): Boolean {
        return try {
            val fileContent = context.openFileInput(filename).bufferedReader().use {
                it.readText()
            }

            val jsonArray = JSONArray(fileContent)
            _items.clear()

            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                parseTodoItemFromJson(jsonObject)?.let {
                    _items.add(it)
                }
            }
            true
        } catch (e: FileNotFoundException) {
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Очистка хранилища
    fun clear() {
        _items.clear()
    }
}