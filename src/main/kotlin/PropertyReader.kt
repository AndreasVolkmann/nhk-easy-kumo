import java.util.*

/**
 * Created by Av on 20-01-2016.
 * Loads the config.properties file
 */
object PropertyReader {

    private var properties: java.util.Properties = java.util.Properties()
    private var loaded: Boolean = false

    fun getProperty(key: String): String = loadProperties().getProperty(key)

    private fun loadProperties(): Properties {
        if (!loaded) load()
        return properties
    }


    private fun load() = PropertyReader::class.java.classLoader.getResourceAsStream("config.properties").use {
        properties.load(it)
        loaded = true
    }
}

