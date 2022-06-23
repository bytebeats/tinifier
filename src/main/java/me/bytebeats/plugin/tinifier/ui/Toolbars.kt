package me.bytebeats.plugin.tinifier.ui

import com.intellij.ide.actions.AboutAction
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.DefaultActionGroup
import java.awt.Dimension
import javax.swing.JPanel


fun createToolbar(): JPanel {
    val group = DefaultActionGroup()
    group.add(AboutAction())
    val actionToolbar = ActionManager.getInstance().createActionToolbar("top", group, true)
    val toolbar = actionToolbar.component as JPanel
    toolbar.minimumSize = Dimension(0, actionToolbar.maxButtonHeight)
    return toolbar
}