package me.bytebeats.plugin.tinifier

import com.intellij.ide.plugins.PluginManager
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.extensions.PluginId
import com.intellij.util.xmlb.XmlSerializerUtil
import me.bytebeats.plugin.tinifier.util.NotificationUtil
import java.awt.Dimension
import java.awt.Point
import java.util.*

@State(name = "me.bytebeats.plugin.tinifier", storages = [Storage("tinifier.xml")])
class Preferences : PersistentStateComponent<Preferences> {
    var version: String? = null
    var uuid: String? = null
    var username: String? = null
    var apiKey: String? = null
    var dialogOffsetX = 0
    var dialogOffsetY = 0
    var dialogWidth = 0
    var dialogHeight = 0
    var dividerOffset = 200
    var checkSupportedFiles = true

    var dialogSize: Dimension
        get() = Dimension(dialogWidth, dialogHeight)
        set(value) {
            this.dialogWidth = value.width
            this.dialogHeight = value.height
        }
    var dialogLocation: Point
        get() = Point(dialogOffsetX, dialogOffsetY)
        set(value) {
            dialogOffsetX = value.x
            dialogOffsetY = value.y
        }

    override fun getState(): Preferences = this

    override fun loadState(state: Preferences) {
        XmlSerializerUtil.copyBean(state, this)
    }

    override fun initializeComponent() {
        super.initializeComponent()
        val plugin = PluginManager.getPlugin(PluginId.getId(PLUGIN_ID)) ?: return
        if (uuid.isNullOrEmpty()) {
            uuid = UUID.randomUUID().toString()
        }
        if (username.isNullOrEmpty()) {
            username = uuid
        }
        if (plugin.version != version) {
            version = plugin.version
            val popupTitle = "${plugin.name} v${plugin.version}"
            NotificationUtil.infoBalloon(popupTitle, plugin.changeNotes)
        }
    }

    companion object {
        internal const val PLUGIN_ID = "me.bytebeats.plugin.tinifier"
        fun getInstance(): Preferences = ApplicationManager.getApplication().getService(Preferences::class.java)
    }
}