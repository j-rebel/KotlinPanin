package com.example.kotlinpanin

import android.util.Log
import android.widget.Toast
import com.google.gson.reflect.TypeToken
import com.google.gson.GsonBuilder
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.cookies.AcceptAllCookiesStorage
import io.ktor.client.features.cookies.HttpCookies
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.forms.submitForm
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.util.KtorExperimentalAPI
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.*
import java.lang.reflect.Type

class Api : CoroutineScope by MainScope() {

    val baseUrl = "https://post-app-back.herokuapp.com/v1/"
    val loginUrl = "${baseUrl}users/login"
    val registrationUrl = "${baseUrl}users/create"
    val getAllPostsUrl = "${baseUrl}posts/all-app"
    val postUrl = "${baseUrl}posts"
    val uploadFileUrl = "${baseUrl}upload"
    val downloadFileUrl = "${baseUrl}download"
    val likeUrl = "${baseUrl}posts/like"
    val shareUrl = "${baseUrl}posts/share"

    @KtorExperimentalAPI
    val client = HttpClient {
        install(JsonFeature) {
            acceptContentTypes = listOf(ContentType.Application.Json)
            serializer = GsonSerializer()
        }
        install(HttpCookies) {
            // Will keep an in-memory map with all the cookies from previous requests.
            storage = AcceptAllCookiesStorage()
        }
    }

}