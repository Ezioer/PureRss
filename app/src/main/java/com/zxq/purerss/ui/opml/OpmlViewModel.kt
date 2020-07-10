package com.zxq.purerss.ui.opml

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zxq.purerss.data.RssFeedRepository
import com.zxq.purerss.data.entity.RssItem
import com.zxq.purerss.data.entity.RssOpmlInfo
import com.zxq.purerss.utils.RssFeed_SAXParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 *  created by xiaoqing.zhou
 *  on 2020/6/28
 *  fun
 */
class OpmlViewModel(private val repository: RssFeedRepository) : ViewModel() {

    val opml = MutableLiveData<MutableList<RssOpmlInfo>>()

    fun read(filepath: String) {
        launch({
            val result = repository.readOpml(filepath)
            opml.value = result
        }, {

        })
    }

    fun insertFeed(list: MutableList<RssOpmlInfo>?) {
        launch({
            var result = repository.insertOpml(list)
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