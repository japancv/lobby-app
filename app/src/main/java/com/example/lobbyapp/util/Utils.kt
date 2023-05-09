package com.example.lobbyapp.util

import android.content.Context
import android.content.res.Resources
import com.example.lobbyapp.model.ServerErrorResponse
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.ResponseBody
import java.text.SimpleDateFormat
import java.time.ZoneOffset
import java.util.*

fun formatDate(date: String): String {
    val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    inputDateFormat.timeZone = TimeZone.getTimeZone(ZoneOffset.UTC)
    val outputDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    outputDateFormat.timeZone = TimeZone.getDefault()

    return outputDateFormat.format(inputDateFormat.parse(date))
}

fun parseServerErrorResponse(errorBody: ResponseBody): ServerErrorResponse? {
    return try {
        Json.decodeFromString<ServerErrorResponse>(errorBody.string())
    } catch (e: Exception) {
        null
    }
}

fun getStringResource(context: Context, string: String): String? {
    return try {
        val stringResourceId =
            context.resources.getIdentifier(string, "string", context.packageName)
        context.getString(stringResourceId)
    } catch (e: Resources.NotFoundException) {
        null
    }
}