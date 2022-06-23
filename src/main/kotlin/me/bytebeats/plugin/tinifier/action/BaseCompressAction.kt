package me.bytebeats.plugin.tinifier.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import com.tinify.Tinify
import me.bytebeats.plugin.tinifier.Preferences
import me.bytebeats.plugin.tinifier.util.API_KEY_REFERENCE
import me.bytebeats.plugin.tinifier.util.TINIFIER

abstract class BaseCompressAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        if (Tinify.key().isNullOrEmpty()) {
            val preferences = Preferences.getInstance()
            if (preferences.apiKey.isNullOrEmpty()) {
                preferences.apiKey =
                    Messages.showInputDialog(e.project, API_KEY_REFERENCE, TINIFIER, Messages.getQuestionIcon())
            }
            if (preferences.apiKey.isNullOrEmpty()) {
                return
            }
            Tinify.setKey(preferences.apiKey)
        }
    }

    override fun update(e: AnActionEvent) {
        val preferences = Preferences.getInstance()
        if (!preferences.checkSupportedFiles) {
            return
        }
        val supportedFiles = filterSupportedFiles(PlatformDataKeys.VIRTUAL_FILE_ARRAY.getData(e.dataContext), true)
        e.presentation.isEnabled = supportedFiles.isNotEmpty()
    }

    override fun isDumbAware(): Boolean = true

    fun filterSupportedFiles(files: Array<VirtualFile>?, breakOn1stRound: Boolean): List<VirtualFile> {
        val ans = mutableListOf<VirtualFile>()
        if (!files.isNullOrEmpty()) {
            for (file in files) {
                if (file.isDirectory) {
                    ans.addAll(filterSupportedFiles(file.children, breakOn1stRound))
                    if (breakOn1stRound && ans.isNotEmpty()) {
                        break
                    } else {
                        continue
                    }
                }
                val extension = file.extension
                if (supportedImageExtensions.contains(extension?.lowercase())) {
                    ans.add(file)
                    if (breakOn1stRound) {
                        break
                    }
                }
            }
        }
        return ans
    }

    companion object {
        val supportedImageExtensions = arrayOf("png", "jpg", "jpeg")
    }
}