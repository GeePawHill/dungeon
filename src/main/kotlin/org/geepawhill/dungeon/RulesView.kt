package org.geepawhill.dungeon

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleLongProperty
import javafx.scene.Parent
import tornadofx.*

class RulesView(r: MapRules, onGenerate: (MapRules) -> Unit) : View() {

    val seed = SimpleLongProperty(r.seed)
    val density = SimpleIntegerProperty(r.density)
    val groupAttempts = SimpleIntegerProperty(r.groupAttempts)
    val preferNearest = SimpleBooleanProperty(r.preferNearest)
    val copies = SimpleIntegerProperty(r.copies)

    val rules: MapRules
        get() {
            return MapRules(seed.value, density.value, groupAttempts.value, preferNearest.value, copies.value)
        }

    override val root: Parent = form {
        fieldset {
            field("Seed") {
                textfield(seed)
                button("+") {
                    action {
                        seed.value += 1
                        onGenerate(rules)
                    }
                }
            }
            field("Density %") {
                textfield(density)
            }
            field("Group Attempts") {
                textfield(groupAttempts)
                button("-") {
                    action {
                        groupAttempts.value -= 1
                        onGenerate(rules)
                    }
                }
                button("+") {
                    action {
                        groupAttempts.value += 1
                        onGenerate(rules)
                    }
                }
            }
            checkbox("Prefer Nearest", preferNearest) {
                action {
                    onGenerate(rules)
                }
            }
            field("Copies") {
                textfield(copies)
            }
            button("Generate") {
                action { onGenerate(rules) }
            }
        }
    }
}