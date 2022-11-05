@file:JvmName("MU")

package org.firstinspires.ftc.teamcodekt.util

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
 *
 * @param int The degree value to convert
 *
 * @return 'int' in radians
 *
 * @author KG
 */
fun rad(int: Int) = Math.toRadians(int.toDouble())

/**
 * Overloaded method of the above to accept doubles
 *
 * @param double The degree value to convert
 *
 * @return 'double' in radians
 *
 * @author KG
 */
fun rad(double: Double) = Math.toRadians(double)

fun Double.toIn() = this / 2.54

fun Double.toRad() = Math.toRadians(this)

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
