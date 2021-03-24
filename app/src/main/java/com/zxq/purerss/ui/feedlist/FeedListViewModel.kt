package com.zxq.purerss.ui.feedlist

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zxq.purerss.App
import com.zxq.purerss.data.Constant
import com.zxq.purerss.data.RssFeedRepository
import com.zxq.purerss.data.entity.table.RSSItemEntity
import com.zxq.purerss.utils.RssFeed_SAXParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 *  created by xiaoqing.zhou
 *  on 2020/6/28
 *  fun
 */
class FeedListViewModel(private val repository: RssFeedRepository) : ViewModel() {

    val feedsList = MutableLiveData<MutableList<RSSItemEntity>>()
    val collectResult = MutableLiveData<Int>()
    val laterResult = MutableLiveData<Int>()
    val status = MutableLiveData<Int>()

    val userLiveData: MutableLiveData<String> = MutableLiveData()
    val userName: MutableLiveData<String> = Transformations.map(userLiveData) { it ->
        "${it}ccc"
    } as MutableLiveData<String>

    fun collectItem(item: RSSItemEntity) {
        launch({
            val result = repository.collectItem(item)
            collectResult.value = result
        }, {

        })
    }

    fun later(item: RSSItemEntity) {
        launch({
            val result = repository.laterItem(item)
            laterResult.value = result
        }, {

        })
    }

    fun getTest() {
        //开启一个协程,运行在主线程
        viewModelScope.launch(Dispatchers.Main) {
            //做耗时操作切到子线程
            val resulr = repository.getRssItemFromDB(1)
            Toast.makeText(App.instance, "hello,world", Toast.LENGTH_SHORT).show()
            var i = withContext(Dispatchers.IO) {
                7
            }
//            launch(Dispatchers.Main) {
            //更新ui切回主线程
//            }
        }
    }

    fun getFeedsList(url: String, id: Long, isRefresh: Boolean) {
        //默认是主线程，可以指定为io线程
        viewModelScope.launch(Dispatchers.Main) {
            //launch或者async都是开启一个协程，切换线程
            val result = async { repository.getRssItemFromDB(id) }
            //开启一个协程后都是异步操作，后面的操作会马上执行
            val data = async { repository.getRssItemFromDB(id) }
            //async可以异步请求最后合并数据
            val info = result.await() + data.await()
            Toast.makeText(
                App.instance,
                "currentthrea=" + Thread.currentThread(),
                Toast.LENGTH_LONG
            ).show()
        }
        return
        launch({
            createFlow()
                //发射数据的线程切为io线程
                .flowOn(Dispatchers.IO).catch {
                    //捕获异常
                }.onCompletion {
                    //完成
                }.collect {
                    //消费的线程取决与启动协程的调度器，触发到collect方法时才会开始发射数据，
                    // 每个数据都是经过 emit、filter 、map和 collect 这一套完整的处理流程后，下个数据才会开始处理，
                    // 而不是所有的数据都先统一 emit，完了再统一 filter，接着 map，最后再 collect
                    /*lifecycleScope.launch {
                        // 1. 生成一个 Channel
                        val channel = Channel<Int>()

                        // 2. Channel 发送数据
                        launch {
                            for(i in 1..5){
                                delay(200)
                                channel.send(i * i)
                            }
                            channel.close()
                        }

                        // 3. Channel 接收数据
                        launch {
                            for( y in channel)
                                Log.e(TAG, "get $y")
                        }
                    }*/
                }

            val list = repository.getRssItemFromDB(id)
            if (list.isNullOrEmpty() || isRefresh) {
                val result = withContext(Dispatchers.IO) {
                    RssFeed_SAXParser().getFeed(url)
                }
                val tempList = repository.saveContent2DB(result, id, list)
                if (tempList.isNullOrEmpty()) {
                    status.value = Constant.EMPTY
                } else {
                    feedsList.value = tempList
                }
            } else {
                feedsList.value = list
            }
        }, {
            status.value = Constant.ERROR
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

    private fun launch11(block: (Int) -> String) {
        viewModelScope.launch {
            try {
                var result = block(1)
                var i = 9
            } catch (e: Throwable) {
                error(e)
            }
        }
    }

    fun getTest2() {
        launch11(fun(par: Int): String {
            val i = 9
            return par.toString()
        })
    }

    suspend fun getData() {
        val list = repository.getRssItemFromDB(1)
    }


    private fun getTest1() {
        launch({
            viewModelScope.launch {
                val i = withContext(Dispatchers.IO) {

                }
            }
        }, {

        })
    }


    fun createFlow(): Flow<Int> = flow {
        for (i in 1..10)
        //发射数据，用flowon可以制定发射数据的线程,也可以直接用 (1..10).asFlow()创建发射对象
            emit(i)
    }
}