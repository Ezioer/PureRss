package com.zxq.purerss.data.entity.table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *  created by xiaoqing.zhou
 *  on 2020/5/7
 *  fun
 */
@Entity(tableName = "rsssource")
data class RSSSourceEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "feed_id") var feedId: Long,
    @ColumnInfo(name = "feed_title") var feedTitle: String,
    @ColumnInfo(name = "feed_link") var feedLink: String,
    @ColumnInfo(name = "feed_desc") var feedDesc: String,
    @ColumnInfo(name = "feed_pic") var feedPic: String
)