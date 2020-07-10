package com.zxq.purerss.ui.chooserss

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zxq.purerss.data.RssFeedRepository
import com.zxq.purerss.data.entity.RssFeed
import com.zxq.purerss.data.entity.table.RSSFeedEntity
import kotlinx.coroutines.launch

/**
 *  created by xiaoqing.zhou
 *  on 2020/5/14
 *  fun
 */
class SaveDBRssViewModel(private val repository: RssFeedRepository): ViewModel(){
    val insertComplete = MutableLiveData<Boolean>()

    fun insertRss(list: List<RSSFeedEntity>){
        launch({
            val result = repository.insertEvent(list)
            insertComplete.value = true
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