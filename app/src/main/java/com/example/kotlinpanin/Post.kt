package com.example.kotlinpanin

import org.joda.time.LocalDateTime

data class Post(val id: Long,
                val posterName: String,
                val posterAvatar: Int,
                val date: Long,
                val type: PostType,
                val repost: Post?,
                val text: String,
                val video: String?,
                val address: String?,
                val geo: Pair<Double, Double>?,
                val likes: Int,
                val comments: Int,
                val shares: Int,
                val isLiked: Boolean = false,
                val isCommented: Boolean = false,
                val isShared: Boolean = false)