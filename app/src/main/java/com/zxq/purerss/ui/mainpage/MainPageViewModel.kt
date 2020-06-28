package com.zxq.purerss.ui.mainpage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zxq.purerss.data.RssFeedRepository
import com.zxq.purerss.data.entity.table.RSSFeedEntity
import kotlinx.coroutines.launch

/**
 *  created by xiaoqing.zhou
 *  on 2020/6/28
 *  fun
 */
class MainPageViewModel(private val repository: RssFeedRepository): ViewModel() {

    val feedsList =MutableLiveData<MutableList<RSSFeedEntity>>()

    fun getFeedsList(){
        launch({
            val result = repository.getRssListFromDb()
            feedsList.value = result
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