package com.example.kotlinpanin

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.youtube.player.YouTubeBaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : YouTubeBaseActivity(), CoroutineScope by MainScope() {

    private var posts: List<Post> = Api().getPostsFromJson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        postList.layoutManager = LinearLayoutManager(this)
        postList.adapter = PostAdapter(posts.map(Post::toUiModel), this)
    }
}