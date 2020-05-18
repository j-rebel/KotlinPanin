package com.example.kotlinpanin

import androidx.recyclerview.widget.DiffUtil

class PostDiffUtilCallback (private val oldList: List<PostUiModel>, private val newList: List<PostUiModel>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem: PostUiModel = oldList[oldItemPosition]
        val newItem: PostUiModel = newList[newItemPosition]
        return oldItem.post.id == newItem.post.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem: PostUiModel = oldList[oldItemPosition]
        val newItem: PostUiModel = newList[newItemPosition]
        return (oldItem.post.repost == newItem.post.repost
                && oldItem.post.isLiked == newItem.post.isLiked
                && oldItem.post.isShared == newItem.post.isShared
                && oldItem.post.isCommented == newItem.post.isCommented
                && oldItem.post.type == newItem.post.type
                && oldItem.post.repost == newItem.post.repost
                && oldItem.post.likes == newItem.post.likes
                && oldItem.post.shares == newItem.post.shares
                && oldItem.post.comments == newItem.post.comments
                && oldItem.post.address == newItem.post.address
                && oldItem.post.geo?.first == newItem.post.geo?.first
                && oldItem.post.geo?.second == newItem.post.geo?.second
                && oldItem.post.date.equals(newItem.post.date)
                && oldItem.post.posterAvatar == newItem.post.posterAvatar
                && oldItem.post.posterName == newItem.post.posterName
                && oldItem.post.video == newItem.post.video
                && oldItem.post.text == newItem.post.text
                )
    }

}