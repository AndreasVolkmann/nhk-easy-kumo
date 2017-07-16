package storage

import com.mongodb.MongoClient
import com.mongodb.client.MongoCollection
import org.bson.Document

interface Mongo {

    companion object {
        const val host = "localhost"
        const val port = 27017
    }

    fun connect() = MongoClient(host, port)

    val database: String

    operator fun <T> invoke(collection: String, body: MongoCollection<Document>.() -> T) = connect().use {
        val col = it.getDatabase(database).getCollection(collection)
        body(col)
    }


}