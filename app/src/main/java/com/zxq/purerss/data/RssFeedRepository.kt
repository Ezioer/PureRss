package com.zxq.purerss.data

import com.zxq.purerss.data.dao.FeedDao
import com.zxq.purerss.data.dao.ItemDao
import com.zxq.purerss.data.entity.RssFeed
import com.zxq.purerss.data.entity.table.RSSFeedEntity
import com.zxq.purerss.data.entity.table.RSSItemEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 *  created by xiaoqing.zhou
 *  on 2020/4/28
 *  fun
 */
class RssFeedRepository private constructor(private val feedDao: FeedDao,private val itemDao: ItemDao) {

    suspend fun insertEvent(list: List<RSSFeedEntity>) = withContext(Dispatchers.IO) {
        for (item in list){
            if (feedDao.isFeedIsExist(item.feedTitle) ==null){
                feedDao.insertOneFeed(item)
            }
        }
    }

    suspend fun getRssListFromDb(): MutableList<RSSFeedEntity> = withContext(Dispatchers.IO){
        feedDao.getFeedsFromDb()
    }

    suspend fun saveContent2DB(feed: RssFeed,id: Long) = withContext(Dispatchers.IO){
        val feedTitle = feed.title
        for (item in feed.items){
            if (itemDao.isExits(item.link) != null){
                itemDao.insertOneContent(RSSItemEntity(item.title,item.link,item.description,item.author,item.pubdate,item.albumPic,id,feedTitle))
            }
        }
    }

    companion object {

        @Volatile
        private var instance: RssFeedRepository? = null

        fun getInstance(feedDao: FeedDao,itemDao: ItemDao) =
            instance ?: synchronized(this) {
                instance ?: RssFeedRepository(feedDao,itemDao).also { instance = it }
            }
    }
}