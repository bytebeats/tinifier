package me.bytebeats.plugin.tinifier.model

import java.util.regex.Pattern

data class TinifyError(val message: String?, val code: Int, val httpMessage: String?) {

    override fun toString(): String = "%s (HTTP %d/%s)".format(message, code, message)

    companion object {
        private val errorPattern = Pattern.compile("(.+)\\s\\(HTTP (\\d+)/(.+)\\)")

        fun parse(errorMessage: String): TinifyError? {
            val matcher = errorPattern.matcher(errorMessage)
            return if (matcher.matches())
                TinifyError(matcher.group(1), matcher.group(2).toInt(), matcher.group(3))
            else
                null
        }
    }
}
