package me.avo.kumo.util

import java.util.*

/**
 * Created by Av on 20-01-2016.
 * Loads the config.properties file
 */
object Property {

    val props = Properties().apply {
        Property::class.java.classLoader.getResourceAsStream("config.properties").use(this::load)
    }

    operator fun get(key: String) = props[key].toString()

}

