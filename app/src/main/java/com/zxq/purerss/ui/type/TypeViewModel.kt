package com.zxq.purerss.ui.type

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zxq.purerss.data.Constant
import com.zxq.purerss.data.RssFeedRepository
import com.zxq.purerss.data.entity.table.RSSItemEntity
import kotlinx.coroutines.launch

/**
 *  created by xiaoqing.zhou
 *  on 2020/6/28
 *  fun
 */
class TypeViewModel(private val repository: RssFeedRepository): ViewModel() {

    val feedsList =MutableLiveData<MutableList<RSSItemEntity>>()
    val itemList = MutableLiveData<MutableList<RSSItemEntity>>()
    val status = MutableLiveData<Int>()
    fun getFeedsList(type: Int){
        launch({
            val result = repository.getRssListFromDb(type)
            if (result.isNullOrEmpty()) {
                status.value = Constant.EMPTY
            } else {
                feedsList.value = result
            }
        },{
            status.value = Constant.ERROR
        })
    }

    fun searchItem(key: String,type: Int){
        launch({
            val result = repository.searchItem(key,type)
            itemList.value = result
        },{

        })
    }

    fun removeItem(id: Long,type: Int){
        launch({
            val result = repository.removeItem(id,type)
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