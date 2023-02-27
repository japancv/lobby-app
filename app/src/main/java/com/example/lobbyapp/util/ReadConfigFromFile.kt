package com.example.lobbyapp.util

import android.os.Environment
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.util.*

enum class RequiredFields {
    LOGO,
    PRIMARY_COLOR,
    SECONDARY_COLOR,
    BACKGROUND_COLOR,
    TEXT_COLOR,
    QUALITY_CHECK,
    THRESHOLD,
    UA,
    IDP_URL,
    IDP_KEY,
    PREFIX,
    PIN
}

fun checkConfigExists(): Boolean {
    return getFile("config.ini").exists()
}

fun checkConfigRequiredFieldsExist(properties: Properties): String {
    val keys = RequiredFields.values()
    var missingKeys = listOf<RequiredFields>()
    keys.toList().forEach {
        if (properties["$it"].toString().isEmpty() || properties["$it"].toString() == "null") {
            missingKeys += it
        }
    }

    return missingKeys.joinToString(separator = ", ")
}

fun readConfigFromFile(): Properties {
    val configFile = getFile("config.ini")
    val fileInputStream = FileInputStream(configFile)
    val properties = Properties()
    properties.load(InputStreamReader(fileInputStream, Charset.forName("UTF-8")))
    return properties
}

fun getFile(filename: String): File {
    val downloadsPath: String =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath

    return File("$downloadsPath/$filename")
}