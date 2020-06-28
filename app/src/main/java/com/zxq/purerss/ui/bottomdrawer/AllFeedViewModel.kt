package com.zxq.purerss.ui.bottomdrawer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zxq.purerss.data.RssFeedRepository
import com.zxq.purerss.data.entity.table.RSSFeedEntity
import kotlinx.coroutines.launch

/**
 *  created by xiaoqing.zhou
 *  on 2020/5/14
 *  fun
 */
class AllFeedViewModel(private val repository: RssFeedRepository): ViewModel(){
    val feedList = MutableLiveData<List<RSSFeedEntity>>()
    fun getList(){
        launch({
            feedList.value = repository.getRssListFromDb()
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