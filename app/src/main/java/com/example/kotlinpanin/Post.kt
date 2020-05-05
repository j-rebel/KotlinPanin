package com.example.kotlinpanin

import org.joda.time.LocalDateTime

data class Post(val posterName: String,
                val posterAvatar: Int,
                val date: LocalDateTime,
                val text: String,
                val likes: Int,
                val comments: Int,
                val shares: Int)
