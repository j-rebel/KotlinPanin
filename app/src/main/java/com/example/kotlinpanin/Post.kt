package com.example.kotlinpanin

import org.joda.time.LocalDateTime

data class Post(val id: Long,
                val posterName: String,
                val posterAvatar: Int,
                val date: LocalDateTime,
                val type: PostType,
                val repost: Post?,
                val text: String,
                val video: String?,
                val address: String?,
                val geo: Pair<Double, Double>?,
                var likes: Int,
                var comments: Int,
                var shares: Int,
                var isLiked: Boolean = false,
                var isCommented: Boolean = false,
                var isShared: Boolean = false)
