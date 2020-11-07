package org.geepawhill.dungeon

import javafx.event.EventHandler
import javafx.geometry.Orientation
import javafx.scene.control.ScrollPane
import javafx.scene.paint.Color
import javafx.scene.transform.Scale
import javafx.stage.Stage
import tornadofx.*

class Main : App(MainView::class) {
    override fun start(stage: Stage) {
        super.start(stage)
        stage.isMaximized = true
    }
}

class MainView : View() {

    val scale = Scale(1.0, 1.0)
    lateinit var scroller: ScrollPane

    override val root = borderpane {
        right = toolbar {
            orientation = Orientation.VERTICAL
            button("Zoom Out") {
            }

            button("Zoom In") {
            }
        }
        center = scrollpane {
            scroller = this
            isPannable = true
            vbarPolicy = ScrollPane.ScrollBarPolicy.NEVER
            hbarPolicy = ScrollPane.ScrollBarPolicy.NEVER
            content = group {
                onScroll = EventHandler { event ->
                    println("Scroll")
                    if (event.deltaY > 0) {
                        scale.x *= SCALE_STEP
                        scale.y *= SCALE_STEP
                        scale.pivotX = this@scrollpane.hvalue + this@scrollpane.width / 2.0
                        scale.pivotY = this@scrollpane.vvalue + this@scrollpane.height / 2.0
                        transforms.clear()
                        transforms.add(scale)
                    } else {
                        scale.x /= SCALE_STEP
                        scale.y /= SCALE_STEP
                        scale.pivotX = this@scrollpane.hvalue + this@scrollpane.width / 2.0
                        scale.pivotY = this@scrollpane.vvalue + this@scrollpane.height / 2.0
                        transforms.clear()
                        transforms.add(scale)
                    }
                }
                rectangle(0.0, 0.0, SIZE, SIZE)
                for (x in 1 until TILES_IN_MAP) {
                    line(x * TILESIZE, 0.0, x * TILESIZE, SIZE) {
                        stroke = Color.RED
                    }
                    line(0.0, x * TILESIZE, SIZE, x * TILESIZE) {
                        stroke = Color.BLUE
                    }
                }
            }
        }
    }

    companion object {
        const val SIZE = 8192.0
        const val TILESIZE = 32.0
        const val TILES_IN_MAP = (SIZE / TILESIZE).toInt()
        const val SCALE_STEP = .8
    }
}
	
