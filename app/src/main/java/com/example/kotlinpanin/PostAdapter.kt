package com.example.kotlinpanin

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import kotlinx.android.synthetic.main.post.view.*

class PostAdapter(private var items: List<PostUiModel>, private val context: Context) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.post, parent, false)).apply {
            hideBtn.setOnClickListener {
                hidePost(adapterPosition)
            }

            likesIcon.setOnClickListener {
                val postUiModel = items[adapterPosition]
                if (postUiModel.post.isLiked) {
                    items = items.toMutableList().apply {
                        set(adapterPosition, postUiModel.copy(
                                post = postUiModel.post.copy(isLiked = false, likes = postUiModel.post.likes.dec()))
                        )
                    }
                    Glide.with(context)
                            .load(R.drawable.likes_none)
                            .into(likesIcon)
                    likesCounter.text = if (postUiModel.post.likes > 0) postUiModel.post.likes.toString() else ""

                } else {
                    items = items.toMutableList().apply {
                        set(adapterPosition, postUiModel.copy(
                                post = postUiModel.post.copy(isLiked = true, likes = postUiModel.post.likes.inc()))
                        )
                    }
                    Glide.with(context)
                            .load(R.drawable.likes_yes)
                            .into(likesIcon)
                    likesCounter.text = postUiModel.post.likes.toString()
                }
            }
        }
    }

    override fun getItemCount(): Int = items.size

    private fun hidePost(position: Int) {
        items = items - items[position]
        notifyItemRemoved(position)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val postUiModel: PostUiModel = items[position]

        Glide.with(context)
                .load(postUiModel.post.posterAvatar)
                .transform(RoundedCorners(150))
                .into(holder.avatar)

        holder.date.text = postUiModel.dateFormatted
        holder.name.text = postUiModel.post.posterName

        when (postUiModel.post.type) {
            PostType.TEXT   -> {
                holder.repostComment.isVisible = false
                holder.repostInfo.isVisible = false
                holder.repostLine.isVisible = false
                holder.geoIcon.isVisible = false
                holder.address.isVisible = false
                holder.youtubeLayout.isVisible = false
                holder.adText.isVisible = false
            }

            PostType.REPOST -> {
                holder.repostComment.isVisible = true
                holder.repostInfo.isVisible = true
                holder.repostLine.isVisible = true
                holder.geoIcon.isVisible = false
                holder.address.isVisible = false
                holder.youtubeLayout.isVisible = false
                holder.adText.isVisible = false

                holder.repostInfo.text = context.getString(R.string.repost_from, postUiModel.post.repost?.posterName,
                        postUiModel.post.repost?.toUiModel()?.dateFormatted)
                holder.repostComment.text = postUiModel.post.text
            }

            PostType.EVENT  -> {
                holder.geoIcon.isVisible = true
                holder.address.isVisible = true
                holder.repostComment.isVisible = false
                holder.repostInfo.isVisible = false
                holder.repostLine.isVisible = false
                holder.youtubeLayout.isVisible = false
                holder.adText.isVisible = false

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

            PostType.VIDEO  -> {
                holder.youtubeLayout.isVisible = true
                holder.geoIcon.isVisible = false
                holder.address.isVisible = false
                holder.repostComment.isVisible = false
                holder.repostInfo.isVisible = false
                holder.repostLine.isVisible = false
                holder.adText.isVisible = false

                Glide.with(context)
                        .load("https://img.youtube.com/vi/" + postUiModel.post.video + "/0.jpg")
                        .into(holder.preview)
                Glide.with(context)
                        .load(R.drawable.play)
                        .into(holder.playBtn)
                holder.playBtn.setOnClickListener {
                    holder.preview.isVisible = false
                    holder.playBtn.isVisible = false
                    holder.youtubePlayerView.initialize("AIzaSyDSdq4pV4D-OpQi9bK1ngfE3sQoLZftkCU",
                            object : YouTubePlayer.OnInitializedListener {
                                override fun onInitializationSuccess(provider: YouTubePlayer.Provider,
                                                                     youTubePlayer: YouTubePlayer, b: Boolean) {
                                    youTubePlayer.cueVideo(postUiModel.post.video)
                                }

                                override fun onInitializationFailure(provider: YouTubePlayer.Provider,
                                                                     youTubeInitializationResult: YouTubeInitializationResult) = Unit
                            })
                }
            }

            PostType.AD     -> {
                holder.adText.isVisible = true
                holder.youtubeLayout.isVisible = false
                holder.geoIcon.isVisible = false
                holder.address.isVisible = false
                holder.repostComment.isVisible = false
                holder.repostInfo.isVisible = false
                holder.repostLine.isVisible = false
            }
        }

        holder.text.text = postUiModel.post.text

        holder.likesCounter.text = postUiModel.likesCounterString
        Glide.with(context)
                .load(postUiModel.likesIcon)
                .into(holder.likesIcon)

        holder.commentsCounter.text = postUiModel.commentsCounterString
        holder.commentsIcon.setImageResource(postUiModel.commentsIcon)

        holder.sharesCounter.text = postUiModel.sharesCounterString
        holder.sharesIcon.setImageResource(postUiModel.sharesIcon)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val avatar: ImageView = view.avatar
        val name: TextView = view.userName
        val date: TextView = view.date
        val hideBtn: Button = view.hideBtn
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