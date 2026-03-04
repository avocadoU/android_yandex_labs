package com.example.android_yandex_labs

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.slf4j.LoggerFactory
import java.util.Date

class MainActivity : AppCompatActivity() {

    private val logger = LoggerFactory.getLogger(MainActivity::class.java)
    private lateinit var storage: FileStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logger.info("MainActivity created")

        storage = FileStorage(this)

        logger.debug("Загрузка записи из файла")
        storage.loadFromFile("todo_items.json")

        val newItem = TodoItem(
            text = "Посмотреть сериал",
            importance = Importance.HIGH,
            deadline = Date(System.currentTimeMillis() + 86400000) // +1 день
        )

        logger.debug("Добавление новой записи в журнал")
        storage.addItem(newItem)

        logger.debug("Сохранение записи")
        storage.saveToFile("todo_items.json")

        val allItems = storage.items
        logger.info("Все записи: ${allItems.size}")
    }
}