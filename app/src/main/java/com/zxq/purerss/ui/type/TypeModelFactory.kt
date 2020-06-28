package com.zxq.purerss.ui.type

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zxq.purerss.data.RssFeedRepository
/**
 *  created by xiaoqing.zhou
 *  on 2020/6/28
 *  fun
 */
class TypeModelFactory(private val repository : RssFeedRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TypeViewModel(repository) as T
    }
}