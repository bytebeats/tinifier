package me.bytebeats.plugin.tinifier.ui.filetree

import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.CheckedTreeNode
import me.bytebeats.plugin.tinifier.model.TinifyError

data class FileTreeNode(
    var error: TinifyError? = null,
    var imageBuffer: ByteArray? = null,
    val file: VirtualFile? = null
) : CheckedTreeNode(file) {
    fun hasError(): Boolean = error != null

    fun getVirtualFile(): VirtualFile? = getUserObject() as VirtualFile?

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FileTreeNode

        if (error != other.error) return false
        if (imageBuffer != null) {
            if (other.imageBuffer == null) return false
            if (!imageBuffer.contentEquals(other.imageBuffer)) return false
        } else if (other.imageBuffer != null) return false
        if (file != other.file) return false

        return true
    }

    override fun hashCode(): Int {
        var result = error?.hashCode() ?: 0
        result = 31 * result + (imageBuffer?.contentHashCode() ?: 0)
        result = 31 * result + (file?.hashCode() ?: 0)
        return result
    }


}