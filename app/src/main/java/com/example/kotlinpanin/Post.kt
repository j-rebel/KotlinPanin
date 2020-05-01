package com.example.kotlinpanin

import android.graphics.drawable.Drawable
import java.util.*

data class Post(val posterName: String,
                val posterAvatar: Drawable,
                val date: Date,
                val text: String,
                val likes: Int,
                val coments: Int,
                val shares: Int) {
    var dateString: String = secondsToString(date.time)
}

fun secondsToString(seconds: Long):String {
    val minutes: Long = seconds / 60
    val hours: Long = minutes / 60
    val days: Long = hours / 24
    val months: Long = days / 30
    val years: Long = months / 12

    return when (seconds) {
        in 0..59 -> "Менее минуты назад"
        in 60..119 -> "Минуту назад"
        in 120..299 -> "$minutes минуты назад"
        in 300..3599 -> "$minutes минут назад"
        in 3600..7199 -> "Час назад"
        in 7200..17999 -> "$hours часа назад"
        in 18000..86399 -> "$hours часов назад"
        in 86400..172799 -> "День назад"
        in 172800..431999 -> "$days дня назад"
        in 432000..2591999 -> "$days дней назад"
        in 2592000..5183999 -> "Месяц назад"
        in 5184000..12959999 -> "$months месяца назад"
        in 12960000..31103999 -> "$months месяцев назад"
        in 31104000..62207999 -> "Год назад"
        in 62208000..Long.MAX_VALUE -> "Несколько лет назад"
        else -> ""
    }
}