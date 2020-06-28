package com.zxq.livedatabus

import androidx.lifecycle.BusLiveData
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

/**
 *  created by xiaoqing.zhou
 *  on 2020/5/14
 *  fun
 */
internal open class BaseBusObserverWrapper<T>(
    private val mObserver: Observer<in T>,
    private val mBusLiveData: BusLiveData<T>
) : Observer<T> {
    private var mLastVersion = mBusLiveData.version
    private val TAG = "BaseBusObserverWrapper"
    override fun onChanged(t: T) {
        if (mLastVersion >= mBusLiveData.version) {
            return
        }
        try {
            mObserver.onChanged(t)
        } catch (e: Exception) {

        }
    }

    open fun isAttachedTo(owner: LifecycleOwner) = false
}