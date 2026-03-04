package com.example.android_yandex_labs

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import org.slf4j.LoggerFactory
import java.io.*

class FileStorage(private val context: Context) {

    private val logger = LoggerFactory.getLogger(FileStorage::class.java)
    private val _items = mutableListOf<TodoItem>()
    val items: List<TodoItem>
        get() = _items.toList()

    fun addItem(item: TodoItem) {
        if (_items.any { it.uid == item.uid }) {
            logger.debug("Запись уже существует")
            updateItem(item)
        } else {
            _items.add(item)
            logger.info("Добавление новой записи")
        }
    }

    fun updateItem(item: TodoItem) {
        val index = _items.indexOfFirst { it.uid == item.uid }
        if (index != -1) {
            _items[index] = item
            logger.info("Обновление записи")
        } else {
            logger.warn("Несуществующая запись!")
        }
    }

    fun removeItem(uid: String): Boolean {
        val removed = _items.removeAll { it.uid == uid }
        if (removed) {
            logger.info("Удаление записи")
        } else {
            logger.warn("Несуществующая запись!")
        }
        return removed
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
            logger.info("Успешное сохранение")
            true
        } catch (e: Exception) {
            logger.error("Ошибка сохранения", e)
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
            logger.info("Успешно загружена запись из файла")
            true
        } catch (e: FileNotFoundException) {
            logger.debug("Файл не найден")
            true
        } catch (e: Exception) {
            logger.error("Ошибка загрузки записи из файлв", e)
            false
        }
    }

    // Очистка хранилища
    fun clear() {
        logger.info("Cleared storage, deleted ${_items.size} items")
        _items.clear()
    }
}