package com.zxq.purerss.listener

import android.content.pm.ShortcutInfo
import androidx.recyclerview.widget.DiffUtil
import com.zxq.purerss.data.entity.RssItem
import com.zxq.purerss.data.entity.RssOpmlInfo
import com.zxq.purerss.data.entity.StatusInfo
import com.zxq.purerss.data.entity.table.RSSFeedEntity
import com.zxq.purerss.data.entity.table.RSSFolderEntity
import com.zxq.purerss.data.entity.table.RSSItemEntity
import com.zxq.purerss.data.entity.table.RSSSourceEntity

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

class ItemRssDiffCallback : DiffUtil.ItemCallback<RSSItemEntity>() {
    override fun areItemsTheSame(oldItem: RSSItemEntity, newItem: RSSItemEntity): Boolean {
        return oldItem.itemTitle == newItem.itemTitle
    }

    override fun areContentsTheSame(oldItem: RSSItemEntity, newItem: RSSItemEntity): Boolean {
        return oldItem.itemRead == newItem.itemRead
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

class SelectCutsItemDiffCallback : DiffUtil.ItemCallback<RSSFeedEntity>() {
    override fun areItemsTheSame(oldItem: RSSFeedEntity, newItem: RSSFeedEntity): Boolean {
        return oldItem.state == newItem.state
    }

    override fun areContentsTheSame(oldItem: RSSFeedEntity, newItem: RSSFeedEntity): Boolean {
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

class RssSourceDiffCallback : DiffUtil.ItemCallback<RSSSourceEntity>() {
    override fun areItemsTheSame(oldItem: RSSSourceEntity, newItem: RSSSourceEntity): Boolean {
        return oldItem.feedTitle == newItem.feedTitle
    }

    override fun areContentsTheSame(oldItem: RSSSourceEntity, newItem: RSSSourceEntity): Boolean {
        return oldItem.feedTitle == newItem.feedTitle
    }
}

class RssFolderDiffCallback : DiffUtil.ItemCallback<RSSFolderEntity>() {
    override fun areItemsTheSame(oldItem: RSSFolderEntity, newItem: RSSFolderEntity): Boolean {
        return oldItem.folderTitle == newItem.folderTitle
    }

    override fun areContentsTheSame(oldItem: RSSFolderEntity, newItem: RSSFolderEntity): Boolean {
        return oldItem.folderTitle == newItem.folderTitle
    }
}

class ShortCutsDiffCallback : DiffUtil.ItemCallback<ShortcutInfo>() {
    override fun areItemsTheSame(oldItem: ShortcutInfo, newItem: ShortcutInfo): Boolean {
        return oldItem.longLabel == newItem.longLabel
    }

    override fun areContentsTheSame(oldItem: ShortcutInfo, newItem: ShortcutInfo): Boolean {
        return oldItem.longLabel == newItem.longLabel
    }
}

class RssItemDiffCallback(
    private var oldList: MutableList<RSSItemEntity>,
    private var newList: MutableList<RSSItemEntity>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].itemTitle == newList[newItemPosition].itemTitle
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].itemRead == newList[newItemPosition].itemRead
    }

}

class StatusItemDiffCallback(
    private var oldList: MutableList<StatusInfo>,
    private var newList: MutableList<StatusInfo>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].time == newList[newItemPosition].time
    }

}