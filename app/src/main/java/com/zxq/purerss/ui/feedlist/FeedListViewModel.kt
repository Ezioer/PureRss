package com.zxq.purerss.ui.feedlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zxq.purerss.data.Constant
import com.zxq.purerss.data.RssFeedRepository
import com.zxq.purerss.data.entity.RssItem
import com.zxq.purerss.data.entity.table.RSSItemEntity
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

    val feedsList = MutableLiveData<MutableList<RSSItemEntity>>()
    val collectResult = MutableLiveData<Int>()
    val laterResult = MutableLiveData<Int>()
    val status = MutableLiveData<Int>()
    fun collectItem(item: RSSItemEntity) {
        launch({
            val result = repository.collectItem(item)
            collectResult.value = result
        }, {

        })
    }

    fun later(item: RSSItemEntity) {
        launch({
            val result = repository.laterItem(item)
            laterResult.value = result
        }, {

        })
    }

    fun getFeedsList(url: String, id: Long, isRefresh: Boolean) {
        launch({
            val list = repository.getRssItemFromDB(id)
            if (list.isNullOrEmpty() || isRefresh) {
                val result = withContext(Dispatchers.IO) {
                    RssFeed_SAXParser().getFeed(url)
                }
                val tempList = repository.saveContent2DB(result, id, list)
                if (tempList.isNullOrEmpty()) {
                    status.value = Constant.EMPTY
                } else {
                    feedsList.value = tempList
                }
            } else {
                feedsList.value = list
            }
        }, {
            status.value = Constant.ERROR
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