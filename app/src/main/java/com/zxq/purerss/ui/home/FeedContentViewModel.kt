package com.zxq.purerss.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zxq.purerss.data.RssFeedRepository
import com.zxq.purerss.data.entity.RssFeed
import com.zxq.purerss.data.entity.table.RSSFeedEntity
import com.zxq.purerss.utils.RssFeed_SAXParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 *  created by xiaoqing.zhou
 *  on 2020/5/14
 *  fun
 */
class FeedContentViewModel(private val repository: RssFeedRepository): ViewModel(){
    var feedData = MutableLiveData<RssFeed>()
    val feeds = MutableLiveData<List<RSSFeedEntity>>()
    fun getFeedList(url: String,id: Long){
        launch({
            val result = withContext(Dispatchers.IO) {
                RssFeed_SAXParser().getFeed(url)
            }
            repository.saveContent2DB(result,id)
            feedData.value = result
        }, {

        })
    }

    fun getAllFeeds(){
        launch({
            val result = repository.getRssListFromDb()
            feeds.value = result
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