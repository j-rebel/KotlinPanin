package com.example.kotlinpanin

import android.content.Context
import org.joda.time.LocalDateTime

data class PostUiModel(val post: Post, val dateFormatted: String, val type: Int) {
    val likesCounterString = if (post.likes > 0) "${post.likes}" else ""
    val commentsCounterString = if (post.comments > 0) "${post.comments}" else ""
    val sharesCounterString = if (post.shares > 0) "${post.shares}" else ""

    val likesIcon = if(post.isLiked) R.drawable.likes_yes else R.drawable.likes_none
    val commentsIcon = if(post.isCommented) R.drawable.comments_yes else R.drawable.comments_none
    val sharesIcon = if(post.isShared) R.drawable.shares_yes else R.drawable.shares_none

    companion object {
        const val POST_TEXT = 0
        const val POST_AD = 1
        const val POST_VIDEO = 2
        const val POST_EVENT = 3
        const val POST_REPOST = 4
        const val POST_HEAD = 5
        const val POST_TAIL = 6
    }
}

fun Post.toUiModel(): PostUiModel = PostUiModel(this, secondsToString(this.date, App.applicationContext()), postTypeToInt(this.type))

private fun secondsToString(postDateInMillis: Long, context: Context):String {
    val milliseconds = LocalDateTime.now().toDate().time - postDateInMillis

    val seconds = milliseconds / 1000
    val minutes: Long = seconds / 60
    val hours: Long = minutes / 60
    val days: Long = hours / 24
    val months: Long = days / 30
    val years: Long = months / 12

    val periodMap = mapOf(
            Pair(0, years) to R.plurals.plurals_years,
            Pair(1, months) to R.plurals.plurals_months,
            Pair(2, days) to R.plurals.plurals_days,
            Pair(3, hours) to R.plurals.plurals_hours,
            Pair(4, minutes) to R.plurals.plurals_minutes
    )

    for ((k, v) in periodMap) {
        if (k.second > 0) {
            return context.resources.getQuantityString(
                    v,
                    k.second.toInt(),
                    k.second.toInt())
        }
    }
    return context.resources.getString(R.string.less_a_minute)
}

private fun postTypeToInt(postType: PostType): Int {
    return when (postType) {
        PostType.TEXT -> 0
        PostType.AD -> 1
        PostType.VIDEO -> 2
        PostType.EVENT -> 3
        PostType.REPOST -> 4
        PostType.HEAD -> 5
        PostType.TAIL -> 6
    }
}