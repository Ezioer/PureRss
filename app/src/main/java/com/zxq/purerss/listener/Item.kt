package com.zxq.purerss.listener

import androidx.recyclerview.widget.DiffUtil
import com.zxq.purerss.data.entity.RssItem
import com.zxq.purerss.data.entity.table.RSSItemEntity

/**
 *  created by xiaoqing.zhou
 *  on 2020/6/29
 *  fun
 */
class ItemDiffCallback : DiffUtil.ItemCallback<RssItem>() {
    override fun areItemsTheSame(oldItem: RssItem, newItem: RssItem): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: RssItem, newItem: RssItem): Boolean {
        return oldItem == newItem
    }
}


class ItemTypeDiffCallback : DiffUtil.ItemCallback<RSSItemEntity>() {
    override fun areItemsTheSame(oldItem: RSSItemEntity, newItem: RSSItemEntity): Boolean {
        return oldItem.itemTitle == newItem.itemTitle
    }

    override fun areContentsTheSame(oldItem: RSSItemEntity, newItem: RSSItemEntity): Boolean {
        return oldItem == newItem
    }
}