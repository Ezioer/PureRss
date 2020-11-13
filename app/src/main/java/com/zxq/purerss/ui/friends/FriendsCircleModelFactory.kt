package com.zxq.purerss.ui.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zxq.purerss.data.StatusRepository

/**
 *  created by xiaoqing.zhou
 *  on 2020/6/28
 *  fun
 */
class FriendsCircleModelFactory(private val repository: StatusRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FriendsCircleViewModel(repository) as T
    }
}