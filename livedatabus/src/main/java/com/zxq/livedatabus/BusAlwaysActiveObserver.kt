package com.zxq.livedatabus

import androidx.lifecycle.BusLiveData
import androidx.lifecycle.Observer

/**
 *  created by xiaoqing.zhou
 *  on 2020/5/14
 *  fun
 */
internal class BusAlwaysActiveObserver<T>(private val mObserver: Observer<in T>, private val mBusLiveData: BusLiveData<T>)
    : BaseBusObserverWrapper<T>(mObserver, mBusLiveData)