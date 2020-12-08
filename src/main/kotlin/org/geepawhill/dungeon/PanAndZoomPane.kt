package org.geepawhill.dungeon

import javafx.event.EventHandler
import javafx.geometry.Point2D
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.control.ScrollPane
import javafx.scene.input.ScrollEvent
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.VBox
import javafx.scene.paint.Color

class PanAndZoomPane(private val target: Node) : ScrollPane() {
    private var scaleValue = 0.08
    private val zoomIntensity = 0.02
    private val zoomNode: Node
    private fun outerNode(node: Node): Node {
        val outerNode = centeredNode(node)
        outerNode.onScroll = EventHandler { e: ScrollEvent ->
            e.consume()
            onScroll(e.textDeltaY, Point2D(e.x, e.y))
        }
        return outerNode
    }

    private fun centeredNode(node: Node): Node {
        val vBox = VBox(node)
        vBox.alignment = Pos.CENTER
        return vBox
    }

    private fun updateScale() {
        target.scaleX = scaleValue
        target.scaleY = scaleValue
    }

    private fun onScroll(wheelDelta: Double, mousePoint: Point2D) {
        val zoomFactor = Math.exp(wheelDelta * zoomIntensity)
        val innerBounds = zoomNode.layoutBounds
        val viewportBounds = viewportBounds

        // calculate pixel offsets from [0, 1] range
        val valX = hvalue * (innerBounds.width - viewportBounds.width)
        val valY = vvalue * (innerBounds.height - viewportBounds.height)
        scaleValue = scaleValue * zoomFactor
        updateScale()
        layout() // refresh ScrollPane scroll positions & target bounds

        // convert target coordinates to zoomTarget coordinates
        val posInZoomTarget = target.parentToLocal(zoomNode.parentToLocal(mousePoint))

        // calculate adjustment of scroll position (pixels)
        val adjustment = target.localToParentTransform.deltaTransform(posInZoomTarget.multiply(zoomFactor - 1))

        // convert back to [0, 1] range
        // (too large/small values are automatically corrected by ScrollPane)
        val updatedInnerBounds = zoomNode.boundsInLocal
        hvalue = (valX + adjustment.x) / (updatedInnerBounds.width - viewportBounds.width)
        vvalue = (valY + adjustment.y) / (updatedInnerBounds.height - viewportBounds.height)
    }

    init {
        background = Background(BackgroundFill(Color.BLACK, null, null))
        zoomNode = Group(target)
        content = outerNode(zoomNode)
        isPannable = true
        hbarPolicy = ScrollBarPolicy.NEVER
        vbarPolicy = ScrollBarPolicy.NEVER
        isFitToHeight = true //center
        isFitToWidth = true //center
        updateScale()
    }
}