package com.example.kotlinpanin

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.youtube.player.YouTubeBaseActivity
import io.ktor.client.request.get
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : YouTubeBaseActivity(), CoroutineScope by MainScope() {

    lateinit var posts: List<Post>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        postList.layoutManager = LinearLayoutManager(this)
        fetchData()
    }

    fun fetchData() = launch {
        posts = withContext(Dispatchers.IO) {
            Api().client.get<List<Post>>(Api().url)
        }
        postList.adapter = PostAdapter(posts.map(Post::toUiModel), App.applicationContext())
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }

}