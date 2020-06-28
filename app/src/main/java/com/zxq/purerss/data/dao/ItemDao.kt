package com.zxq.purerss.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zxq.purerss.data.entity.table.RSSItemEntity

/**
 *  created by xiaoqing.zhou
 *  on 2020/5/25
 *  fun
 */
@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOneFeed(rssItemEntity: RSSItemEntity): Long

    @Query("select * from rssitem where item_link = :link")
    fun isExits(link: String):RSSItemEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOneContent(rssItemEntity: RSSItemEntity)
}