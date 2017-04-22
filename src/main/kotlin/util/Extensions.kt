package util

import org.apache.logging.log4j.LogManager
import kotlin.reflect.KClass

/**
 * Created by Av on 4/22/2017.
 */


fun <T: Any> KClass<T>.getLogger() = LogManager.getLogger(java)!!

fun <T> Collection<T>.print(i: Int = size) = also { take(i).onEach { println(it) } }
fun <R, T> Map<R, T>.print(i: Int = size) = also { entries.take(i).onEach { println(it) } }

fun <T: Any> KClass<T>.loadResource(name: String) = java.classLoader.getResource(name).readText()