package com.zxq.purerss.data

import com.zxq.purerss.data.dao.FeedDao
import com.zxq.purerss.data.dao.ItemDao
import com.zxq.purerss.data.entity.RssFeed
import com.zxq.purerss.data.entity.RssItem
import com.zxq.purerss.data.entity.table.*
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

    suspend fun removeItem(id: Long,type: Int) = withContext(Dispatchers.IO){
        if (type == 1){
            itemDao.removeReaded(id)
        } else if (type == 2){
            itemDao.removeCollect(id)
        } else {
            itemDao.removeReaded(id)
        }
    }

    suspend fun searchItem(key: String,type: Int): MutableList<RSSItemEntity> = withContext(Dispatchers.IO){
        val result = mutableListOf<RSSItemEntity>()
        if (type == 1){
            val list = itemDao.searchReaded(key)
            for (item in list){
                result.add(RSSItemEntity(item.itemTitle,item.itemLink,item.itemDesc,item.itemAuthor,item.itemDate,item.itemPic,item.itemFeed,item.feedTitle))
            }
        } else if (type == 2){
            val list = itemDao.searchCollect(key)
            for (item in list){
                result.add(RSSItemEntity(item.itemTitle,item.itemLink,item.itemDesc,item.itemAuthor,item.itemDate,item.itemPic,item.itemFeed,item.feedTitle))
            }
        } else {
            val list = itemDao.searchLater(key)
            for (item in list){
                result.add(RSSItemEntity(item.itemTitle,item.itemLink,item.itemDesc,item.itemAuthor,item.itemDate,item.itemPic,item.itemFeed,item.feedTitle))
            }
        }
        result
    }

    suspend fun searchFeeds(key: String): MutableList<RSSFeedEntity> = withContext(Dispatchers.IO){
        feedDao.searchFeeds(key)
    }

    suspend fun getRssListFromDb(): MutableList<RSSFeedEntity> = withContext(Dispatchers.IO){
        feedDao.getFeedsFromDb()
    }

    suspend fun getRssListFromDb(type: Int): MutableList<RSSItemEntity> = withContext(Dispatchers.IO){
        val result = mutableListOf<RSSItemEntity>()
        if (type == 1){
           var list =  itemDao.selectAllReaded()
            for (item in list){
                result.add(RSSItemEntity(item.itemTitle,item.itemLink,item.itemDesc,item.itemAuthor,item.itemDate,item.itemPic,item.itemFeed,item.feedTitle))
            }
        } else if (type == 2){
            var list= itemDao.selectAllCollect()
            for (item in list){
                result.add(RSSItemEntity(item.itemTitle,item.itemLink,item.itemDesc,item.itemAuthor,item.itemDate,item.itemPic,item.itemFeed,item.feedTitle))
            }
        } else {
           var list = itemDao.selectAllLater()
            for (item in list){
                result.add(RSSItemEntity(item.itemTitle,item.itemLink,item.itemDesc,item.itemAuthor,item.itemDate,item.itemPic,item.itemFeed,item.feedTitle))
            }
        }
        result
    }

    suspend fun getRssItemFromDB(id: Long): MutableList<RSSItemEntity> = withContext(Dispatchers.IO){
        itemDao.selectById(id)
    }

    suspend fun collectItem(item: RssItem) = withContext(Dispatchers.IO){
        itemDao.insertCollect(RSSCollectEntity(item.title,item.link,item.description,item.author,item.pubdate,item.albumPic,0L,""))
    }

    suspend fun laterItem(item: RssItem) = withContext(Dispatchers.IO){
        if (itemDao.laterIsExist(item.title) == null) {
            itemDao.insertLater(
                RSSLaterEntity(
                    item.title,
                    item.link,
                    item.description,
                    item.author,
                    item.pubdate,
                    item.albumPic,
                    0L,
                    ""
                )
            )
        }
    }

    suspend fun readedItem(item: RssItem) = withContext(Dispatchers.IO) {
        if (itemDao.readedIsExist(item.title) == null) {
            itemDao.insertReaded(
                RSSReadedEntity(
                    item.title,
                    item.link,
                    item.description,
                    item.author,
                    item.pubdate,
                    item.albumPic,
                    0L,
                    ""
                )
            )
        }
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