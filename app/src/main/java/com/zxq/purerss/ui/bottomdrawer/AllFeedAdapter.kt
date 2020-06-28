package com.zxq.purerss.ui.bottomdrawer

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zxq.purerss.R
import com.zxq.purerss.data.entity.table.RSSFeedEntity
import com.zxq.purerss.databinding.ChooseRssItemBinding
import com.zxq.purerss.databinding.ItemAllfeedListBinding
import com.zxq.purerss.widget.TopicThumbnailTarget

/**
 *  created by xiaoqing.zhou
 *  on 2020/5/14
 *  fun
 */
class AllFeedAdapter(private var context: Context,private var onClick: FeedClick): ListAdapter<RSSFeedEntity, AllFeedAdapter.ChooseViewHolder>(RssDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseViewHolder {
        return ChooseViewHolder(ItemAllfeedListBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ChooseViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item,onClick)
    }

    inner class ChooseViewHolder(private val binding: ItemAllfeedListBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(rss: RSSFeedEntity, onClick: FeedClick){
            binding.apply {
                item = rss
//                clickHandle = onClick
                executePendingBindings()
            }
        }
    }
}

interface FeedClick {
    fun onClick(view: View, rss: RSSFeedEntity)
}

private class RssDiffCallback: DiffUtil.ItemCallback<RSSFeedEntity>(){
    override fun areItemsTheSame(oldItem: RSSFeedEntity, newItem: RSSFeedEntity): Boolean {
        return oldItem.feedId == newItem.feedId
    }

    override fun areContentsTheSame(oldItem: RSSFeedEntity, newItem: RSSFeedEntity): Boolean {
       return oldItem == newItem
    }
}