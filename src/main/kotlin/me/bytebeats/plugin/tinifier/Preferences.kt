package me.bytebeats.plugin.tinifier

import com.intellij.configurationStore.APP_CONFIG
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil
import me.bytebeats.plugin.tinifier.util.TINIFIER
import java.awt.Dimension
import java.awt.Point

@State(name = TINIFIER, storages = [Storage("$APP_CONFIG$/tinifier.xml")])
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

    companion object {
        fun getInstance(): Preferences = ApplicationManager.getApplication().getService(Preferences::class.java)
    }
}