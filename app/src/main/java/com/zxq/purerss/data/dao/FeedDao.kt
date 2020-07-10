package com.zxq.purerss.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zxq.purerss.data.entity.table.RSSFeedEntity
import com.zxq.purerss.data.entity.table.RSSReadedEntity

/**
 *  created by xiaoqing.zhou
 *  on 2020/5/25
 *  fun
 */
@Dao
interface FeedDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOneFeed(rssFeedEntity: RSSFeedEntity): Long

    @Query("select * from rssfeed where parent_id = :id")
    fun getFeedsFromDb(id: Long): MutableList<RSSFeedEntity>

    @Query("select * from rssfeed where feed_title = :title")
    fun isFeedIsExist(title: String): RSSFeedEntity

    @Query("delete  from rssfeed where feed_id = :id")
    fun deleteFeed(id: Long)

    @Query("select * from rssfeed where feed_title like '%' || :key || '%' ")
    fun searchFeeds(key: String): MutableList<RSSFeedEntity>
}