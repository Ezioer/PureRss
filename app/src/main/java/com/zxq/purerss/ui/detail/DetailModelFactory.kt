package com.zxq.purerss.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zxq.purerss.data.RssFeedRepository
/**
 *  created by xiaoqing.zhou
 *  on 2020/6/28
 *  fun
 */
class DetailModelFactory(private val repository : RssFeedRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DetailViewModel(repository) as T
    }
}