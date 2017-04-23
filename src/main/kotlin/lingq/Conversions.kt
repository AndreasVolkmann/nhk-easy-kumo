package lingq

import com.github.salomonbrys.kotson.get
import com.github.salomonbrys.kotson.string
import com.google.gson.JsonParser

/**
 * Created by Av on 4/22/2017.
 */

/**
 * Takes the JSON response and returns a list of titles from the lessons
 */
fun resToTitle(json: String): List<String> = JsonParser().parse(json).asJsonArray.map {
    it["title"].string
}