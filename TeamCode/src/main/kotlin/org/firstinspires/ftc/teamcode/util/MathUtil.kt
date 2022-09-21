@file:JvmName("MU")

package org.firstinspires.ftc.teamcode.util

/**
 * Simple utility function to convert from degrees to radians without having to type out a
 * long function name (and without having to do `<Int>.toDouble()` in Kotlin)
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