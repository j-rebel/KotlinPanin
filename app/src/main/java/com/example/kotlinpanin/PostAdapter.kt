package com.example.kotlinpanin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.post.view.*

class PostAdapter(private val items : List<PostUiModel>) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.post, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val postUiModel: PostUiModel = items[position]

        holder.avatar.setImageResource(postUiModel.post.posterAvatar)
        holder.date.text = postUiModel.dateFormatted
        holder.name.text = postUiModel.post.posterName
        holder.text.text = postUiModel.post.text

        // TODO эти все ветвления тоже можно заранее предусмотреть
        if (postUiModel.post.likes == 0) {
            holder.likesCounter.text = ""
            holder.likesIcon.setImageResource(R.drawable.likes_none)
        } else {
            holder.likesCounter.text = postUiModel.post.likes.toString()
            holder.likesIcon.setImageResource(R.drawable.likes_yes)
        }

        if (postUiModel.post.comments == 0) {
            holder.commentsCounter.text = ""
            holder.commentsIcon.setImageResource(R.drawable.comments_none)
        } else {
            holder.commentsCounter.text = postUiModel.post.comments.toString()
            holder.commentsIcon.setImageResource(R.drawable.comments_yes)
        }

        if (postUiModel.post.shares == 0) {
            holder.sharesCounter.text = ""
            holder.sharesIcon.setImageResource(R.drawable.shares_none)
        } else {
            holder.sharesCounter.text = postUiModel.post.shares.toString()
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
}