package me.avo.kumo.nhk

class NhkException(id: String, message: String)
    : Exception("There was a problem with Article $id: $message")