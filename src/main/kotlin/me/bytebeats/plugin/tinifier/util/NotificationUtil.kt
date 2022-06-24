package me.bytebeats.plugin.tinifier.util

import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.ex.ProjectManagerEx


object NotificationUtil {
    private val LOG_NOTIFICATION_GROUP = NotificationGroup.logOnlyGroup("Tinifier Log")
    private val BALLOON_NOTIFICATION_GROUP = NotificationGroup.balloonGroup("Tinifier Balloon")
    private val TOOL_WINDOW_NOTIFICATION_GROUP =
        NotificationGroup.toolWindowGroup("Tinifier Tool Window", "Tinifier Tool Window")

    /**
     * messages on Event Log Window
     *
     * @param message
     */
    fun info(message: String) {
        info("Tinifier", message)
    }

    /**
     * messages on Event Log Window
     *
     * @param message
     */
    fun info(title: String, message: String) {
        LOG_NOTIFICATION_GROUP.createNotification(title, message, NotificationType.INFORMATION, null)
            .notify(ProjectManagerEx.getInstance().defaultProject)
    }

    /**
     * messages on Event Log Window in balloon style
     *
     * @param message
     */
    fun infoBalloon(message: String) {
        infoBalloon("Tinifier", message)
    }

    /**
     * messages on Event Log Window in balloon style
     *
     * @param message
     */
    fun infoBalloon(title: String, message: String) {
        BALLOON_NOTIFICATION_GROUP.createNotification(title, message, NotificationType.WARNING, null)
            .notify(ProjectManagerEx.getInstance().defaultProject)
    }

    /**
     * messages on Tool Window
     *
     * @param message
     */
    fun infoToolWindow(message: String) {
        infoToolWindow("Tinifier", message)
    }

    /**
     * messages on Tool Window
     *
     * @param message
     */
    fun infoToolWindow(title: String, message: String) {
        TOOL_WINDOW_NOTIFICATION_GROUP.createNotification(title, message, NotificationType.ERROR, null)
            .notify(ProjectManagerEx.getInstance().defaultProject)
    }
}