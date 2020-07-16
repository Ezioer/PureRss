package com.zxq.purerss.ui.setting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zxq.purerss.data.RssFeedRepository
import com.zxq.purerss.data.entity.RssItem
import com.zxq.purerss.data.entity.RssItemInfo
import com.zxq.purerss.data.entity.RssOpmlInfo
import com.zxq.purerss.data.entity.table.RSSFeedEntity
import com.zxq.purerss.data.entity.table.RSSItemEntity
import com.zxq.purerss.utils.ReadOPML
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 *  created by xiaoqing.zhou
 *  on 2020/6/28
 *  fun
 */
class SettingViewModel(private val repository: RssFeedRepository) : ViewModel() {

    val list = MutableLiveData<MutableList<RSSFeedEntity>>()
    val widgetList = MutableLiveData<MutableList<RSSFeedEntity>>()
    val success = MutableLiveData<Boolean>()
    fun getAllFeeds() {
        launch({
            val result = repository.getRssListFromDbX(1)
            list.value = result
        }, {

        })
    }

    fun getWidgetContent(id: Long) {
        launch({
            val result = repository.getRssListFromDbX(id)
            widgetList.value = result
        }, {

        })
    }

    fun exportOpml(path: String, list: MutableList<RSSFeedEntity>) {
        viewModelScope.launch {
            val result = writeData(path, list)
            success.value = result
        }
    }

    suspend fun writeData(path: String, list: MutableList<RSSFeedEntity>) =
        withContext(Dispatchers.IO) {
            ReadOPML.write(path, list)
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