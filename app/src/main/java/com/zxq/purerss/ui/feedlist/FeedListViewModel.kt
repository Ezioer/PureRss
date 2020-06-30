package com.zxq.purerss.ui.feedlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zxq.purerss.data.RssFeedRepository
import com.zxq.purerss.data.entity.RssItem
import com.zxq.purerss.data.entity.table.RSSFeedEntity
import com.zxq.purerss.utils.RssFeed_SAXParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 *  created by xiaoqing.zhou
 *  on 2020/6/28
 *  fun
 */
class FeedListViewModel(private val repository: RssFeedRepository) : ViewModel() {

    val feedsList = MutableLiveData<MutableList<RssItem>>()

    fun collectItem(item: RssItem){
        launch({
            val result = repository.collectItem(item)
        },{

        })
    }

    fun later(item: RssItem){
        launch({
            val result = repository.laterItem(item)
        },{

        })
    }

    fun readed(item: RssItem){
        launch({
            val result = repository.readedItem(item)
        },{

        })
    }

    fun getFeedsList(url: String, id: Long) {
        launch({
            val list = repository.getRssItemFromDB(id)
            if (list.isNullOrEmpty()) {
                val result = withContext(Dispatchers.IO) {
                    RssFeed_SAXParser().getFeed(url)
                }
                repository.saveContent2DB(result, id)
                feedsList.value = result.items
            } else {
                val resultList = mutableListOf<RssItem>()
                for (item in list) {
                    resultList.add(
                        RssItem(
                            item.itemTitle,
                            item.itemLink,
                            item.itemDesc,
                            item.itemDate,
                            item.itemAuthor,
                            item.itemPic
                        )
                    )
                }
                feedsList.value = resultList
            }
        }, {

        })
    }

    private fun launch(block: suspend () -> Unit, error: suspend (Throwable) -> Unit) =
        viewModelScope.launch {
            try {
                block()
            } catch (e: Throwable) {
                error(e)
            }
        }
}