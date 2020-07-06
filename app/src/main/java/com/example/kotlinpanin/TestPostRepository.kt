package com.example.kotlinpanin

import org.joda.time.LocalDateTime

class TestPostRepository : PostRepository {
        override fun getPosts(): List<Post> {
            val posts: ArrayList<Post> = ArrayList()

            val textPost = Post(
                    1,
                    "User One",
                    "R.mipmap.test_img_one",
                    LocalDateTime(2020, 5, 21, 15, 9, 0, 0).toDate().time,
                    PostType.TEXT,
                    null,
                    "Обычный текстовый пост",
                    null,
                    null,
                    null,
                    5,
                    0,
                    0
            )
            val videoPost = Post(
                    2,
                    "User Tree",
                    "R.mipmap.test_img_three",
                    LocalDateTime(2020, 5, 20, 13, 7, 0, 0).toDate().time,
                    PostType.VIDEO,
                    null,
                    "Пост с текстом и видео",
                    "yNLtreZkkL0",
                    null,
                    null,
                    0,
                    0,
                    3
            )
            val eventPost = Post(
                    3,
                    "User Two",
                    "R.mipmap.test_img_two",
                    LocalDateTime(2020, 5, 19, 14, 8, 0, 0).toDate().time,
                    PostType.EVENT,
                    null,
                    "Пост-событие с координатами",
                    null,
                    "Город Москва",
                    55.753215 to 37.622504,
                    0,
                    10,
                    0
            )
            val rePost = Post(
                    4,
                    "User Four",
                    "R.mipmap.test_img_four",
                    LocalDateTime(2020, 5, 18, 12, 6, 0, 0).toDate().time,
                    PostType.REPOST,
                    videoPost,
                    "Репост с текстовым комментарием",
                    null,
                    null,
                    null,
                    3,
                    11,
                    2
            )
            val adPost = Post(
                    5,
                    "User Five",
                    "R.mipmap.test_img_five",
                    LocalDateTime(2020, 5, 17, 11, 5, 0, 0).toDate().time,
                    PostType.AD,
                    null,
                    "Рекламный пост",
                    null,
                    null,
                    null,
                    0,
                    0,
                    0
            )

            posts.add(textPost)
            posts.add(eventPost)
            posts.add(videoPost)
            posts.add(rePost)
            posts.add(adPost)

            return posts
        }
}