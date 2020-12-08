package org.geepawhill.dungeon

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleLongProperty
import javafx.scene.Parent
import tornadofx.*

class RulesView(r: MapRules, onGenerate: (MapRules) -> Unit) : View() {

    val seed = SimpleLongProperty(r.seed)
    val density = SimpleIntegerProperty(r.density)

    val rules: MapRules
        get() {
            return MapRules(seed.value, density.value)
        }

    override val root: Parent = form {
        fieldset {
            field("Seed") {
                textfield(seed)
            }
            field("Density %") {
                textfield(density)
            }
            button("Generate") {
                action { onGenerate(rules) }
            }
        }
    }
}