package com.example.android_yandex_labs

import org.json.JSONObject
import android.graphics.Color
import java.text.SimpleDateFormat
import java.util.*

private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).apply {
    timeZone = TimeZone.getTimeZone("UTC")
}

fun TodoItem.toJson(): JSONObject {
    val json = JSONObject()

    json.put("uid", uid)
    json.put("text", text)
    json.put("is_done", isDone)

    if (importance != Importance.NORMAL) {
        json.put("importance", importance.name)
    }

    if (color != Color.WHITE) {
        json.put("color", color)
    }

    deadline?.let {
        json.put("deadline", dateFormat.format(it))
    }

    return json
}

fun parseTodoItemFromJson(json: JSONObject): TodoItem? {
    return try {
        val uid = json.optString("uid") ?: UUID.randomUUID().toString()
        val text = json.getString("text")

        val importance = when (json.optString("importance")) {
            "LOW" -> Importance.LOW
            "HIGH" -> Importance.HIGH
            else -> Importance.NORMAL
        }

        val color = json.optInt("color", Color.WHITE)

        val deadline = if (json.has("deadline")) {
            dateFormat.parse(json.getString("deadline"))
        } else null

        val isDone = json.optBoolean("is_done", false)

        TodoItem(
            uid = uid,
            text = text,
            importance = importance,
            color = color,
            deadline = deadline,
            isDone = isDone
        )
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}