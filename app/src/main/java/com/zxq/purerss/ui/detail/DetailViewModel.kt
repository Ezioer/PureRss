package com.zxq.purerss.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zxq.purerss.data.RssFeedRepository
import com.zxq.purerss.data.entity.RssItem
import com.zxq.purerss.data.entity.RssItemInfo
import kotlinx.coroutines.launch
/**
 *  created by xiaoqing.zhou
 *  on 2020/6/28
 *  fun
 */
class DetailViewModel(private val repository: RssFeedRepository) : ViewModel() {

    val collectResult = MutableLiveData<Int>()

    fun collectItem(item: RssItemInfo) {
        launch({
            val result = repository.collectItem(
                RssItem(
                    item.title,
                    item.link,
                    item.description,
                    item.pubdate,
                    item.author,
                    item.pic
                )
            )
            collectResult.value = result
        }, {

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