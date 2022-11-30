@file:JvmName("MU")

package org.firstinspires.ftc.teamcodekt.util

/**
 * Simple utility function to convert from cm to inces without having to type out a
 * long function name (and without having to do `<Int>.toDouble()` in Kotlin)
 *
 * Kotlin usage examples:
 * ```
 * val inches = inches(30)
 * ```
 *
 * Java usage examples:
 * ```java
 * double inches = MU.inches(30);
 * ```
 */
fun inches(cm: Number) = cm.toDouble() / 2.54

/**
 * Kotlin syntac sugar for the above
 */
fun Number.toIn() = this.toDouble() / 2.54

/**
 * Simple utility function to convert from degrees to radians without having to type out a
 * long function name (and without having to do `<Int>.toDouble()` in Kotlin)
 *
 * Kotlin usage examples:
 * ```
 * val theta = rad(180)
 * ```
 *
 * Java usage examples:
 * ```java
 * double theta = MU.rad(180);
 * ```
 */
fun rad(x: Number) = Math.toRadians(x.toDouble())

/**
 * Kotlin syntac sugar for the above
 */
fun Number.toRad() = Math.toRadians(this.toDouble())

/**
 * Simple utility function to return a default value if the given double is NaN
 *
 * Kotlin usage examples:
 * ```
 * val doubleOk = 0.0
 * val doubleNaN = 0.0/0.0
 *
 * doubleOk.ifNan(1.0) // returns 0.0
 * doubleNaN.ifNan(1.0) // returns 1.0
 * ```
 *
 * Java usage examples:
 * ```java
 * double doubleOk = 0.0;
 * double doubleNaN = 0.0/0.0;
 *
 * MU.ifNan(doubleOk, 1.0); // returns 0.0
 * MU.ifNan(doubleNaN, 1.0); // returns 1.0
 * ```
 *
 * @param default The default value to return if 'double' is NaN
 *
 * @return 'double' if it is not NaN, otherwise 'default'
 *
 * @author KG
 */
fun Double.ifNaN(default: Double) = if (isNaN()) default else this

/**
 * Overloaded method of the above to accept floats
 *
 * @param default The degree value to convert
 *
 * @return 'float' if it is not NaN, otherwise 'default'
 *
 * @author KG
 */
fun Float.ifNaN(default: Double) = if (isNaN()) default else this

/**
 * Just returns `true` if the given number is in between the given min and maxes
 *
 * Kotlin usage examples:
 * ```kotlin
 * val willBeTrue = inRange(5, 0, 10)
 * ```
 *
 * Java usage examples:
 * ```java
 * boolean willBeTrue = MU.inRange(5, 0, 10);
 * ```
 */
fun inRange(x: Number, min: Number, max: Number) =
    x.toDouble() in min.toDouble()..max.toDouble()

/**
 * Finds the average of the given numbers
 *
 * Kotlin usage examples:
 * ```kotlin
 * val average = average(1, 2, 3, 4, 5)
 * ```
 *
 * Java usage examples:
 * ```java
 * double average = MU.average(1, 2, 3, 4, 5);
 * ```
 */
fun avg(vararg xs: Number) = xs.sumOf { it.toDouble() } / xs.size
