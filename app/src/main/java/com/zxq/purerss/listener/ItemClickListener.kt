package com.zxq.purerss.listener

import android.view.View
import com.zxq.purerss.data.entity.RssItem
import com.zxq.purerss.data.entity.table.RSSItemEntity

interface ItemClickListener {
    fun onClick(view: View, rss: RSSItemEntity)
}

interface ItemSearchClickListener {
    fun onClick(view: View, rss: RssItem)
}

interface ItemTypeClickListener {
    fun onClick(view: View, rss: RSSItemEntity)
}