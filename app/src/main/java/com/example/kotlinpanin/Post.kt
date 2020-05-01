package com.example.kotlinpanin

import java.util.*

data class Post(val posterName: String,
                val posterAvatar: Int,
                val date: Date,
                val text: String,
                val likes: Int,
                val comments: Int,
                val shares: Int)
