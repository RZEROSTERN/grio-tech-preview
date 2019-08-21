package com.rzerocorp.petsposts.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.rzerocorp.petsposts.R
import com.rzerocorp.petsposts.models.Post

class PostsAdapter(posts: ArrayList<Post>): RecyclerView.Adapter<PostsAdapter.ViewHolder>() {
    lateinit var posts: ArrayList<Post>

    init {
        this.posts = posts
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var holder: View = LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        return ViewHolder(holder)
    }

    override fun getItemCount(): Int {
        return this.posts.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = posts.get(position).author.username
        holder.message.text = posts.get(position).message.body.text
    }

    class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.txt_title)
        var message: TextView = itemView.findViewById(R.id.txt_message)

    }
}