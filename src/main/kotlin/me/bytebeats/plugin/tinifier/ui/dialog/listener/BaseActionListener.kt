package me.bytebeats.plugin.tinifier.ui.dialog.listener

import me.bytebeats.plugin.tinifier.ui.dialog.CompressImageDialog
import me.bytebeats.plugin.tinifier.ui.filetree.FileTreeNode
import java.awt.event.ActionListener

abstract class BaseActionListener(private val dialog: CompressImageDialog) : ActionListener {
    protected fun getCheckedNodes(root: FileTreeNode): List<FileTreeNode> {
        val nodes = mutableListOf<FileTreeNode>()
        val children = root.children()
        while (children.hasMoreElements()) {
            val child = children.nextElement() as FileTreeNode
            if (!child.isLeaf) {
                nodes.addAll(getCheckedNodes(child))
                continue
            }
            if (child.isChecked) {
                nodes.add(child)
            }
        }
        return nodes
    }
}