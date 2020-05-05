package com.example.kotlinpanin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var posts: List<Post> = TestPostRepository().getPosts()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        postList.layoutManager = LinearLayoutManager(this)
        postList.adapter = PostAdapter(posts, this)
    }
}