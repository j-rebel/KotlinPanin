package com.example.kotlinpanin

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import kotlinx.android.synthetic.main.post.view.*

class PostAdapter(private val items: List<PostUiModel>, private val context: Context) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.post, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val postUiModel: PostUiModel = items[position]

        Glide.with(context)
                .load(postUiModel.post.posterAvatar)
                .into(holder.avatar)

        holder.date.text = postUiModel.dateFormatted
        holder.name.text = postUiModel.post.posterName

        if (postUiModel.post.type == PostType.REPOST) {
            holder.repostComment.visibility = View.VISIBLE
            holder.repostInfo.visibility = View.VISIBLE
            holder.repostLine.visibility = View.VISIBLE

            holder.repostInfo.text = context.getString(R.string.repost_from, postUiModel.post.repost?.posterName, postUiModel.post.repost?.toUiModel()?.dateFormatted)
            holder.repostComment.text = postUiModel.post.text

            postUiModel.post = postUiModel.post.repost!!
        }

        holder.text.text = postUiModel.post.text

        if (postUiModel.post.type == PostType.VIDEO) {
            holder.youtubeLayout.visibility = View.VISIBLE
            Glide.with(context)
                    .load("https://img.youtube.com/vi/" + postUiModel.post.video + "/0.jpg")
                    .into(holder.preview)
            Glide.with(context)
                    .load(R.drawable.play)
                    .into(holder.playBtn)
            holder.playBtn.setOnClickListener {
                holder.preview.visibility = View.GONE
                holder.playBtn.visibility = View.GONE
                holder.youtubePlayerView.initialize("AIzaSyDSdq4pV4D-OpQi9bK1ngfE3sQoLZftkCU",
                        object : YouTubePlayer.OnInitializedListener {
                            override fun onInitializationSuccess(provider: YouTubePlayer.Provider,
                                                                 youTubePlayer: YouTubePlayer, b: Boolean) {
                                youTubePlayer.cueVideo(postUiModel.post.video)
                            }

                            override fun onInitializationFailure(provider: YouTubePlayer.Provider,
                                                                 youTubeInitializationResult: YouTubeInitializationResult) {
                            }
                        })
            }
        }

        if (postUiModel.post.type == PostType.EVENT) {
            holder.geoIcon.visibility = View.VISIBLE
            holder.address.visibility = View.VISIBLE
            holder.address.text = postUiModel.post.address
            Glide.with(context)
                    .load(R.drawable.location)
                    .into(holder.geoIcon)
            holder.geoIcon.setOnClickListener {
                val intent = Intent().apply {
                    action = Intent.ACTION_VIEW
                    data = Uri.parse("geo:${postUiModel.post.geo?.first},${postUiModel.post.geo?.second}")
                }
                startActivity(context, intent, Bundle.EMPTY)
            }
        }

        if (postUiModel.post.type == PostType.AD) {
            holder.adText.visibility = View.VISIBLE
        }

        holder.likesCounter.text = postUiModel.likesCounterString
        Glide.with(context)
                .load(postUiModel.likesIcon)
                .into(holder.likesIcon)
        holder.likesIcon.setOnClickListener {
            if (postUiModel.post.isLiked) {
                postUiModel.post.isLiked = false
                postUiModel.post.likes--
                Glide.with(context)
                        .load(R.drawable.likes_none)
                        .into(holder.likesIcon)
                holder.likesCounter.text = if (postUiModel.post.likes > 0) postUiModel.post.likes.toString() else ""

            } else {
                postUiModel.post.isLiked = true
                postUiModel.post.likes++
                Glide.with(context)
                        .load(R.drawable.likes_yes)
                        .into(holder.likesIcon)
                holder.likesCounter.text = postUiModel.post.likes.toString()
            }
        }

        holder.commentsCounter.text = postUiModel.commentsCounterString
        holder.commentsIcon.setImageResource(postUiModel.commentsIcon)

        holder.sharesCounter.text = postUiModel.sharesCounterString
        holder.sharesIcon.setImageResource(postUiModel.sharesIcon)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val avatar: ImageView = view.avatar
        val name: TextView = view.userName
        val date: TextView = view.date
        val repostComment: TextView = view.repostComment
        val repostInfo: TextView = view.repostInfo
        val repostLine: TextView = view.repostLine
        val youtubeLayout: FrameLayout = view.youtubeLayout
        val youtubePlayerView: YouTubePlayerView = view.youtubePlayerView
        val preview: ImageView = view.preview
        val playBtn: ImageView = view.playBtn
        val text: TextView = view.text
        val address: TextView = view.address
        val geoIcon: ImageView = view.geoIcon
        val adText: TextView = view.adText
        val likesIcon: ImageView = view.likesIcon
        val likesCounter: TextView = view.likesCounter
        val commentsIcon: ImageView = view.commentsIcon
        val commentsCounter: TextView = view.commentsCounter
        val sharesIcon: ImageView = view.sharesIcon
        val sharesCounter: TextView = view.sharesCounter
    }
}