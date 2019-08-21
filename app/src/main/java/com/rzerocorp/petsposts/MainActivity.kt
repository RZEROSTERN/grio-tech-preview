package com.rzerocorp.petsposts

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.rzerocorp.petsposts.adapters.PostsAdapter
import com.rzerocorp.petsposts.models.Author
import com.rzerocorp.petsposts.models.Body
import com.rzerocorp.petsposts.models.Message
import com.rzerocorp.petsposts.models.Post
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.stream.Collectors

class MainActivity : AppCompatActivity() {
     private var offset: Int = 10
     private var page: Int = 1

     @RequiresApi(Build.VERSION_CODES.N)
     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_main)

         var data: JSONObject = retrieveData()
         var posts: JSONArray = data.getJSONArray("posts")
         var arrayPostsData: ArrayList<Post> = ArrayList()

         var builder: GsonBuilder = GsonBuilder()
         var gson: Gson = builder.create()

         for (i in 0 .. (offset - 1)) {
             val item = posts.getJSONObject(i)
             var author: Author = gson.fromJson(item["author"].toString(), Author::class.java)
             var message: Message = gson.fromJson(item["message"].toString(), Message::class.java)

             message.body = gson.fromJson(item.getJSONObject("message")["body"].toString(), Body::class.java)

             var post = Post()

             post.author = author
             post.message = message

             arrayPostsData.add(post)
         }

         var postsAdapter = PostsAdapter(arrayPostsData)
         var recyclerView = findViewById<RecyclerView>(R.id.rv_posts)
         val linearLayoutManager = LinearLayoutManager(applicationContext)

         recyclerView.layoutManager = linearLayoutManager
         recyclerView.itemAnimator = DefaultItemAnimator()
         recyclerView.adapter = postsAdapter
     }

     @RequiresApi(Build.VERSION_CODES.N)
     fun retrieveData(): JSONObject {
         var resId: Int = resources.getIdentifier("data", "raw", packageName)
         var ins: InputStream = resources.openRawResource(resId)

         val result = BufferedReader(InputStreamReader(ins))
             .lines().collect(Collectors.joining("\n"))

         var res = JSONObject(result)

         return res
     }
 }
