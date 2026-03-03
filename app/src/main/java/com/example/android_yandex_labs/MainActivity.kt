package com.example.android_yandex_labs

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.util.Date

class MainActivity : AppCompatActivity() {

    private lateinit var storage: FileStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        storage = FileStorage(this)

        storage.loadFromFile("todo_items.json")

        val newItem = TodoItem(
            text = "Посмотреть сериал",
            importance = Importance.HIGH,
            deadline = Date(System.currentTimeMillis() + 86400000) // +1 день
        )

        storage.addItem(newItem)

        storage.saveToFile("todo_items.json")

        val allItems = storage.items
    }
}