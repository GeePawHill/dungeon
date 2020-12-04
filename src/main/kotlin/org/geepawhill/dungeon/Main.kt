package org.geepawhill.dungeon

import javafx.collections.ObservableList
import javafx.geometry.Orientation
import javafx.scene.Node
import javafx.stage.Stage
import tornadofx.*

class Main : App(MainView::class) {
    override fun start(stage: Stage) {
        super.start(stage)
        stage.isMaximized = true
    }
}

class MainView : View() {
    val map = Map(200, 200)
    val maker = MapMaker(map)
    val mapToScreen = MapToScreen(map)

    lateinit var rectangles: ObservableList<Node>

    val zoomable = group {
        rectangle(0.0, 0.0, map.widthInPixels, map.heightInPixels) {
        }
        group {
            rectangles = this.children
        }
    }

    val zoomer = PanAndZoomPane(zoomable)

    override val root = borderpane {
        right = toolbar {
            orientation = Orientation.VERTICAL
            label {
                mapToScreen.cell.addListener { _, _, value ->
                    text = value.toString()
                }
            }
            button("Generate") {
                action {
                    maker.generate()
                    update()
                }
            }
        }
        center = zoomer
    }


    init {
        update()

    }
    
    fun update() {
        mapToScreen.mapToScreen(rectangles)
    }

    companion object {
        const val SCALE_STEP = .8
    }
}
	
