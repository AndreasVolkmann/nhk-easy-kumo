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
    val collection: String

    operator fun <T> invoke(body: MongoCollection<Document>.() -> T) = connect().use {
        val col = it.getDatabase(database).getCollection(collection)
        body(col)
    }


}