package com.zxq.purerss.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zxq.purerss.data.entity.table.RSSFeedEntity

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

    @Query("select * from rssfeed where parent_id = :id order by add_time desc")
    fun getFeedsFromDbTime(id: Long): MutableList<RSSFeedEntity>

    @Query("select * from rssfeed where parent_id = :id order by see_count desc")
    fun getFeedsFromDbCount(id: Long): MutableList<RSSFeedEntity>

    @Query("select * from rssfeed")
    fun getAllFeedsFromDb(): MutableList<RSSFeedEntity>

    @Query("select * from rssfeed order by add_time desc")
    fun getAllFeedsByTime(): MutableList<RSSFeedEntity>

    @Query("select * from rssfeed order by see_count desc")
    fun getAllFeedsByCount(): MutableList<RSSFeedEntity>

    @Query("select * from rssfeed where feed_title = :title")
    fun isFeedIsExist(title: String): RSSFeedEntity

    @Query("delete  from rssfeed where feed_id = :id")
    fun deleteFeed(id: Long)

    @Query("update rssfeed set feed_title =:title, feed_desc = :subTitle , feed_link = :link , parent_id = :parentId  where feed_id = :id")
    fun update(title: String, subTitle: String, link: String, parentId: Long, id: Long)

    @Query("update rssfeed set see_count = :count  where feed_id = :id")
    fun update(count: Int, id: Long)

    @Query("select * from rssfeed where feed_title like '%' || :key || '%' ")
    fun searchFeeds(key: String): MutableList<RSSFeedEntity>
}