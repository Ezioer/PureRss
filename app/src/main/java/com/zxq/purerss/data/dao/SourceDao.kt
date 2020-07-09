package com.zxq.purerss.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zxq.purerss.data.entity.table.RSSFeedEntity
import com.zxq.purerss.data.entity.table.RSSReadedEntity
import com.zxq.purerss.data.entity.table.RSSSourceEntity

/**
 *  created by xiaoqing.zhou
 *  on 2020/5/25
 *  fun
 */
@Dao
interface SourceDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOneFeed(rssFeedEntity: RSSSourceEntity): Long

    @Query("select * from rsssource")
    fun getFeedsFromDb(): MutableList<RSSSourceEntity>

    @Query("select * from rsssource where feed_title = :title")
    fun isFeedIsExist(title: String): RSSSourceEntity

    @Query("select * from rsssource where feed_title like '%' || :key || '%' ")
    fun searchFeeds(key: String): MutableList<RSSSourceEntity>
}