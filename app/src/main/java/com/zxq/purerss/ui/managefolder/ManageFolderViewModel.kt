package com.zxq.purerss.ui.managefolder

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zxq.purerss.data.FolderRepository
import com.zxq.purerss.data.RssFeedRepository
import com.zxq.purerss.data.entity.RssItem
import com.zxq.purerss.data.entity.RssOpmlInfo
import com.zxq.purerss.data.entity.table.RSSFolderEntity
import com.zxq.purerss.utils.RssFeed_SAXParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 *  created by xiaoqing.zhou
 *  on 2020/6/28
 *  fun
 */
class ManageFolderViewModel(private val repository: FolderRepository) : ViewModel() {
    val folders = MutableLiveData<MutableList<RSSFolderEntity>>()
    val folder = MutableLiveData<Long>()
    fun getAllFolder() {
        launch({
            val result = repository.getFolderFromDb()
            folders.value = result
        }, {

        })
    }

    fun newFolder(title: String) {
        launch({
            val result = repository.insertFolder(title)
            if (result != null) {
                folder.value = result
            }
        }, {

        })
    }

    fun deleteFolder(id: Long) {
        launch({
            val result = repository.deleteFolder(id)
        }, {

        })
    }

    fun updateFolder(id: Long, title: String) {
        launch({
            val result = repository.updateFolder(id, title)
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