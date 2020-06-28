package com.zxq.purerss.ui.type

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
class TypeViewModel(private val repository: RssFeedRepository): ViewModel() {

    val feedsList =MutableLiveData<MutableList<RssItem>>()

    fun getFeedsList(url: String,id:Long){
        launch({
            val result = withContext(Dispatchers.IO) {
                RssFeed_SAXParser().getFeed(url)
            }
            repository.saveContent2DB(result,id)
            feedsList.value = result.items
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