package com.example.sweetjoygeladinhos.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

fun saveImageToInternalStorage(context: Context, imageUri: Uri): String {
    val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
    val fileName = "produto_${System.currentTimeMillis()}.jpg"
    val file = File(context.filesDir, fileName)
    val outputStream = FileOutputStream(file)

    inputStream?.use { input ->
        outputStream.use { output ->
            input.copyTo(output)
        }
    }

    return file.absolutePath
}
