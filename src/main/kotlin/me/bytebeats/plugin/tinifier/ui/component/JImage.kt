package me.bytebeats.plugin.tinifier.ui.component

import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.JBColor
import com.intellij.util.ui.UIUtil
import java.awt.Color
import java.awt.Graphics
import java.awt.Image
import java.awt.Rectangle
import java.awt.event.ComponentEvent
import java.awt.event.ComponentListener
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.IOException
import javax.imageio.ImageIO
import javax.swing.BorderFactory
import javax.swing.JPanel

class JImage : JPanel() {
    private var image: BufferedImage? = null
    private var size: Int = 0
    private lateinit var rect: Rectangle
    private var backImage: BufferedImage? = null

    init {
        border = BorderFactory.createLineBorder(JBColor.border())
        addComponentListener(object : ComponentListener {
            override fun componentResized(e: ComponentEvent?) {
                if (image != null) {
                    rect = getImageRect()
                    backImage = prepareChessboardBackground()
                }
            }

            override fun componentMoved(e: ComponentEvent?) {
            }

            override fun componentShown(e: ComponentEvent?) {
            }

            override fun componentHidden(e: ComponentEvent?) {
            }

        })
    }

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)
        if (image != null) {
            g?.drawImage(backImage, rect.x, rect.y, rect.width, rect.height, this)
            g?.drawImage(image, rect.x, rect.y, rect.width, rect.height, this)
        }
    }

    @Throws(IOException::class)
    fun setImage(file: VirtualFile?) {
        setImage(file?.contentsToByteArray())
    }

    @Throws(IOException::class)
    fun setImage(buffer: ByteArray?) {
        if (buffer == null) {
            image = null
            size = 0
        } else {
            image = ImageIO.read(ByteArrayInputStream(buffer))
            size = buffer.size
        }
        if (image != null) {
            rect = getImageRect()
            backImage = prepareChessboardBackground()
        }
        repaint()
    }

    fun getImage(): Image? = image

    fun getImageSize(): Int = size

    private fun getImageRect(): Rectangle {

        val rect = Rectangle()
        if (image != null) {
            if (width > image!!.width && height > image!!.height) {
                rect.width = image!!.width
                rect.height = image!!.height
                rect.x = (width - rect.width) / 2
                rect.y = (height - rect.height) / 2
                return rect
            }

            val widthTransform = width.toFloat() / image!!.width.toFloat()
            val heightTransform = height.toFloat() / image!!.height.toFloat()

            if (width < image!!.width && height < image!!.height) {
                val transform = minOf(widthTransform, heightTransform)
                rect.width = (image!!.width * transform).toInt()
                rect.height = (image!!.height * transform).toInt()
                rect.x = (width - rect.width) / 2
                rect.y = (height - rect.height) / 2
                return rect
            }

            if (width > image!!.width) {
                rect.width = (image!!.width * heightTransform).toInt()
                rect.height = height
                rect.x = (width - rect.width) / 2
                rect.y = 0
            } else {
                rect.width = width
                rect.height = (image!!.height * widthTransform).toInt()
                rect.x = 0
                rect.y = (height - rect.height) / 2
            }
        }
        return rect
    }

    private fun prepareChessboardBackground(): BufferedImage {
        val image = UIUtil.createImage(rect!!.width, rect!!.height, BufferedImage.TYPE_INT_RGB)
        val g = image.graphics
        var even = true
        g.color = Color.WHITE
        g.fillRect(0, 0, rect!!.width, rect!!.height)
        g.color = Color.LIGHT_GRAY
        for (x in 0 until rect!!.width step 5) {
            even = (x / 5) and 1 == 0
            for (y in 0 until rect!!.height step 5) {
                even = !even
                if (even) {
                    g.fillRect(x, y, 5, 5)
                }
            }
        }
        return image
    }
}