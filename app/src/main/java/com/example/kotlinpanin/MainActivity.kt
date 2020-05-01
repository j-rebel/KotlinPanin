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
        posts.add(Post("User One", R.mipmap.test_img_one, Date(2020, 4, 1), getString(R.string.ipsum_string), 5, 0, 0))
        posts.add(Post("User Two", R.mipmap.test_img_two, Date(2020, 3, 25), getString(R.string.ipsum_string), 0, 10, 0))
        posts.add(Post("User Tree", R.mipmap.test_img_three, Date(2020, 2, 1), getString(R.string.ipsum_string), 0, 0, 3))
        posts.add(Post("User Four", R.mipmap.test_img_four, Date(2019, 4, 1), getString(R.string.ipsum_string), 3, 11, 2))
        posts.add(Post("User Five", R.mipmap.test_img_five, Date(2005, 4, 1), getString(R.string.ipsum_string), 0, 0, 0))
    }
}