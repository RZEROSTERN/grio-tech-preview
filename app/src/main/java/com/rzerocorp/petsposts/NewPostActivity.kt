package com.rzerocorp.petsposts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class NewPostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_post)

        supportActionBar!!.title = "Add new post"
    }
}
