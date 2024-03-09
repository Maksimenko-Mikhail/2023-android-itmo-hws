package ru.ok.itmo.hw.data_objects



data class MessageData(val text : String?, val imagePath : String?)
data class Message(val id : Int, val userName : String, val channel : String?, val data : MessageData, val time : Long?)

