package com.example.lobbyapp.util

import android.graphics.*
import android.media.Image
import android.os.Environment
import android.util.Base64
import java.io.ByteArrayOutputStream

fun imageToBase64(image: Image): String {
    val yBuffer = image.planes[0].buffer // Y
    val vuBuffer = image.planes[2].buffer // VU

    val ySize = yBuffer.remaining()
    val vuSize = vuBuffer.remaining()

    val nv21 = ByteArray(ySize + vuSize)

    yBuffer.get(nv21, 0, ySize)
    vuBuffer.get(nv21, ySize, vuSize)

    val yuvImage = YuvImage(nv21, ImageFormat.NV21, image.width, image.height, null)
    val out = ByteArrayOutputStream()
    yuvImage.compressToJpeg(Rect(0, 0, yuvImage.width, yuvImage.height), 50, out)
    val imageBytes = out.toByteArray()
    val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    val matrix = Matrix()
    matrix.postRotate(90F)
    val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    val byteArrayOutputStream = ByteArrayOutputStream()
    rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
    val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
    var base64Str = Base64.encodeToString(byteArray, Base64.DEFAULT)
    base64Str = base64Str.replace(System.getProperty("line.separator"), "")
    return base64Str
}

fun checkLegalLogoPath(logoPath: String): Boolean {
    val downloadsDir =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    val logoFile = downloadsDir.resolve(logoPath).canonicalFile

    return logoFile.startsWith(downloadsDir)
}