package com.zxq.purerss.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zxq.purerss.data.entity.table.RSSFeedEntity
import com.zxq.purerss.data.entity.table.RSSFolderEntity
import com.zxq.purerss.data.entity.table.RSSReadedEntity

/**
 *  created by xiaoqing.zhou
 *  on 2020/5/25
 *  fun
 */
@Dao
interface FolderDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOneFolder(rssFolderEntity: RSSFolderEntity): Long

    @Query("select * from rssfolder")
    fun selectFolder(): MutableList<RSSFolderEntity>

    @Query("select * from rssfolder where folder_title =:title")
    fun folderExist(title: String): RSSFolderEntity

    @Query("delete  from rssfolder where folder_id =:id")
    fun delete(id: Long)

    @Query("update rssfolder set folder_title =:title  where folder_id =:id")
    fun update(title: String, id: Long)
}