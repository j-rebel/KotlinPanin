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
import io.ktor.client.request.get
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : YouTubeBaseActivity(), CoroutineScope by MainScope() {

    val APP_PREFERENCES = "mysettings"
    val APP_PREFERENCES_TOKEN = "TOKEN"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mSettings: SharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        if (mSettings.getString(APP_PREFERENCES_TOKEN ,"").equals("")) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        setContentView(R.layout.activity_main)
        postList.layoutManager = LinearLayoutManager(this)
        //fetchData()
    }

/*    fun fetchData() = launch {
        val allPosts = withContext(Dispatchers.IO) {
            Api().client.get<List<Post>>(Api().url)
        }
        val userPosts = allPosts.filter { it.type != PostType.AD }
        val adPosts = allPosts.filter { it.type == PostType.AD }
        val adapterPosts: MutableList<Post> = mutableListOf<Post>()
        for (i in 0 until userPosts.size) {
            if (i > 0 && i % 2 == 0) {
                for (j in 0 until adPosts.size) {
                    adapterPosts.add(adPosts[j])
                }
            }
            adapterPosts.add(userPosts[i])
        }

        postList.adapter = PostAdapter(adapterPosts.map(Post::toUiModel), App.applicationContext())
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
    }*/

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }

}