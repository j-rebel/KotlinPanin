package com.example.kotlinpanin

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import io.ktor.client.features.ClientRequestException
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.header
import io.ktor.http.Parameters
import io.ktor.util.KtorExperimentalAPI
import kotlinx.android.synthetic.main.post_test.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class PostAdapterTest(private var items: List<PostUiModel>, private val context: Context, private  val TOKEN: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), CoroutineScope by MainScope() {

    open class TextViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val avatar: ImageView = view.avatar
        val name: TextView = view.userName
        val date: TextView = view.date
        val hideBtn: Button = view.hideBtn
        val text: TextView = view.text
        val likesIcon: ImageView = view.likesIcon
        val likesCounter: TextView = view.likesCounter
        val commentsIcon: ImageView = view.commentsIcon
        val commentsCounter: TextView = view.commentsCounter
        val sharesIcon: ImageView = view.sharesIcon
        val sharesCounter: TextView = view.sharesCounter
    }

    class AdViewHolder(view: View) : TextViewHolder(view) {
        val adText: TextView = view.adText
    }

    class VideoViewHolder(view: View) : TextViewHolder(view) {
        val youtubeLayout: FrameLayout = view.youtubeLayout
        val youtubePlayerView: YouTubePlayerView = view.youtubePlayerView
        val preview: ImageView = view.preview
        val playBtn: ImageView = view.playBtn
    }

    class EventViewHolder(view: View) : TextViewHolder(view) {
        val address: TextView = view.address
        val geoIcon: ImageView = view.geoIcon
    }

    class RepostViewHolder(view: View) : TextViewHolder(view) {
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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view: View = LayoutInflater.from(parent.context).inflate(R.layout.post_text, parent, false)
        var vh: RecyclerView.ViewHolder = TextViewHolder(view)
        when (viewType) {
            PostUiModel.POST_TEXT -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.post_text, parent, false)
                vh = TextViewHolder(view)
            }
            PostUiModel.POST_AD -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.post_ad, parent, false)
                vh = AdViewHolder(view)
            }
            PostUiModel.POST_VIDEO -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.post_video, parent, false)
                vh = VideoViewHolder(view)
            }
            PostUiModel.POST_EVENT -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.post_event, parent, false)
                vh = EventViewHolder(view)
            }
            PostUiModel.POST_REPOST -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.post_repost, parent, false)
                vh = RepostViewHolder(view)
            }
        }
        return vh
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position].type) {
            0 -> PostUiModel.POST_TEXT
            1 -> PostUiModel.POST_AD
            2 -> PostUiModel.POST_VIDEO
            3 -> PostUiModel.POST_EVENT
            4 -> PostUiModel.POST_REPOST
            else -> PostUiModel.POST_TEXT
        }
    }

    @KtorExperimentalAPI
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item: PostUiModel = items.get(position)
        val itemData = item.post
        var newItems = items

        (holder as TextViewHolder).hideBtn.setOnClickListener {
            hidePost(position)
        }
        Glide.with(context)
                .load(itemData.posterAvatar)
                .transform(RoundedCorners(150))
                .into((holder as TextViewHolder).avatar)

        (holder as TextViewHolder).date.text = item.dateFormatted
        (holder as TextViewHolder).name.text = itemData.posterName
        (holder as TextViewHolder).text.text = itemData.text

        (holder as TextViewHolder).likesCounter.text = item.likesCounterString
        Glide.with(context)
                .load(item.likesIcon)
                .into((holder as TextViewHolder).likesIcon)

        (holder as TextViewHolder).commentsCounter.text = item.commentsCounterString
        (holder as TextViewHolder).commentsIcon.setImageResource(item.commentsIcon)

        (holder as TextViewHolder).sharesCounter.text = item.sharesCounterString
        (holder as TextViewHolder).sharesIcon.setImageResource(item.sharesIcon)

        (holder as TextViewHolder).likesIcon.setOnClickListener {
            val postUiModel = items[position]
            if (itemData.isLiked) {
                newItems = items.toMutableList().apply {
                    set(position, postUiModel.copy(
                            post = itemData.copy(isLiked = false, likes = itemData.likes.dec()))
                    )
                }
                Glide.with(context)
                        .load(R.drawable.likes_none)
                        .into((holder as TextViewHolder).likesIcon)
                (holder as TextViewHolder).likesCounter.text = postUiModel.likesCounterString
            } else {
                newItems = items.toMutableList().apply {
                    set(position, postUiModel.copy(
                            post = itemData.copy(isLiked = true, likes = itemData.likes.inc()))
                    )
                }
                Glide.with(context)
                        .load(R.drawable.likes_yes)
                        .into((holder as TextViewHolder).likesIcon)
                (holder as TextViewHolder).likesCounter.text = postUiModel.likesCounterString
            }
            setNoteList(newItems)
            addLike(postUiModel.post.id)
        }

        when (item.type) {
            PostUiModel.POST_VIDEO -> {
                Glide.with(context)
                        .load("https://img.youtube.com/vi/" + itemData.video + "/0.jpg")
                        .into((holder as VideoViewHolder).preview)
                Glide.with(context)
                        .load(R.drawable.play)
                        .into((holder as VideoViewHolder).playBtn)
                (holder as VideoViewHolder).playBtn.setOnClickListener {
                    (holder as VideoViewHolder).preview.isVisible = false
                    (holder as VideoViewHolder).playBtn.isVisible = false
                    (holder as VideoViewHolder).youtubePlayerView.initialize(context.getString(R.string.key_one) + context.getString(R.string.key_two) + context.getString(R.string.key_three),
                            object : YouTubePlayer.OnInitializedListener {
                                override fun onInitializationSuccess(provider: YouTubePlayer.Provider,
                                                                     youTubePlayer: YouTubePlayer, b: Boolean) {
                                    youTubePlayer.cueVideo(itemData.video)
                                }

                                override fun onInitializationFailure(provider: YouTubePlayer.Provider,
                                                                     youTubeInitializationResult: YouTubeInitializationResult) = Unit
                            })
                }
            }
            PostUiModel.POST_EVENT -> {
                (holder as EventViewHolder).address.text = itemData.address

            }
            PostUiModel.POST_REPOST -> {
                (holder as RepostViewHolder).repostInfo.text = context.getString(R.string.repost_from, itemData.repost?.posterName,
                        itemData.repost?.toUiModel()?.dateFormatted)
                Glide.with(context)
                        .load(itemData.repost?.posterAvatar)
                        .transform(RoundedCorners(150))
                        .into(holder.repostedAvatar)

                (holder as RepostViewHolder).repostedDate.text = itemData.repost?.toUiModel()?.dateFormatted
                (holder as RepostViewHolder).repostedName.text = itemData.repost?.posterName
                when (itemData.repost?.type) {
                    PostType.TEXT   -> {
                        (holder as RepostViewHolder).repostedGeoIcon.isVisible = false
                        (holder as RepostViewHolder).repostedAddress.isVisible = false
                        (holder as RepostViewHolder).repostedYoutubeLayout.isVisible = false
                        (holder as RepostViewHolder).repostedAdText.isVisible = false
                    }
                    PostType.EVENT  -> {
                        (holder as RepostViewHolder).repostedGeoIcon.isVisible = true
                        (holder as RepostViewHolder).repostedAddress.isVisible = true
                        (holder as RepostViewHolder).repostedYoutubeLayout.isVisible = false
                        (holder as RepostViewHolder).repostedAdText.isVisible = false

                        (holder as RepostViewHolder).repostedAddress.text = itemData.repost?.address
                        Glide.with(context)
                                .load(R.drawable.location)
                                .into((holder as RepostViewHolder).repostedGeoIcon)
                        (holder as RepostViewHolder).repostedGeoIcon.setOnClickListener {
                            val intent = Intent().apply {
                                action = Intent.ACTION_VIEW
                                data = Uri.parse("geo:${itemData.repost?.geo?.first},${itemData.repost?.geo?.second}")
                            }
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            ContextCompat.startActivity(context, intent, Bundle.EMPTY)
                        }
                    }
                    PostType.VIDEO  -> {
                        (holder as RepostViewHolder).repostedYoutubeLayout.isVisible = true
                        (holder as RepostViewHolder).repostedGeoIcon.isVisible = false
                        (holder as RepostViewHolder).repostedAddress.isVisible = false
                        (holder as RepostViewHolder).repostedAdText.isVisible = false

                        Glide.with(context)
                                .load("https://img.youtube.com/vi/" + itemData.repost?.video + "/0.jpg")
                                .into((holder as RepostViewHolder).repostedPreview)
                        Glide.with(context)
                                .load(R.drawable.play)
                                .into((holder as RepostViewHolder).repostedPlayBtn)
                        (holder as RepostViewHolder).repostedPlayBtn.setOnClickListener {
                            (holder as RepostViewHolder).repostedPreview.isVisible = false
                            (holder as RepostViewHolder).repostedPlayBtn.isVisible = false
                            (holder as RepostViewHolder).repostedYoutubePlayerView.initialize(context.getString(R.string.key_one) + context.getString(R.string.key_two) + context.getString(R.string.key_three),
                                    object : YouTubePlayer.OnInitializedListener {
                                        override fun onInitializationSuccess(provider: YouTubePlayer.Provider,
                                                                             youTubePlayer: YouTubePlayer, b: Boolean) {
                                            youTubePlayer.cueVideo(itemData.repost?.video)
                                        }

                                        override fun onInitializationFailure(provider: YouTubePlayer.Provider,
                                                                             youTubeInitializationResult: YouTubeInitializationResult) = Unit
                                    })
                        }
                    }
                    PostType.AD     -> {
                        (holder as RepostViewHolder).repostedAdText.isVisible = true
                        (holder as RepostViewHolder).repostedYoutubeLayout.isVisible = false
                        (holder as RepostViewHolder).repostedGeoIcon.isVisible = false
                        (holder as RepostViewHolder).repostedAddress.isVisible = false
                    }
                }

                (holder as RepostViewHolder).repostedText.text = itemData.repost?.text
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun setNoteList(newItems: List<PostUiModel>) {
        val postDiffUtilCallback = PostDiffUtilCallback(items, newItems)
        val postDiffResult = DiffUtil.calculateDiff(postDiffUtilCallback)
        items = newItems
        postDiffResult.dispatchUpdatesTo(this)
    }

    private fun hidePost(position: Int) {
        val newItems = items - items[position]
        setNoteList(newItems)
    }

    @KtorExperimentalAPI
    fun addLike(postId: Long) = launch {
        try {
            val params = Parameters.build {
                append("post", postId.toString())
            }
            Api.client.submitForm(Api.likeUrl, params, false) {
                header("Authorization", "Bearer $TOKEN")
            }
        } catch (e: ClientRequestException) {
            Log.e("Error", e.message)
        }
    }
}