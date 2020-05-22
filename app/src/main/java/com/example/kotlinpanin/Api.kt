package com.example.kotlinpanin

import android.util.Log
import com.google.gson.reflect.TypeToken
import com.google.gson.GsonBuilder
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.http.ContentType
import java.lang.reflect.Type

class Api {
    val url = "https://raw.githubusercontent.com/j-rebel/KotlinPanin/master/app/src/main/java/com/example/kotlinpanin/PostJson.json"

    val client = HttpClient {
        install(JsonFeature) {
            acceptContentTypes = listOf(ContentType.Text.Plain, ContentType.Application.Json)
            serializer = GsonSerializer()
        }
    }

    /*fun getPostsFromJson(): List<Post> {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val jsonString: String = (gson.toJson(TestPostRepository().getPosts()))
        Log.d("result JSON", jsonString)
        val type = object : TypeToken<List<Post>>() {}.type
        return parseArray<List<Post>>(json = jsonString, typeToken = type)
    }

    inline fun <reified T> parseArray(json: String, typeToken: Type): T {
        val gson = GsonBuilder().setPrettyPrinting().create()
        return gson.fromJson<T>(json, typeToken)
    }*/
}