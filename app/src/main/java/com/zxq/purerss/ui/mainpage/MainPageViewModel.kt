package com.zxq.purerss.ui.mainpage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zxq.purerss.data.RssFeedRepository
import com.zxq.purerss.data.SourceRepository
import com.zxq.purerss.data.entity.table.RSSFeedEntity
import com.zxq.purerss.data.entity.table.RSSFolderEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 *  created by xiaoqing.zhou
 *  on 2020/6/28
 *  fun
 */
class MainPageViewModel(
    private val repository: RssFeedRepository,
    private val sourceRepository: SourceRepository
) : ViewModel() {

    val feedsList = MutableLiveData<MutableList<RSSFeedEntity>>()
    val searchFeedsList = MutableLiveData<MutableList<RSSFeedEntity>>()
    val folders = MutableLiveData<MutableList<RSSFolderEntity>>()
    val saveComplete = MutableLiveData<Int>()
    fun getFeedsList(id: Long) {
        launch({
            val result = repository.getRssListFromDbX(id)
            feedsList.value = result
        }, {

        })
    }

    fun getFeedsList(id: Long, sortId: Int) {
        launch({
            val result = repository.getRssListFromDbX(id, sortId)
            feedsList.value = result
        }, {

        })
    }

    fun getFolder() {
        launch({
            val result = repository.getFolderFromDb()
            folders.value = result
        }, {

        })
    }

    fun insertFolder(title: String) {
        launch({
            val result = repository.insertFolder(title)
        }, {

        })
    }

    fun deleteItem(item: RSSFeedEntity) {
        launch({
            val result = repository.deleteFeed(item)
        }, {

        })
    }

    fun serachFeeds(key: String) {
        launch({
            val result = repository.searchFeeds(key)
            searchFeedsList.value = result
        }, {

        })
    }

    fun insertSource() {
        launch({
            val result = sourceRepository.insertAllFromOpml()
        }, {

        })
    }

    fun updateFeeds(title: String, subTitle: String, link: String, parentId: Long, id: Long) {
        launch({
            val result = repository.updateFeed(title, subTitle, link, parentId, id)
            saveComplete.value = 1
        }, {

        })
    }

    fun updateFeeds(count: Int, id: Long) {
        launch({
            val result = repository.updateFeed(count, id)
            saveComplete.value = 1
        }, {

        })
    }

    private inline fun launch( crossinline block: suspend () -> Unit,crossinline error: suspend (Throwable) -> Unit) =
        viewModelScope.launch(Dispatchers.Main) {
            try {
                block()
            } catch (e: Throwable) {
                error(e)
            }
        }
}