package me.bytebeats.plugin.tinifier.action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.vfs.VirtualFile
import me.bytebeats.plugin.tinifier.util.compress
import me.bytebeats.plugin.tinifier.util.setupApiKey
import java.io.IOException

class BackgroundCompressAction : BaseCompressAction() {
    override fun actionPerformed(e: AnActionEvent) {
        ProgressManager.getInstance().run(object : Task.Backgroundable(e.project, "Tinifier", true) {
            override fun run(indicator: ProgressIndicator) {
                val p = e.project
                val roots = PlatformDataKeys.VIRTUAL_FILE_ARRAY.getData(e.dataContext)
                if (roots.isNullOrEmpty() || !setupApiKey(p)) {
                    return
                }
                val supportedFiles = filterSupportedFiles(roots, false)
                indicator.text = "Compressing image(s)..."
                var index = 0.0
                for (file in supportedFiles) {
                    indicator.text2 = file.name
                    index += 1
                    try {
                        val buffer = compress(file)
                        WriteCommandAction.runWriteCommandAction(p, SaveImage(file, buffer))
                        indicator.fraction = index / supportedFiles.size
                    } catch (ioe: IOException) {
                        println(ioe)
                    }
                }
            }

        })

    }

    class SaveImage(private val file: VirtualFile, private val buffer: ByteArray) : Runnable {
        override fun run() {
            try {
                val stream = file.getOutputStream(this)
                stream.write(buffer)
                stream.close()
            } catch (ignore: IOException) {
                println(ignore)
            }
        }
    }
}