package com.inspiredandroid.kai.updater

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URI

/**
 * JVM (desktop + android) implementation of [httpGet] using HttpURLConnection.
 */
internal actual suspend fun httpGet(url: String, headers: Map<String, String>): String = withContext(Dispatchers.IO) {
    val connection = URI.create(url).toURL().openConnection() as java.net.HttpURLConnection
    try {
        connection.connectTimeout = 10_000
        connection.readTimeout = 10_000
        connection.requestMethod = "GET"
        headers.forEach { (key, value) -> connection.setRequestProperty(key, value) }
        val statusCode = connection.responseCode
        val stream = if (statusCode in 200..299) connection.inputStream else connection.errorStream
        stream?.bufferedReader()?.use { it.readText() }
            ?: throw IllegalStateException("HTTP $statusCode for $url")
    } finally {
        connection.disconnect()
    }
}

/**
 * JVM implementation of [currentPlatformKey] from system properties.
 */
internal actual fun currentPlatformKey(): String {
    val os = System.getProperty("os.name", "").lowercase()
    val arch = System.getProperty("os.arch", "").lowercase()
    return when {
        os.contains("windows") -> "windows"

        os.contains("mac") || os.contains("darwin") ->
            if (arch.contains("aarch64") || arch.contains("arm64")) "macos-arm64" else "macos-x64"

        os.contains("linux") ->
            if (arch.contains("aarch64") || arch.contains("arm64")) "linux-arm64" else "linux-x64"

        else -> "none"
    }
}
