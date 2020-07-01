package com.zxq.purerss.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zxq.purerss.data.RssFeedRepository
import com.zxq.purerss.data.entity.RssItem
import com.zxq.purerss.data.entity.RssItemInfo
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
class DetailViewModel(private val repository: RssFeedRepository) : ViewModel() {

    val feedsList = MutableLiveData<MutableList<RssItem>>()

    fun collectItem(item: RssItem){
        launch({
            val result = repository.collectItem(item)
        },{

        })
    }

    fun readed(item: RssItemInfo){
        launch({
            val result = repository.readedItem(item)
        },{

        })
    }

    fun deleteReaded(item: RssItemInfo){
        launch({
            val result = repository.readedItem(item)
        },{

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