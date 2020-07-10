package com.zxq.purerss.data.entity.table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *  created by xiaoqing.zhou
 *  on 2020/5/7
 *  fun
 */
@Entity(tableName = "rssfolder")
data class RSSFolderEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "folder_id") var folderId: Long,
    @ColumnInfo(name = "folder_title") var folderTitle: String
) {
}