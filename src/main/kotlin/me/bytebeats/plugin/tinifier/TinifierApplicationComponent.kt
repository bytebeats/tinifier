package me.bytebeats.plugin.tinifier

import com.intellij.ide.plugins.PluginManager
import com.intellij.notification.*
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.ApplicationComponent
import com.intellij.openapi.extensions.PluginId
import java.util.*

class TinifierApplicationComponent : ApplicationComponent {

    override fun getComponentName(): String = "TinifierApplicationComponent"

    override fun initComponent() {
        val plugin = PluginManager.getPlugin(PluginId.getId(PLUGIN_ID)) ?: return
        val preferences = Preferences.getInstance()
        if (preferences.uuid.isNullOrEmpty()) {
            preferences.uuid = UUID.randomUUID().toString()
        }
        if (preferences.username.isNullOrEmpty()) {
            preferences.username = preferences.uuid
        }
        if (plugin.version != preferences.version) {
            preferences.version = plugin.version
            val popupTitle = "${plugin.name} v${plugin.version}"
            val notification = Notification(
                plugin.name,
                popupTitle,
                plugin.changeNotes,
                NotificationType.INFORMATION
            )
            Notifications.Bus.notify(notification)
        }
    }

    companion object {
        internal const val PLUGIN_ID = "me.bytebeats.plugin.tinifier"
        fun getInstance(): TinifierApplicationComponent = ApplicationManager.getApplication().getComponent(
            TinifierApplicationComponent::class.java
        )
    }
}