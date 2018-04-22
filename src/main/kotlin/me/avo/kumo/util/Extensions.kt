package me.avo.kumo.util

import org.apache.logging.log4j.LogManager
import org.jsoup.nodes.Element
import kotlin.reflect.KClass

fun <T: Any> KClass<T>.getLogger() = LogManager.getLogger(java)!!

fun <T> Collection<T>.print(i: Int = size) = also { take(i).onEach { println(it) } }
fun <R, T> Map<R, T>.print(i: Int = size) = also { entries.take(i).onEach { println(it) } }

fun <T: Any> KClass<T>.loadResource(name: String) = java.classLoader.getResource(name).readText()

fun Element.getFirstByTag(tag: String) = getElementsByTag(tag).first()!!
fun Element.getFirstByClass(clazz: String) = getElementsByClass(clazz).first()!!