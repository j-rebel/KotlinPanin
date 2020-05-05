package com.example.kotlinpanin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.post.view.*
import java.util.*
import kotlin.collections.ArrayList

class PostAdapter(val items : ArrayList<Post>, val context: Context) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.post, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post: Post = items[position]

        holder.avatar.setImageResource(post.posterAvatar)
        holder.date.text = secondsToString(post.date.time)
        holder.name.text = post.posterName
        holder.text.text = post.text

        if (post.likes == 0) {
            holder.likesCounter.text = ""
            holder.likesIcon.setImageResource(R.drawable.likes_none)
        } else {
            holder.likesCounter.text = post.likes.toString()
            holder.likesIcon.setImageResource(R.drawable.likes_yes)
        }

        if (post.comments == 0) {
            holder.commentsCounter.text = ""
            holder.commentsIcon.setImageResource(R.drawable.comments_none)
        } else {
            holder.commentsCounter.text = post.comments.toString()
            holder.commentsIcon.setImageResource(R.drawable.comments_yes)
        }

        if (post.shares == 0) {
            holder.sharesCounter.text = ""
            holder.sharesIcon.setImageResource(R.drawable.shares_none)
        } else {
            holder.sharesCounter.text = post.shares.toString()
            holder.sharesIcon.setImageResource(R.drawable.shares_yes)
        }
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val avatar: ImageView = view.avatar
        val name: TextView = view.userName
        val date: TextView = view.date
        val text: TextView = view.text
        val likesIcon: ImageView = view.likesIcon
        val likesCounter: TextView = view.likesCounter
        val commentsIcon: ImageView = view.commentsIcon
        val commentsCounter: TextView = view.commentsCounter
        val sharesIcon: ImageView = view.sharesIcon
        val sharesCounter: TextView = view.sharesCounter
    }

    fun secondsToString(postDateInMillis: Long):String {
        val milliseconds = System.currentTimeMillis() - postDateInMillis

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
}

