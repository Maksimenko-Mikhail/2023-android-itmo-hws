package ru.ok.itmo.hw.data_objects


data class MessageText(val text : String)
data class MessageImage(val link : String)
data class MessageData(val Text : MessageText?, val Image : MessageImage?)
data class Message(val id : Int, val userName : String, val channel : String?, val data : MessageData, val time : Long?)

