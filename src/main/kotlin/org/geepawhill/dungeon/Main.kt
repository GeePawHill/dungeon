package org.geepawhill.dungeon

import javafx.scene.Group
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
    val mapToScreen: MapToScreen

    lateinit var cellLayer: Group
    lateinit var markupLayer: Group

    val zoomable = group {
        rectangle(0.0, 0.0, map.widthInPixels, map.heightInPixels) {
        }
        cellLayer = group {}
        markupLayer = group {}
    }

    val zoomer = PanAndZoomPane(zoomable)
    val rulesView = RulesView(MapRules(preferNearest = true)) { rules -> generate(rules) }

    override val root = borderpane {
        right = rulesView.root
        center = zoomer
    }


    init {
        mapToScreen = MapToScreen(map, cellLayer)
        mapToScreen.update()
    }

    fun generate(rules: MapRules) {
        maker.generate(rules)
        mapToScreen.update()
    }

    companion object {
        const val SCALE_STEP = .8
    }
}
	
