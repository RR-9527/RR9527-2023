package org.tunableautogen

import java.io.Serializable

fun interface MarkerCallback : Serializable {
    fun onMarkerReached()
}
