package com.zxq.purerss.listener

import android.view.View
import com.zxq.purerss.data.entity.RssItem

interface ItemClickListener {
    fun onClick(view: View, rss: RssItem)
}