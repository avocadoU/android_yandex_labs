package com.example.android_yandex_labs

import android.graphics.Color
import java.util.*

enum class Importance {
    LOW,        
    NORMAL,
    HIGH
}

data class TodoItem(
    val uid: String = UUID.randomUUID().toString(),
    val text: String,
    val importance: Importance,
    val color: Int = Color.WHITE,
    val deadline: Date? = null,
    val isDone: Boolean = false
)