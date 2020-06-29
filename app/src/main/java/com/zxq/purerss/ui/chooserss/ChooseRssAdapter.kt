package com.zxq.purerss.ui.chooserss

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
import com.zxq.purerss.widget.TopicThumbnailTarget

/**
 *  created by xiaoqing.zhou
 *  on 2020/5/14
 *  fun
 */
class ChooseRssAdapter(private var context: Context): ListAdapter<RSSFeedEntity, ChooseRssAdapter.ChooseViewHolder>(RssDiffCallback()) {
    private var list = ArrayList<RSSFeedEntity>()
    private val selectedTint = context.getColor(R.color.topic_tint)
    private val selectedTopLeftCornerRadius =
        context.resources.getDimensionPixelSize(R.dimen.choose_item_radius)
    private val selectedDrawable = context.getDrawable(R.drawable.ic_checkmark)!!
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseViewHolder {
        return ChooseViewHolder(ChooseRssItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ChooseViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.isActivated = list.contains(item)
        holder.bind(item,selectedTint, selectedTopLeftCornerRadius, selectedDrawable,object: ChooseViewClick{
           override fun onClick(it: View, poetry: RSSFeedEntity) {
               it.isActivated = !it.isActivated
               if (it.isActivated) {
                   list.add(item)
               } else {
                   if (list.contains(item)) {
                       list.remove(item)
                   }
               }
           }
       })
    }

    inner class ChooseViewHolder(private val binding: ChooseRssItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(rss: RSSFeedEntity, @ColorInt selectedTint: Int,
                 @Px selectedTopLeftCornerRadius: Int,
                 selectedDrawable: Drawable, onClick: ChooseViewClick){
            binding.apply {
                itemevent = rss
                onChooseClick = onClick
                Glide.with(feedImage)
                    .asBitmap()
                    .load(R.drawable.default_two)
                    .placeholder(R.drawable.course_image_placeholder)
                    .into(
                        TopicThumbnailTarget(
                            feedImage,
                            selectedTint,
                            selectedTopLeftCornerRadius,
                            selectedDrawable
                        )
                    )
            }
        }
    }

    fun getSelectList(): List<RSSFeedEntity>{
        return list
    }
}

interface ChooseViewClick {
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