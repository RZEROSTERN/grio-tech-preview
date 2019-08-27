package com.rzerocorp.petsposts

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
import java.math.BigInteger
import java.util.*
import java.util.regex.Pattern
import kotlin.Comparator
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
     private var offset: Int = 10
     var page: Int = 1
     lateinit var arrayPostsData: ArrayList<Post>
     lateinit var postsAdapter: PostsAdapter
     lateinit var recyclerView: RecyclerView

     @RequiresApi(Build.VERSION_CODES.N)
     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_main)

         var loading = true
         var pastVisiblesItems: Int
         var visibleItemCount: Int
         var totalItemCount: Int
         arrayPostsData = this.getData(0, 10)



         var fab = findViewById<FloatingActionButton>(R.id.fab_add_new)
         fab.setOnClickListener(object: View.OnClickListener {
             override fun onClick(p0: View?) {
                 var i = Intent(this@MainActivity, NewPostActivity::class.java)
                 startActivity(i)
             }

         })

         postsAdapter = PostsAdapter(arrayPostsData)
         recyclerView = findViewById<RecyclerView>(R.id.rv_posts)
         val linearLayoutManager = LinearLayoutManager(applicationContext)

         recyclerView.layoutManager = linearLayoutManager!!
         recyclerView.itemAnimator = DefaultItemAnimator()
         recyclerView.adapter = postsAdapter

         recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
             override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                 if (dy > 0)
                 //check for scroll down
                 {
                     visibleItemCount = linearLayoutManager.getChildCount()
                     totalItemCount = linearLayoutManager.getItemCount()
                     pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition()

                     if (loading) {
                         if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                             loading = false

                             var data = getData(1,10)

                             for(i in 0 until data.size) {
                                 arrayPostsData.add(data[i])
                             }

                             postsAdapter.notifyDataSetChanged()
                         }
                     }
                 }
             }
         })
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

    @RequiresApi(Build.VERSION_CODES.N)
    fun getData(start: Int, offset: Int): ArrayList<Post> {
        var data: JSONObject = this.retrieveData()
        var posts: JSONArray = data.getJSONArray("posts")
        var arrayPostsData: ArrayList<Post> = ArrayList()

        var builder: GsonBuilder = GsonBuilder()
        var gson: Gson = builder.create()

        for (i in start until ((start + 1) * offset) - 1) {
            val item = posts.getJSONObject(i)
            var author: Author = gson.fromJson(item["author"].toString(), Author::class.java)
            var message: Message = gson.fromJson(item["message"].toString(), Message::class.java)

            message.body = gson.fromJson(item.getJSONObject("message")["body"].toString(), Body::class.java)

            var post = Post()

            post.author = author
            post.message = message

            arrayPostsData.add(post)
        }

        return arrayPostsData
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        lateinit var arrayPostsDataSorted: List<Post>
        when (item.itemId) {
            R.id.sort_date -> {
                arrayPostsDataSorted = arrayPostsData.sortedBy {
                    it.message.date
                }
            }
            R.id.sort_length -> {
                arrayPostsDataSorted = arrayPostsData.sortedBy {
                    it.message.body.text.length
                }
            }
            R.id.sort_likes -> {
                arrayPostsDataSorted = arrayPostsData.sortedBy {
                    it.message.likes
                }
            }
        }

        var list = ArrayList(arrayPostsDataSorted)
        postsAdapter = PostsAdapter(list)
        recyclerView.adapter = postsAdapter

        return super.onOptionsItemSelected(item)
    }
 }
