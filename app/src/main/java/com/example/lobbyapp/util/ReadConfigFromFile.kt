package com.example.lobbyapp.util

import android.os.Environment
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.util.Properties

enum class RequiredFields {
    LOGO,
    PRIMARY_COLOR,
    SECONDARY_COLOR,
    BACKGROUND_COLOR,
    TEXT_COLOR,
    QUALITY_CHECK,
    CAMERA_ALWAYS_ON,
    THRESHOLD,
    UA,
    IDP_URL,
    IDP_KEY,
    PREFIX,
    PIN
}

private val FILENAMES = listOf("lobby-config.ini", "config.ini");

fun checkConfigExists(): Boolean {
    return getFile(FILENAMES) != null
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
    val configFile = getFile(FILENAMES)
    val fileInputStream = FileInputStream(configFile)
    val properties = Properties()
    properties.load(InputStreamReader(fileInputStream, Charset.forName("UTF-8")))
    return properties
}

fun getFile(filenames: List<String>): File? {
    val downloadsPath: String =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath

    for (filename in filenames) {
        val file = File("$downloadsPath/$filename")
        if (file.exists()) {
            return file
        }
    }

    return null
}