package com.zxq.purerss.ui.managefolder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zxq.purerss.data.FolderRepository
import com.zxq.purerss.data.RssFeedRepository

/**
 *  created by xiaoqing.zhou
 *  on 2020/6/28
 *  fun
 */
class ManageFolderModelFactory(private val repository: FolderRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ManageFolderViewModel(repository) as T
    }
}