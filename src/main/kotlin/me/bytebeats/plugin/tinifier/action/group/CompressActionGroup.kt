package me.bytebeats.plugin.tinifier.action.group

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DefaultActionGroup

class CompressActionGroup : DefaultActionGroup() {
    private var firstRun = true
    override fun update(e: AnActionEvent) {
        if (!firstRun) {
            return
        }
        firstRun = false
        if (isPopup) {
            getChildren(e).forEach { action -> action.templatePresentation.icon = null }
        }
    }
}