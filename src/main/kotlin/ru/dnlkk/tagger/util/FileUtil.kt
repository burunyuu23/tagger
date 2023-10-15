package ru.dnlkk.tagger.util

import java.io.*

class FileUtil {
    fun getFile(resourcePath: String): File {
        return try {
            File("/app/$resourcePath")
        } catch (e: Exception) {
            throw RuntimeException("File not found")
        }
    }

    fun readFile(file: File): String? {
        try {
            BufferedReader(FileReader(file)).use { br ->
                val sb = StringBuilder()
                var line: String?
                while (br.readLine().also { line = it } != null) {
                    sb.append(line)
                    sb.append("\n")
                }
                if (sb.isNotEmpty()) {
                    sb.setLength(sb.length - 1)
                }
                return sb.toString()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }

    fun overwriteFile(file: File, content: String) {
        try {
            FileOutputStream(file, false).use { fos -> fos.write(content.toByteArray()) }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}