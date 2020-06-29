package com.zxq.purerss.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zxq.purerss.data.entity.RssItem
import com.zxq.purerss.data.entity.table.RSSCollectEntity
import com.zxq.purerss.data.entity.table.RSSItemEntity
import com.zxq.purerss.data.entity.table.RSSLaterEntity
import com.zxq.purerss.data.entity.table.RSSReadedEntity

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

    @Query("select * from rssitem where item_feed=:id")
    fun selectById(id: Long): MutableList<RSSItemEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertReaded(rssReadedEntity: RSSReadedEntity)

    @Query("select * from rssreaded")
    fun selectAllReaded(): MutableList<RSSReadedEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCollect(rssReadedEntity: RSSCollectEntity)

    @Query("select * from rsscollect")
    fun selectAllCollect(): MutableList<RSSCollectEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertLater(rssReadedEntity: RSSLaterEntity)

    @Query("select * from rsslater")
    fun selectAllLater(): MutableList<RSSLaterEntity>
}