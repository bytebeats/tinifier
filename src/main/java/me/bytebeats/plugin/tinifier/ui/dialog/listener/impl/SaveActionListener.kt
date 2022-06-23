package me.bytebeats.plugin.tinifier.ui.dialog.listener.impl

import com.intellij.openapi.application.ApplicationManager
import me.bytebeats.plugin.tinifier.ui.dialog.CompressImageDialog
import me.bytebeats.plugin.tinifier.ui.dialog.listener.BaseActionListener
import me.bytebeats.plugin.tinifier.ui.filetree.FileTreeNode
import java.awt.event.ActionEvent
import java.io.IOException
import javax.swing.tree.DefaultTreeModel

class SaveActionListener(private val dialog: CompressImageDialog) : BaseActionListener(dialog) {
    override fun actionPerformed(e: ActionEvent?) {
        dialog.buttonSave.isEnabled = false
        dialog.buttonCancel.isEnabled = false

        val nodes = getCheckedNodes(dialog.tree.model.root as FileTreeNode)
        ApplicationManager.getApplication().runWriteAction {
            for (node in nodes) {
                try {
                    val stream = node.getVirtualFile()?.getOutputStream(this) ?: return@runWriteAction
                    node.imageBuffer?.let { stream.write(it) }
                    stream.close()
                } catch (ioe: IOException) {
                    println(ioe)
                }
            }

            ApplicationManager.getApplication().invokeLater {
                for (node in nodes) {
                    node.imageBuffer = null
                    (dialog.tree.model as DefaultTreeModel).nodeChanged(node)
                }

                dialog.buttonCancel.text = "Cancel"
                dialog.buttonCancel.isEnabled = true
                dialog.buttonProcess.isEnabled = true
                dialog.rootPane.defaultButton = dialog.buttonProcess
            }
        }
    }
}