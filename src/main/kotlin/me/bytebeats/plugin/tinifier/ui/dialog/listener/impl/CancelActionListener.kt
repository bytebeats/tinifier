package me.bytebeats.plugin.tinifier.ui.dialog.listener.impl

import me.bytebeats.plugin.tinifier.ui.dialog.CompressImageDialog
import me.bytebeats.plugin.tinifier.ui.dialog.listener.BaseActionListener
import java.awt.event.ActionEvent

class CancelActionListener(private val dialog: CompressImageDialog) : BaseActionListener(dialog) {
    override fun actionPerformed(e: ActionEvent?) {
        if (!dialog.compressInProgress) {
            dialog.dispose()
        }
        dialog.compressInProgress = false
        dialog.buttonCancel.text = "Cancel"
    }
}