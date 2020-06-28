package com.zxq.purerss.ui.home

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zxq.purerss.R
import com.zxq.purerss.data.entity.RssItem
import com.zxq.purerss.data.entity.RssItemInfo
import com.zxq.purerss.data.entity.table.RSSFeedEntity
import com.zxq.purerss.databinding.ChooseRssItemBinding
import com.zxq.purerss.databinding.ItemAllfeedListBinding
import com.zxq.purerss.databinding.ItemFeedContentBinding
import com.zxq.purerss.widget.TopicThumbnailTarget

/**
 *  created by xiaoqing.zhou
 *  on 2020/5/14
 *  fun
 */
class FeedContentAdapter(private var context: Context): ListAdapter<RssItem, FeedContentAdapter.ChooseViewHolder>(ItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseViewHolder {
        return ChooseViewHolder(ItemFeedContentBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ChooseViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item,object: FeedItemClick{
            override fun onClick(view: View, rss: RssItem) {
//                val action = HomeFragmentDirections.actionHomeToDetail(RssItemInfo(rss.title,rss.link,rss.description,rss.pubdate,rss.author,0L,""))
//                view.findNavController().navigate(action)
            }
        })
    }

    inner class ChooseViewHolder(private val binding: ItemFeedContentBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(rss: RssItem, onClick: FeedItemClick){
            binding.apply {
                item = rss
                clickHandle = onClick
                executePendingBindings()
            }
        }
    }
}

interface FeedItemClick {
    fun onClick(view: View, rss: RssItem)
}

private class ItemDiffCallback: DiffUtil.ItemCallback<RssItem>(){
    override fun areItemsTheSame(oldItem: RssItem, newItem: RssItem): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: RssItem, newItem: RssItem): Boolean {
       return oldItem == newItem
    }
}