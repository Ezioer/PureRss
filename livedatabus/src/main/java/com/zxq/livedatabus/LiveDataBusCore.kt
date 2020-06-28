package com.zxq.livedatabus

import androidx.lifecycle.BusLiveData

/**
 *  created by xiaoqing.zhou
 *  on 2020/5/14
 *  fun
 */
internal class LiveDataBusCore {
    companion object{

        @JvmStatic
        private val defaultBus = LiveDataBusCore()

        @JvmStatic
        fun getInstance() = defaultBus
    }

    internal val mBusMap : MutableMap<String, BusLiveData<*>> by lazy {
        mutableMapOf<String, BusLiveData<*>>()
    }

    fun <T> getChannel(key: String) : BusLiveData<T> {
        return mBusMap.getOrPut(key){
            BusLiveData<T>(key)
        } as BusLiveData<T>
    }
}