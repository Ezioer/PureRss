package com.zxq.purerss.data.entity.table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *  created by xiaoqing.zhou
 *  on 2020/5/7
 *  fun
 */
@Entity(tableName = "rsslater")
data class RSSLaterEntity(
    @ColumnInfo(name = "item_title") var itemTitle: String,
    @ColumnInfo(name = "item_link") var itemLink: String,
    @ColumnInfo(name = "item_desc") var itemDesc: String,
    @ColumnInfo(name = "item_author") var itemAuthor: String,
    @ColumnInfo(name = "item_date") var itemDate: String,
    @ColumnInfo(name = "item_pic") var itemPic: String,
    @ColumnInfo(name = "item_feed") var itemFeed: Long,
    @ColumnInfo(name = "feed_title") var feedTitle: String

) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "item_id")
    var itemId: Long = 0
}