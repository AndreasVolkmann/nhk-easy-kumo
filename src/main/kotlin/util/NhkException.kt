package util

/**
 * Created by Av on 5/17/2017.
 */
class NhkException(id: String, message: String)
    : Exception("There was a problem with Article $id: $message")