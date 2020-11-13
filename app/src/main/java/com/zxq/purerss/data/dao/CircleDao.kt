package com.zxq.purerss.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zxq.purerss.data.entity.table.CircleItemEntity

/**
 *  created by xiaoqing.zhou
 *  on 2020/5/25
 *  fun
 */
@Dao
interface CircleDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOneStatus(circleItemEntity: CircleItemEntity): Long

    @Query("select * from circleitem where status_id = :id")
    fun getStatusFromDbById(id: Long): CircleItemEntity

    @Query("select * from circleitem order by status_time desc")
    fun getAllStatusFromDb(): MutableList<CircleItemEntity>

    @Query("delete from circleitem where status_id = :id")
    fun deleteStatus(id: Long)
}