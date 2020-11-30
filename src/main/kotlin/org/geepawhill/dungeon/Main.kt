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

    lateinit var rectangles: ObservableList<Node>

    val zoomable = group {
        rectangle(0.0, 0.0, map.widthInPixels, map.heightInPixels)
        group {
            rectangles = this.children
        }
    }

    val zoomer = PanAndZoomPane(zoomable)

    override val root = borderpane {
        right = toolbar {
            orientation = Orientation.VERTICAL
            button("Initial Place") {
                action {
                    maker.place(45, 45, 55, 55)
                    update()
                }
            }

            button("Random Fills") {
                action {
                    maker.randomFills(25)
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
        maker.populateRegions(rectangles)
    }

    companion object {
        const val SCALE_STEP = .8
    }
}
	
