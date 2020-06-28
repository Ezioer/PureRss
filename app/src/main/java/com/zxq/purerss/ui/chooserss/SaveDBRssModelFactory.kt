package com.zxq.purerss.ui.chooserss

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zxq.purerss.data.RssFeedRepository

/**
 *  created by xiaoqing.zhou
 *  on 2020/4/29
 *  fun
 */
class SaveDBRssModelFactory(private val repository : RssFeedRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SaveDBRssViewModel(repository) as T
    }
}