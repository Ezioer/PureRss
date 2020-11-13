package com.zxq.purerss.data.entity.table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *  created by xiaoqing.zhou
 *  on 2020/5/7
 *  fun
 */
@Entity(tableName = "userinfo")
data class UserInfoEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "status_id") var itemId: Long,
    @ColumnInfo(name = "status_content") var statusContent: String,
    @ColumnInfo(name = "status_name") var statusName: String,
    @ColumnInfo(name = "status_time") var statusTime: Long,
    @ColumnInfo(name = "status_like") var statusLike: Int,
    @ColumnInfo(name = "status_pic") var statusPic: Int,
    @ColumnInfo(name = "status_pics") var statusPics: String
) {

}