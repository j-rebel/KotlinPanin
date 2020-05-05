package com.example.kotlinpanin

import org.joda.time.LocalDateTime

class TestPostRepository : PostRepository {
    override fun getPosts(): List<Post> {
        val posts: ArrayList<Post> = ArrayList()

        posts.add(Post("User One", R.mipmap.test_img_one, LocalDateTime(2020, 5, 5, 15, 9, 0, 0), "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod", 5, 0, 0))
        posts.add(Post("User Two", R.mipmap.test_img_two, LocalDateTime(2020, 5, 5, 14, 8, 0, 0), "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod", 0, 10, 0))
        posts.add(Post("User Tree", R.mipmap.test_img_three, LocalDateTime(2020, 5, 5, 13, 7, 0, 0),"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod", 0, 0, 3))
        posts.add(Post("User Four", R.mipmap.test_img_four, LocalDateTime(2020, 5, 5, 12, 6, 0, 0), "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod", 3, 11, 2))
        posts.add(Post("User Five", R.mipmap.test_img_five, LocalDateTime(2020, 5, 5, 11, 5, 0, 0), "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod", 0, 0, 0))

        return posts
    }
}