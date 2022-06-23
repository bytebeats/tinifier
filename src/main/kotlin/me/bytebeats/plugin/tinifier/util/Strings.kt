package me.bytebeats.plugin.tinifier.util

import kotlin.math.ln
import kotlin.math.pow

fun humanReadableByteCount(bytes: Long): String {
    return humanReadableByteCount(bytes, false)
}

fun humanReadableByteCount(bytes: Long, si: Boolean): String {
    val unit = if (si) 1000 else 1024
    if (bytes < unit * 10) return String.format("%,d B", bytes)
    val exp = (ln(bytes.toDouble()) / ln(unit.toDouble())).toInt()
    val pre = (if (si) "kMGTPE" else "KMGTPE")[exp - 1].toString() + if (si) "" else "i"
    return "%.1f %sB".format(bytes / unit.toDouble().pow(exp.toDouble()), pre)
}