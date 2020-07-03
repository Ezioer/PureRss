package com.zxq.purerss.data.entity

/**
 *  created by xiaoqing.zhou
 *  on 2020/6/16
 *  fun
 */
data class RssFeed(
    var title: String = "",
    var pubdate: String = "",
    var subTitle: String = "",
    var link: String = "",
    var items: ArrayList<RssItem> = ArrayList()
) {
    fun addItem(item: RssItem){
        items?.add(item)
    }
}

data class RssItem(
    var title: String = "",
    var link: String = "",
    var description: String = "",
    var pubdate: String = "",
    var author: String = "",
    var albumPic: String = ""
)