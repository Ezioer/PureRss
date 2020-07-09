package com.zxq.purerss.ui.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zxq.purerss.data.RssFeedRepository
import com.zxq.purerss.data.entity.SourceRepository

/**
 *  created by xiaoqing.zhou
 *  on 2020/6/28
 *  fun
 */
class AddRssModelFactory(
    private val repository: RssFeedRepository,
    private val sourceRepository: SourceRepository
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AddRssViewModel(repository, sourceRepository) as T
    }
}