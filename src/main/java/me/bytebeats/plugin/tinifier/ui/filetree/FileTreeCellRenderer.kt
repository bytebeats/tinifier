package me.bytebeats.plugin.tinifier.ui.filetree

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Iconable
import com.intellij.ui.CheckboxTree
import com.intellij.ui.SimpleTextAttributes
import com.intellij.util.IconUtil
import javax.swing.JTree

class FileTreeCellRenderer(private val project: Project) : CheckboxTree.CheckboxTreeCellRenderer() {
    override fun customizeRenderer(
        tree: JTree?,
        value: Any?,
        selected: Boolean,
        expanded: Boolean,
        leaf: Boolean,
        row: Int,
        hasFocus: Boolean
    ) {
        val node = value as FileTreeNode
        val file = node.getVirtualFile() ?: return
        val renderer = textRenderer
        renderer.icon = IconUtil.getIcon(file, Iconable.ICON_FLAG_VISIBILITY, project)
        renderer.append(file.name)
        if (node.hasError()) {
            renderer.append("    ${node.error?.message}", SimpleTextAttributes.ERROR_ATTRIBUTES)
            return
        }
        node.imageBuffer?.let { buffer ->
            val compressed = 100L - buffer.size * 100L / file.length
            renderer.append("  %d%%".format(compressed), SimpleTextAttributes.DARK_TEXT)
        }
    }
}