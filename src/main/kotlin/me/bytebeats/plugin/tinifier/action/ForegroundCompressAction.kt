package me.bytebeats.plugin.tinifier.action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.wm.WindowManager
import me.bytebeats.plugin.tinifier.ui.dialog.CompressImageDialog
import me.bytebeats.plugin.tinifier.util.setupApiKey

class ForegroundCompressAction : BaseCompressAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val p = e.project
        val roots = PlatformDataKeys.VIRTUAL_FILE_ARRAY.getData(e.dataContext)
        val frame = WindowManager.getInstance().getFrame(p)
        if (roots.isNullOrEmpty() || frame == null) {
            return
        }
        setupApiKey(p)
        val supportedFiles = filterSupportedFiles(roots, false)
        val dialog = CompressImageDialog(p, supportedFiles, roots.toList())
        dialog.setDialogSize(frame)
        dialog.isVisible = true
    }
}