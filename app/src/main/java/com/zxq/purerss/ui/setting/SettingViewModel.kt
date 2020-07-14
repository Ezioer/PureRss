package com.zxq.purerss.ui.setting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zxq.purerss.data.RssFeedRepository
import com.zxq.purerss.data.entity.RssItem
import com.zxq.purerss.data.entity.RssItemInfo
import com.zxq.purerss.data.entity.table.RSSFeedEntity
import kotlinx.coroutines.launch

/**
 *  created by xiaoqing.zhou
 *  on 2020/6/28
 *  fun
 */
class SettingViewModel(private val repository: RssFeedRepository) : ViewModel() {

    val list = MutableLiveData<MutableList<RSSFeedEntity>>()

    fun getAllFeeds() {
        launch({
            val result = repository.getRssListFromDbX(1)
            list.value = result
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