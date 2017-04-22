package lingq

import com.github.salomonbrys.kotson.get
import com.google.gson.JsonParser

/**
 * Created by Av on 4/22/2017.
 */


fun resToTitle(json: String) = JsonParser().parse(json).asJsonArray.map {
    it["title"]
}