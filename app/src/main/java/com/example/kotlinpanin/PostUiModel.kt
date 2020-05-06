package com.example.kotlinpanin

import android.content.Context
import org.joda.time.LocalDateTime

data class PostUiModel(val post: Post, val dateFormatted: String)

fun Post.toUiModel(): PostUiModel = PostUiModel(this, secondsToString(this.date.toDate().time, App.applicationContext()))

private fun secondsToString(postDateInMillis: Long, context: Context):String {
    val milliseconds = LocalDateTime.now().toDate().time - postDateInMillis

    val seconds = milliseconds / 1000
    val minutes: Long = seconds / 60
    val hours: Long = minutes / 60
    val days: Long = hours / 24
    val months: Long = days / 30
    val years: Long = months / 12

    val periodMap = mapOf(
            Pair(0, years) to R.plurals.plurals_years,
            Pair(1, months) to R.plurals.plurals_months,
            Pair(2, days) to R.plurals.plurals_days,
            Pair(3, hours) to R.plurals.plurals_hours,
            Pair(4, minutes) to R.plurals.plurals_minutes
    )

    for ((k, v) in periodMap) {
        if (k.second > 0) {
            return context.resources.getQuantityString(
                    v,
                    k.second.toInt(),
                    k.second.toInt())
        }
    }
    return context.resources.getString(R.string.less_a_minute)
}
