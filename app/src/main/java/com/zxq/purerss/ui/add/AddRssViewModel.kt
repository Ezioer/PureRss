package com.zxq.purerss.ui.add

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zxq.purerss.data.RssFeedRepository
import com.zxq.purerss.data.entity.RssFeed
import com.zxq.purerss.data.entity.SourceRepository
import com.zxq.purerss.data.entity.table.RSSFeedEntity
import com.zxq.purerss.data.entity.table.RSSSourceEntity
import com.zxq.purerss.utils.RssFeed_SAXParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 *  created by xiaoqing.zhou
 *  on 2020/6/28
 *  fun
 */
class AddRssViewModel(
    private val repository: RssFeedRepository,
    private val sourceRepository: SourceRepository
) : ViewModel() {

    val feedsList = MutableLiveData<RssFeed>()
    val sources = MutableLiveData<MutableList<RSSSourceEntity>>()
    val noThingFound = MutableLiveData<Boolean>()
    val addComplete = MutableLiveData<Boolean>()
    fun getFeedsList(url: String) {
        launch({
            val result = withContext(Dispatchers.IO) {
                RssFeed_SAXParser().getFeed(url)
            }
            feedsList.value = result
        }, {
            noThingFound.value = false
        })
    }

    fun searchSource(text: String) {
        launch({
            val result = sourceRepository.searchSource(text)
            sources.value = result
        }, {
            noThingFound.value = false
        })
    }

    fun insertRss(list: RSSFeedEntity) {
        launch({
            val result = repository.insertEvent(list)
            addComplete.value = result
        }, {
            addComplete.value = false
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