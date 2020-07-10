package com.zxq.purerss.data

import com.zxq.purerss.data.dao.FeedDao
import com.zxq.purerss.data.dao.FolderDao
import com.zxq.purerss.data.dao.ItemDao
import com.zxq.purerss.data.entity.RssFeed
import com.zxq.purerss.data.entity.RssItemInfo
import com.zxq.purerss.data.entity.RssOpmlInfo
import com.zxq.purerss.data.entity.table.*
import com.zxq.purerss.utils.DateUtils
import com.zxq.purerss.utils.ReadOPML
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 *  created by xiaoqing.zhou
 *  on 2020/4/28
 *  fun
 */
class RssFeedRepository private constructor(
    private val feedDao: FeedDao,
    private val itemDao: ItemDao,
    private val folderDao: FolderDao
) {

    suspend fun insertEvent(list: List<RSSFeedEntity>) = withContext(Dispatchers.IO) {
        for (item in list) {
            if (feedDao.isFeedIsExist(item.feedTitle) == null) {
                feedDao.insertOneFeed(item)
            }
        }
    }

    suspend fun insertEvent(item: RSSFeedEntity): Boolean = withContext(Dispatchers.IO) {
        if (feedDao.isFeedIsExist(item.feedTitle) == null) {
            feedDao.insertOneFeed(item)
            true
        } else {
            false
        }
    }

    suspend fun insertOpml(list: MutableList<RssOpmlInfo>?) = withContext(Dispatchers.IO) {
        if (!list.isNullOrEmpty()) {
            for (item in list) {
                if (feedDao.isFeedIsExist(item.title) == null) {
                    feedDao.insertOneFeed(RSSFeedEntity(0, item.title, item.url, "", "", 0))
                }
            }
        }
    }

    suspend fun removeItem(id: Long, type: Int) = withContext(Dispatchers.IO) {
        if (type == 1) {
            itemDao.removeReaded(id)
            itemDao.updateReadedState(id, false)
        } else if (type == 2) {
            itemDao.removeCollect(id)
        } else {
            itemDao.removeLater(id)
        }
    }

    suspend fun searchItem(key: String, type: Int): MutableList<RSSItemEntity> =
        withContext(Dispatchers.IO) {
            val result = mutableListOf<RSSItemEntity>()
            if (type == 1) {
                val list = itemDao.searchReaded(key)
                for (item in list) {
                    result.add(
                        RSSItemEntity(
                            item.itemId,
                            item.itemTitle,
                            item.itemLink,
                            item.itemDesc,
                            item.itemAuthor,
                            item.itemDate,
                            item.itemPic,
                            item.itemFeed,
                            1,
                            item.feedTitle,
                            DateUtils.getCurrentSystemTime()
                        )
                    )
                }
            } else if (type == 2) {
                val list = itemDao.searchCollect(key)
                for (item in list) {
                    result.add(
                        RSSItemEntity(
                            item.itemId,
                            item.itemTitle,
                            item.itemLink,
                            item.itemDesc,
                            item.itemAuthor,
                            item.itemDate,
                            item.itemPic,
                            item.itemFeed,
                            item.itemRead,
                            item.feedTitle,
                            DateUtils.getCurrentSystemTime()
                        )
                    )
                }
            } else {
                val list = itemDao.searchLater(key)
                for (item in list) {
                    result.add(
                        RSSItemEntity(
                            item.itemId,
                            item.itemTitle,
                            item.itemLink,
                            item.itemDesc,
                            item.itemAuthor,
                            item.itemDate,
                            item.itemPic,
                            item.itemFeed,
                            item.itemRead,
                            item.feedTitle,
                            DateUtils.getCurrentSystemTime()
                        )
                    )
                }
            }
            result
        }

    suspend fun searchFeeds(key: String): MutableList<RSSFeedEntity> = withContext(Dispatchers.IO) {
        feedDao.searchFeeds(key)
    }

    suspend fun getRssListFromDbX(id: Long): MutableList<RSSFeedEntity> =
        withContext(Dispatchers.IO) {
            if (folderDao.folderExist("全部") == null) {
                folderDao.insertOneFolder(RSSFolderEntity(0, "全部"))
            }
            feedDao.getFeedsFromDb(id)
        }

    suspend fun insertFolder(title: String) = withContext(Dispatchers.IO) {
        if (folderDao.folderExist(title) == null) {
            folderDao.insertOneFolder(RSSFolderEntity(0, title))
        }
    }

    suspend fun getFolderFromDb(): MutableList<RSSFolderEntity> = withContext(Dispatchers.IO) {
        folderDao.selectFolder()
    }

    suspend fun getRssListFromDb(type: Int): MutableList<RSSItemEntity> =
        withContext(Dispatchers.IO) {
            val result = mutableListOf<RSSItemEntity>()
            if (type == 1) {
                var list = itemDao.selectAllReaded()
                for (item in list) {
                    result.add(
                        RSSItemEntity(
                            item.itemId,
                            item.itemTitle,
                            item.itemLink,
                            item.itemDesc,
                            item.itemAuthor,
                            item.itemDate,
                            item.itemPic,
                            item.itemFeed,
                            1,
                            item.feedTitle,
                            DateUtils.getCurrentSystemTime()
                        )
                    )
                }
            } else if (type == 2) {
                var list = itemDao.selectAllCollect()
                for (item in list) {
                    result.add(
                        RSSItemEntity(
                            item.itemId,
                            item.itemTitle,
                            item.itemLink,
                            item.itemDesc,
                            item.itemAuthor,
                            item.itemDate,
                            item.itemPic,
                            item.itemFeed,
                            item.itemRead,
                            item.feedTitle,
                            DateUtils.getCurrentSystemTime()
                        )
                    )
                }
            } else {
                var list = itemDao.selectAllLater()
                for (item in list) {
                    result.add(
                        RSSItemEntity(
                            item.itemId,
                            item.itemTitle,
                            item.itemLink,
                            item.itemDesc,
                            item.itemAuthor,
                            item.itemDate,
                            item.itemPic,
                            item.itemFeed,
                            item.itemRead,
                            item.feedTitle,
                            DateUtils.getCurrentSystemTime()
                        )
                    )
                }
            }
            result
        }

    suspend fun getRssItemFromDB(id: Long): MutableList<RSSItemEntity> =
        withContext(Dispatchers.IO) {
            itemDao.selectById(id)
        }

    suspend fun collectItem(item: RSSItemEntity): Int = withContext(Dispatchers.IO) {
        if (itemDao.collectIsExist(item.itemTitle) == null) {
            itemDao.insertCollect(
                RSSCollectEntity(
                    0,
                    item.itemTitle,
                    item.itemLink,
                    item.itemDesc,
                    item.itemAuthor,
                    item.itemDate,
                    item.itemPic,
                    0L,
                    item.itemRead ?: 0,
                    ""
                )
            )
            1
        } else {
            0
        }

    }

    suspend fun laterItem(item: RSSItemEntity): Int = withContext(Dispatchers.IO) {
        if (itemDao.laterIsExist(item.itemTitle) == null) {
            itemDao.insertLater(
                RSSLaterEntity(
                    0,
                    item.itemTitle,
                    item.itemLink,
                    item.itemDesc,
                    item.itemAuthor,
                    item.itemDate,
                    item.itemPic,
                    0L,
                    item.itemRead ?: 0,
                    ""
                )
            )
            1
        } else {
            0
        }
    }

    suspend fun readedItem(item: RssItemInfo) = withContext(Dispatchers.IO) {
        if (itemDao.readedIsExist(item.title) == null) {
            itemDao.updateReadedState(item.title, 1)
            itemDao.insertReaded(
                RSSReadedEntity(
                    0,
                    item.title,
                    item.link,
                    item.description,
                    item.author,
                    item.pubdate,
                    item.pic,
                    0L,
                    ""
                )
            )
        }
    }

    suspend fun deleteReaded(title: String) = withContext(Dispatchers.IO) {
        if (itemDao.readedIsExist(title) != null) {
            itemDao.updateReadedState(title, 0)
            itemDao.removeReaded(title)
        }
    }

    suspend fun readOpml(filepath: String): MutableList<RssOpmlInfo>? =
        withContext(Dispatchers.IO) {
            ReadOPML.read(filepath)
        }


    suspend fun deleteFeed(item: RSSFeedEntity) = withContext(Dispatchers.IO) {
        feedDao.deleteFeed(item.feedId)
        itemDao.deleteById(item.feedId)
    }

    suspend fun saveContent2DB(
        feed: RssFeed,
        id: Long,
        list: MutableList<RSSItemEntity>
    ): MutableList<RSSItemEntity> = withContext(Dispatchers.IO) {
        val feedTitle = feed.title
        for (item in feed.items.reversed()) {
            if (itemDao.isExits(item.title) == null) {
                val info = RSSItemEntity(
                    0,
                    item.title,
                    item.link,
                    item.description,
                    item.author,
                    item.pubdate,
                    item.albumPic,
                    id,
                    0,
                    feedTitle,
                    DateUtils.getCurrentSystemTime()
                )
                list.add(0, info)
                itemDao.insertOneContent(info)
            }
        }
        list
    }

    companion object {

        @Volatile
        private var instance: RssFeedRepository? = null

        fun getInstance(feedDao: FeedDao, itemDao: ItemDao, folderDao: FolderDao) =
            instance ?: synchronized(this) {
                instance ?: RssFeedRepository(feedDao, itemDao, folderDao).also { instance = it }
            }
    }
}