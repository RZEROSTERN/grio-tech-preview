package com.rzerocorp.petsposts.models

class Message {
    var likes: Int = 0
    var views: Int = 0
    var date: String = ""
    var id: String = ""
    lateinit var body: Body
}