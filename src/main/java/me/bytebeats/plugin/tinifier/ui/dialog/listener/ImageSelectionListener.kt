package me.bytebeats.plugin.tinifier.ui.dialog.listener

import com.intellij.openapi.vfs.VirtualFile
import me.bytebeats.plugin.tinifier.ui.component.JImage
import me.bytebeats.plugin.tinifier.ui.dialog.CompressImageDialog
import me.bytebeats.plugin.tinifier.ui.filetree.FileTreeNode
import me.bytebeats.plugin.tinifier.util.humanReadableByteCount
import java.io.IOException
import javax.swing.JLabel
import javax.swing.event.TreeSelectionEvent
import javax.swing.event.TreeSelectionListener

class ImageSelectionListener(private val dialog: CompressImageDialog) : TreeSelectionListener {
    override fun valueChanged(e: TreeSelectionEvent?) {
        val node = dialog.tree.lastSelectedPathComponent as FileTreeNode
        try {
            updateImage(dialog.imageBefore, dialog.detailsBefore, node.getVirtualFile()!!)
            updateImage(dialog.imageAfter, dialog.detailsAfter, node.imageBuffer!!)
        } catch (ignore: IOException) {
            println(ignore)
        }
    }

    @Throws(IOException::class)
    private fun updateImage(image: JImage, detailsLabel: JLabel, file: VirtualFile) {
        if (file.isDirectory) {
            image.setImage(null as VirtualFile?)
            dialog.clearTitle()
        } else {
            image.setImage(file)
            image.getImage()?.let {
                val width = it.getWidth(dialog)
                val height = it.getHeight(dialog)
                dialog.title = "- %s [%dx%d]".format(file.name, width, height)
            }
        }
        updateImageDetails(image, detailsLabel, "Old")
    }

    @Throws(IOException::class)
    private fun updateImage(image: JImage, detailsLabel: JLabel, buffer: ByteArray) {
        image.setImage(buffer)
        updateImageDetails(image, detailsLabel, "New")
    }


    private fun updateImageDetails(image: JImage, detailsLabel: JLabel, prefix: String) {
        if (image.getImage() == null) {
            detailsLabel.text = "$prefix Size: ---"
        } else {
            detailsLabel.text = "$prefix Size: %s".format(humanReadableByteCount(image.getImageSize().toLong()))
        }
    }
}