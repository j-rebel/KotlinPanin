package com.example.kotlinpanin

interface PostRepository {
    fun getPosts(): List<Post>
}