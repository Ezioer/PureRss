package com.zxq.purerss.ui.mainpage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zxq.purerss.data.RssFeedRepository
import com.zxq.purerss.data.entity.SourceRepository

/**
 *  created by xiaoqing.zhou
 *  on 2020/6/28
 *  fun
 */
class MainPageModelFactory(
    private val repository: RssFeedRepository,
    private val sourceRepository: SourceRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainPageViewModel(repository, sourceRepository) as T
    }
}