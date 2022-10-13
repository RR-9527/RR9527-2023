package org.firstinspires.ftc.teamcodekt.components.easytoggle

/**
 * __It's a switch. What more do you want to know?__
 *
 * Kotlin usage examples:
 * ```
 * fun main() {
 *   var toggle = EasyToggle()
 *   var troglodyte = EasyToggle(true) //or EasyToggle(initialState = true)
 *
 *   troglodyte.state = false
 *   print(troglodyte.nowFalse()) // true
 * }
 * ```
 *
 * Java usage examples:
 * ```java
 * public static void main(String... args) {
 *   EasyToggle toggle = new EasyToggle(false);
 *   EasyToggle troglodyte = new EasyToggle(true);
 *
 *   troglodyte.setState(false);
 *   System.out.print(troglodyte.nowFalse()); // true
 * }
 * ```
 * _Note: Both language examples produce the exact same outputs_
 *
 * @param initialState The initial state of the switch. Defaults to `false`
 *
 * @author KG
 */
class EasyToggle(initialState: Boolean = false) {
    var state = initialState
        set(value) {
            previousState = field
            field = value
        }

    private var previousState = initialState

    /**
     * Returns `true` on the rising edge.
     */
    fun nowTrue() = state && !previousState

    /**
     * Returns `true` on the falling edge.
     */
    fun nowFalse() = !state && previousState
}