package com.example.kotlinpanin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager

import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private val posts: ArrayList<Post> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addPosts()
        postList.layoutManager = LinearLayoutManager(this)
        postList.adapter = PostAdapter(posts, this)

    }

    fun addPosts() {
        posts.add(Post("User One", R.mipmap.test_img_one, Date(), getString(R.string.ipsum_string), 5, 0, 0))
        posts.add(Post("User Two", R.mipmap.test_img_two, Date(), getString(R.string.ipsum_string), 0, 10, 0))
        posts.add(Post("User Tree", R.mipmap.test_img_three, Date(), getString(R.string.ipsum_string), 0, 0, 3))
        posts.add(Post("User Four", R.mipmap.test_img_four, Date(), getString(R.string.ipsum_string), 3, 11, 2))
        posts.add(Post("User Five", R.mipmap.test_img_five, Date(), getString(R.string.ipsum_string), 0, 0, 0))
    }
}