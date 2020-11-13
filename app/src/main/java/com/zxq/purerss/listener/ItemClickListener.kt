package com.zxq.purerss.listener

import android.view.View
import com.zxq.purerss.data.entity.RssItem
import com.zxq.purerss.data.entity.StatusInfo
import com.zxq.purerss.data.entity.table.RSSFolderEntity
import com.zxq.purerss.data.entity.table.RSSItemEntity
import com.zxq.purerss.data.entity.table.RSSSourceEntity

interface ItemClickListener {
    fun onClick(view: View, rss: RSSItemEntity)
}

interface ItemSearchClickListener {
    fun onClick(view: View, rss: RssItem)
}

interface ItemTypeClickListener {
    fun onClick(view: View, rss: RSSItemEntity)
}

interface AddFeedClickListener {
    fun onClick(view: View, rss: RSSSourceEntity)
}

interface FolderClickListener {
    fun onClick(view: View, rss: RSSFolderEntity)
}

interface StatusClickListener {
    fun onClick(view: View, rss: StatusInfo)
}