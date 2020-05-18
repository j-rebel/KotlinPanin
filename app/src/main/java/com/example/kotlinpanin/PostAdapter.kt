package com.example.kotlinpanin

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import kotlinx.android.synthetic.main.post.view.adText
import kotlinx.android.synthetic.main.post.view.address
import kotlinx.android.synthetic.main.post.view.avatar
import kotlinx.android.synthetic.main.post.view.commentsCounter
import kotlinx.android.synthetic.main.post.view.commentsIcon
import kotlinx.android.synthetic.main.post.view.date
import kotlinx.android.synthetic.main.post.view.geoIcon
import kotlinx.android.synthetic.main.post.view.hideBtn
import kotlinx.android.synthetic.main.post.view.likesCounter
import kotlinx.android.synthetic.main.post.view.likesIcon
import kotlinx.android.synthetic.main.post.view.playBtn
import kotlinx.android.synthetic.main.post.view.preview
import kotlinx.android.synthetic.main.post.view.repostInfo
import kotlinx.android.synthetic.main.post.view.sharesCounter
import kotlinx.android.synthetic.main.post.view.sharesIcon
import kotlinx.android.synthetic.main.post.view.text
import kotlinx.android.synthetic.main.post.view.userName
import kotlinx.android.synthetic.main.post.view.youtubeLayout
import kotlinx.android.synthetic.main.post.view.youtubePlayerView
import kotlinx.android.synthetic.main.post_test.view.*

class PostAdapter(private var items: List<PostUiModel>, private val context: Context) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.post_test, parent, false)).apply {
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
        val newItems = items - items[position]
        setNoteList(newItems)
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
                holder.repostInfo.isVisible = false
                holder.repostedMainContent.isVisible = false
                holder.geoIcon.isVisible = false
                holder.address.isVisible = false
                holder.youtubeLayout.isVisible = false
                holder.adText.isVisible = false
            }

            PostType.REPOST -> {
                holder.repostInfo.isVisible = true
                holder.repostedMainContent.isVisible = true
                holder.geoIcon.isVisible = false
                holder.address.isVisible = false
                holder.youtubeLayout.isVisible = false
                holder.adText.isVisible = false

                holder.repostInfo.text = context.getString(R.string.repost_from, postUiModel.post.repost?.posterName,
                        postUiModel.post.repost?.toUiModel()?.dateFormatted)
                Glide.with(context)
                        .load(postUiModel.post.repost?.posterAvatar)
                        .transform(RoundedCorners(150))
                        .into(holder.repostedAvatar)

                holder.repostedDate.text = postUiModel.post.repost?.toUiModel()?.dateFormatted
                holder.repostedName.text = postUiModel.post.repost?.posterName

                when (postUiModel.post.repost?.type) {
                    PostType.TEXT   -> {
                        holder.repostedGeoIcon.isVisible = false
                        holder.repostedAddress.isVisible = false
                        holder.repostedYoutubeLayout.isVisible = false
                        holder.repostedAdText.isVisible = false
                    }
                    PostType.EVENT  -> {
                        holder.repostedGeoIcon.isVisible = true
                        holder.repostedAddress.isVisible = true
                        holder.repostedYoutubeLayout.isVisible = false
                        holder.repostedAdText.isVisible = false

                        holder.repostedAddress.text = postUiModel.post.repost?.address
                        Glide.with(context)
                                .load(R.drawable.location)
                                .into(holder.repostedGeoIcon)
                        holder.repostedGeoIcon.setOnClickListener {
                            val intent = Intent().apply {
                                action = Intent.ACTION_VIEW
                                data = Uri.parse("geo:${postUiModel.post.repost?.geo?.first},${postUiModel.post.repost?.geo?.second}")
                            }
                            startActivity(context, intent, Bundle.EMPTY)
                        }
                    }
                    PostType.VIDEO  -> {
                        holder.repostedYoutubeLayout.isVisible = true
                        holder.repostedGeoIcon.isVisible = false
                        holder.repostedAddress.isVisible = false
                        holder.repostedAdText.isVisible = false

                        Glide.with(context)
                                .load("https://img.youtube.com/vi/" + postUiModel.post.repost?.video + "/0.jpg")
                                .into(holder.repostedPreview)
                        Glide.with(context)
                                .load(R.drawable.play)
                                .into(holder.repostedPlayBtn)
                        holder.repostedPlayBtn.setOnClickListener {
                            holder.repostedPreview.isVisible = false
                            holder.repostedPlayBtn.isVisible = false
                            holder.repostedYoutubePlayerView.initialize(context.getString(R.string.key_one + R.string.key_two + R.string.key_three),
                                    object : YouTubePlayer.OnInitializedListener {
                                        override fun onInitializationSuccess(provider: YouTubePlayer.Provider,
                                                                             youTubePlayer: YouTubePlayer, b: Boolean) {
                                            youTubePlayer.cueVideo(postUiModel.post.repost?.video)
                                        }

                                        override fun onInitializationFailure(provider: YouTubePlayer.Provider,
                                                                             youTubeInitializationResult: YouTubeInitializationResult) = Unit
                                    })
                        }
                    }
                    PostType.AD     -> {
                        holder.repostedAdText.isVisible = true
                        holder.repostedYoutubeLayout.isVisible = false
                        holder.repostedGeoIcon.isVisible = false
                        holder.repostedAddress.isVisible = false
                        }
                }

                holder.repostedText.text = postUiModel.post.repost?.text

            }

            PostType.EVENT  -> {
                holder.geoIcon.isVisible = true
                holder.address.isVisible = true
                holder.repostInfo.isVisible = false
                holder.repostedMainContent.isVisible = false
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
                holder.repostInfo.isVisible = false
                holder.repostedMainContent.isVisible = false
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
                holder.repostedMainContent.isVisible = false
                holder.repostInfo.isVisible = false
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
        val repostInfo: TextView = view.repostInfo
        val repostedMainContent: LinearLayout = view.repostedMainContent
        val repostedAvatar: ImageView = view.repostedAvatar
        val repostedName: TextView = view.repostedUserName
        val repostedDate: TextView = view.repostedDate
        val repostedYoutubeLayout: FrameLayout = view.repostedYoutubeLayout
        val repostedYoutubePlayerView: YouTubePlayerView = view.repostedYoutubePlayerView
        val repostedPreview: ImageView = view.repostedPreview
        val repostedPlayBtn: ImageView = view.repostedPlayBtn
        val repostedText: TextView = view.repostedText
        val repostedAddress: TextView = view.repostedAddress
        val repostedGeoIcon: ImageView = view.repostedGeoIcon
        val repostedAdText: TextView = view.repostedAdText
        val repostedLikesIcon: ImageView = view.repostedLikesIcon
        val repostedLikesCounter: TextView = view.repostedLikesCounter
        val repostedCommentsIcon: ImageView = view.repostedCommentsIcon
        val repostedCommentsCounter: TextView = view.repostedCommentsCounter
        val repostedSharesIcon: ImageView = view.repostedSharesIcon
        val repostedSharesCounter: TextView = view.repostedSharesCounter
    }

    fun setNoteList(newItems: List<PostUiModel>) {
        val postDiffUtilCallback = PostDiffUtilCallback(items, newItems)
        val postDiffResult = DiffUtil.calculateDiff(postDiffUtilCallback)
        items = newItems
        postDiffResult.dispatchUpdatesTo(this)
    }
}