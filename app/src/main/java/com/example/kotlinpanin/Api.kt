package com.example.kotlinpanin

import io.ktor.client.HttpClient
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature

object Api {

    val baseUrl = "https://post-app-back.herokuapp.com/v1/"
    val loginUrl = "${baseUrl}users/login"
    val registrationUrl = "${baseUrl}users/create"
    val getAllPostsUrl = "${baseUrl}posts/all-app"
    val getNewerPostsUrl = "${baseUrl}posts/all-app-new"
    val getOlderPostsUrl = "${baseUrl}posts/all-app-old"
    val postUrl = "${baseUrl}posts"
    val uploadFileUrl = "${baseUrl}upload"
    val downloadFileUrl = "${baseUrl}download"
    val likeUrl = "${baseUrl}posts/like"
    val shareUrl = "${baseUrl}posts/share"

    val client = HttpClient {
        install(JsonFeature) {
            serializer = GsonSerializer()
        }
    }
}