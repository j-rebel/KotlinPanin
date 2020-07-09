package com.example.kotlinpanin

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.youtube.player.YouTubeBaseActivity
import io.ktor.client.features.cookies.cookies
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.util.KtorExperimentalAPI
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : YouTubeBaseActivity(), CoroutineScope by MainScope() {

    val APP_PREFERENCES = "mysettings"
    val APP_PREFERENCES_TOKEN = "TOKEN"
    var TOKEN = ""

    @KtorExperimentalAPI
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mSettings: SharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        TOKEN = mSettings.getString(APP_PREFERENCES_TOKEN ,"").toString()
        if (TOKEN.equals("")) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        } else {
            setContentView(R.layout.activity_main)
            postList.layoutManager = LinearLayoutManager(this)
            fetchData()
        }

    }

    @KtorExperimentalAPI
    fun fetchData() = launch {
        fun selector(p: Post): Long = p.date
        val allPosts = withContext(Dispatchers.IO) {
            Api().client.get<List<Post>>(Api().getAllPostsUrl) {
                header("Authorization", "Bearer $TOKEN")
                header("Cookie", Api().client.cookies("MY_SESSION"))
            }
        }
        Log.i("cookie", Api().client.cookies("MY_SESSION").toString())
        allPosts.sortedByDescending { selector(it) }
        val userPosts = allPosts.filter { it.type != PostType.AD }.sortedByDescending { selector(it) }
        val adPosts = allPosts.filter { it.type == PostType.AD }.sortedByDescending { selector(it) }
        val adapterPosts: MutableList<Post> = mutableListOf<Post>()
        for (i in 0 until userPosts.size) {
            if (i > 0 && i % 2 == 0) {
                for (j in 0 until adPosts.size) {
                    adapterPosts.add(adPosts[j])
                }
            }
            adapterPosts.add(userPosts[i])
        }

        postList.adapter = PostAdapter(adapterPosts.map(Post::toUiModel), App.applicationContext(), TOKEN)
        for (i in 1..adapterPosts.size) {
            delay(500)
            progressBar.incrementProgressBy(100 / adapterPosts.size)
            progressText.text = App.applicationContext().getString(R.string.loading_progress, progressBar.progress)
        }

        val animation = AnimationUtils.loadAnimation(App.applicationContext(), R.anim.alpha)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
                progressLayout.isVisible = false
            }

            override fun onAnimationEnd(animation: Animation?) {
                progressLayout.isVisible = false
            }

            override fun onAnimationStart(animation: Animation?) {
                Log.d("Animation", "started")
            }
        })
        progressLayout.startAnimation(animation)
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }

}