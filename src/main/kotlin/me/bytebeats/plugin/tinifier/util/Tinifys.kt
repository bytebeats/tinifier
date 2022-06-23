package me.bytebeats.plugin.tinifier.util

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.InputValidator
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import com.tinify.Tinify
import me.bytebeats.plugin.tinifier.Preferences
import java.io.IOException

fun setupApiKey(project: Project): Boolean {
    if (Tinify.key().isNullOrEmpty()) {
        val preferences = Preferences.getInstance()
        if (preferences.apiKey.isNullOrEmpty()) {
            preferences.apiKey =
                Messages.showInputDialog(project, API_KEY_REFERENCE, TINIFIER, Messages.getQuestionIcon(), "",
                    object : InputValidator {
                        override fun checkInput(inputString: String?): Boolean = !inputString.isNullOrEmpty()

                        override fun canClose(inputString: String?): Boolean = true
                    })
            if (preferences.apiKey.isNullOrEmpty()) {
                return false
            }
        }
        Tinify.setKey(preferences.apiKey)
    }
    return true
}

@Throws(IOException::class)
fun compress(vf: VirtualFile): ByteArray = Tinify.fromFile(vf.path).toBuffer()