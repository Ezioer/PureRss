package com.zxq.purerss.data

import com.zxq.purerss.data.dao.FolderDao
import com.zxq.purerss.data.entity.table.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 *  created by xiaoqing.zhou
 *  on 2020/4/28
 *  fun
 */
class FolderRepository private constructor(
    private val folderDao: FolderDao
) {

    suspend fun insertFolder(title: String): Long = withContext(Dispatchers.IO) {
        if (folderDao.folderExist(title) == null) {
            folderDao.insertOneFolder(RSSFolderEntity(0, title))
        } else {
            0
        }
    }

    suspend fun getFolderFromDb(): MutableList<RSSFolderEntity> = withContext(Dispatchers.IO) {
        folderDao.selectFolder()
    }

    companion object {

        @Volatile
        private var instance: FolderRepository? = null

        fun getInstance(folderDao: FolderDao) =
            instance ?: synchronized(this) {
                instance ?: FolderRepository(folderDao).also { instance = it }
            }
    }
}