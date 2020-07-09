package com.zxq.purerss.data.entity

import com.zxq.purerss.data.dao.SourceDao
import com.zxq.purerss.data.entity.table.RSSSourceEntity
import com.zxq.purerss.utils.ReadOPML
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 *  created by xiaoqing.zhou
 *  on 2020/4/28
 *  fun
 */
class SourceRepository private constructor(
    private val sourceDao: SourceDao
) {

    suspend fun insertAllFromOpml() = withContext(Dispatchers.IO) {
        val list = ReadOPML.read()
        for (item in list!!) {
            if (sourceDao.isFeedIsExist(item.title) == null) {
                sourceDao.insertOneFeed(RSSSourceEntity(0, item.title, item.url, "", ""))
            }
        }
    }

    suspend fun searchSource(text: String): MutableList<RSSSourceEntity> =
        withContext(Dispatchers.IO) {
            sourceDao.searchFeeds(text)
        }

    companion object {

        @Volatile
        private var instance: SourceRepository? = null

        fun getInstance(sourceDao: SourceDao) =
            instance ?: synchronized(this) {
                instance ?: SourceRepository(sourceDao).also { instance = it }
            }
    }
}