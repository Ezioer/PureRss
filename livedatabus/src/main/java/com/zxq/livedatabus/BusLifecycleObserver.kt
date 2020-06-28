package com.zxq.livedatabus

import androidx.lifecycle.*

/**
 *  created by xiaoqing.zhou
 *  on 2020/5/14
 *  fun
 */
internal class BusLifecycleObserver<T>(
    private val observer: Observer<in T>,
    private val owner: LifecycleOwner,
    private val liveData: BusLiveData<T>
) : BaseBusObserverWrapper<T>(observer,liveData),LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(){
        liveData.removeObserver(observer)
        owner.lifecycle.removeObserver(this)
    }
}