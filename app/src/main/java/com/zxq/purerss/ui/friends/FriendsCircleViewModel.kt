package com.zxq.purerss.ui.friends

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zxq.purerss.data.StatusRepository
import com.zxq.purerss.data.entity.ImageItemInfo
import com.zxq.purerss.data.entity.StatusInfo
import kotlinx.coroutines.launch

/**
 *  created by xiaoqing.zhou
 *  on 2020/6/28
 *  fun
 */
class FriendsCircleViewModel(private val repository: StatusRepository) : ViewModel() {

    val statusList = MutableLiveData<MutableList<StatusInfo>>()
    val sendStatus = MutableLiveData<Int>()
    fun insertOneStatus(content: String, pics: MutableList<ImageItemInfo>) {
        launch({
            val result = repository.insertStatus(content, pics)
            sendStatus.value = 1
        }, {

        })
    }

    fun insertOneStatus(statusInfo: StatusInfo) {
        launch({
            val result = repository.insertStatus(statusInfo)
            sendStatus.value = 1
        }, {

        })
    }

    fun getAllStatus() {
        launch({
            val result = repository.getStatusFromDb()
            statusList.value = result
        }, {

        })
    }

    fun deleteOneStatus(statusInfo: StatusInfo) {
        launch({
            val result = repository.deleteOne(statusInfo)
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