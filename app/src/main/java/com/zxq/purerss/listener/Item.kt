package com.zxq.purerss.listener

import androidx.recyclerview.widget.DiffUtil
import com.zxq.purerss.data.entity.RssItem
import com.zxq.purerss.data.entity.RssOpmlInfo
import com.zxq.purerss.data.entity.table.RSSFeedEntity
import com.zxq.purerss.data.entity.table.RSSItemEntity

/**
 *  created by xiaoqing.zhou
 *  on 2020/6/29
 *  fun
 */
class ItemDiffCallback(
    private var oldList: MutableList<RssItem>,
    private var newList: MutableList<RssItem>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].title == newList[newItemPosition].title
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].link == newList[newItemPosition].link
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

class OpmlItemDiffCallback : DiffUtil.ItemCallback<RssOpmlInfo>() {
    override fun areItemsTheSame(oldItem: RssOpmlInfo, newItem: RssOpmlInfo): Boolean {
        return oldItem.state == newItem.state
    }

    override fun areContentsTheSame(oldItem: RssOpmlInfo, newItem: RssOpmlInfo): Boolean {
        return oldItem.state == newItem.state
    }
}

class RssDiffCallback : DiffUtil.ItemCallback<RSSFeedEntity>() {
    override fun areItemsTheSame(oldItem: RSSFeedEntity, newItem: RSSFeedEntity): Boolean {
        return oldItem.feedTitle == newItem.feedTitle
    }

    override fun areContentsTheSame(oldItem: RSSFeedEntity, newItem: RSSFeedEntity): Boolean {
        return oldItem.feedTitle == newItem.feedTitle
    }
}

class RssItemDiffCallback(
    private var oldList: MutableList<RSSFeedEntity>,
    private var newList: MutableList<RSSFeedEntity>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].feedTitle == newList[newItemPosition].feedTitle
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].feedLink == newList[newItemPosition].feedLink
    }

}