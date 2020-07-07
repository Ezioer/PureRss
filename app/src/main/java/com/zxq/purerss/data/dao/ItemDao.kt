package com.zxq.purerss.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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

    @Query("select * from rssitem where item_title = :link")
    fun isExits(link: String): RSSItemEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOneContent(rssItemEntity: RSSItemEntity)

    @Query("select * from rssitem where item_feed=:id order by feed_time desc")
    fun selectById(id: Long): MutableList<RSSItemEntity>

    @Query("delete from rssitem where item_feed=:id")
    fun deleteById(id: Long)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertReaded(rssReadedEntity: RSSReadedEntity)

    @Query("update rssitem set item_read =:state where  item_title =:title")
    fun updateReadedState(title: String, state: Int)

    @Query("update rssitem set item_read =:state where  item_id =:id")
    fun updateReadedState(id: Long, state: Boolean)

    @Query("select * from rssreaded where item_title =:title")
    fun readedIsExist(title: String): RSSReadedEntity

    @Query("select * from rssreaded")
    fun selectAllReaded(): MutableList<RSSReadedEntity>

    @Query("select * from rssreaded where item_title like '%' || :key || '%' or feed_title like '%' || :key || '%' ")
    fun searchReaded(key: String): MutableList<RSSReadedEntity>

    @Query("delete  from rssreaded where item_id =:id")
    fun removeReaded(id: Long)

    @Query("delete  from rssreaded where item_title =:title")
    fun removeReaded(title: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCollect(rssReadedEntity: RSSCollectEntity)

    @Query("select * from rsscollect where item_title =:title")
    fun collectIsExist(title: String): RSSCollectEntity

    @Query("select * from rsscollect")
    fun selectAllCollect(): MutableList<RSSCollectEntity>

    @Query("select * from rsscollect where item_title like '%' || :key || '%' or feed_title like '%' || :key || '%' ")
    fun searchCollect(key: String): MutableList<RSSCollectEntity>

    @Query("delete  from rsscollect where item_id =:id")
    fun removeCollect(id: Long)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertLater(rssReadedEntity: RSSLaterEntity)

    @Query("select * from rsslater where item_title =:title")
    fun laterIsExist(title: String): RSSLaterEntity

    @Query("select * from rsslater")
    fun selectAllLater(): MutableList<RSSLaterEntity>

    @Query("select * from rsslater where item_title like '%' || :key || '%' or feed_title like '%' || :key || '%' ")
    fun searchLater(key: String): MutableList<RSSLaterEntity>

    @Query("delete  from rsslater where item_id =:id")
    fun removeLater(id: Long)
}