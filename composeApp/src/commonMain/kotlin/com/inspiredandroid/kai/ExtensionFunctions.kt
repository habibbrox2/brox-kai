package com.inspiredandroid.kai

import java.text.NumberFormat
import java.util.Locale
import kotlin.time.Instant

/**
 * Convert a Unix epoch-seconds timestamp to an ISO-8601 date string (YYYY-MM-DD),
 * or null for zero/negative values. Some providers return `0` instead of omitting
 * the `created` field, which would otherwise surface as "Jan 1970".
 */
fun Long.toIsoDate(): String? = if (this <= 0L) null else Instant.fromEpochSeconds(this).toString().take(10)

fun formatContextWindow(tokens: Long): String = when {
    tokens >= 1_000_000 -> "${tokens / 1_000_000}M"
    tokens >= 1_000 -> "${tokens / 1_000}K"
    else -> "$tokens"
}

private val shortMonthNames = listOf(
    "Jan", "Feb", "Mar", "Apr", "May", "Jun",
    "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",
)

/**
 * Formats a release date string (ISO format like "2025-03" or "2025-09-29")
 * into a localized "MMM yyyy" format using the provided locale.
 * Falls back to the input string if parsing fails.
 */
fun formatReleaseDate(iso: String, locale: Locale = Locale.getDefault()): String {
    val firstDash = iso.indexOf('-')
    if (firstDash < 1) return iso
    val secondDash = iso.indexOf('-', firstDash + 1)
    val year = iso.substring(0, firstDash).toIntOrNull() ?: return iso
    val monthStr = if (secondDash > 0) iso.substring(firstDash + 1, secondDash) else iso.substring(firstDash + 1)
    val month = monthStr.toIntOrNull() ?: return iso
    if (month !in 1..12) return iso
    // Use locale-aware month name
    val calendar = java.util.Calendar.getInstance(locale)
    calendar.set(java.util.Calendar.YEAR, year)
    calendar.set(java.util.Calendar.MONTH, month - 1)
    calendar.set(java.util.Calendar.DAY_OF_MONTH, 1)
    return java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM, locale).format(calendar.time)
}

/**
 * Formats a file size in bytes to a human-readable string using the provided locale.
 * Uses localized number formatting for decimal separators.
 */
fun formatFileSize(bytes: Long, locale: Locale = Locale.getDefault()): String {
    val nf = NumberFormat.getInstance(locale)
    nf.maximumFractionDigits = 1
    nf.minimumFractionDigits = 0
    return when {
        bytes >= 1_000_000_000 -> "${nf.format(bytes / 100_000_000.0 / 10)} GB"
        bytes >= 1_000_000 -> "${nf.format(bytes / 1_000_000)} MB"
        bytes >= 1_000 -> "${nf.format(bytes / 1_000)} KB"
        else -> "${nf.format(bytes.toDouble())} B"
    }
}

fun String.smartTruncate(maxLength: Int): String {
    if (length <= maxLength) return this
    val keep = (maxLength - 80) / 2
    return take(keep) +
        "\n[... ${length - 2 * keep} characters truncated ...]\n" +
        takeLast(keep)
}