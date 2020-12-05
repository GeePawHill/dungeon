package org.geepawhill.dungeon

import javafx.beans.property.SimpleLongProperty
import javafx.scene.Parent
import tornadofx.*

class RulesView(val maker: MapMaker) : View() {

    val seed = SimpleLongProperty(0)

    override val root: Parent = form {
        fieldset {
            field("Seed") {
                textfield(seed)
            }
            button("Generate") {
                action { generate() }
            }
        }
    }

    fun generate() {
        maker.generate(MapRules(seed.value))
    }

}