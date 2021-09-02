package com.zxq.purerss.utils

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

class ConcurrentHashSet<T> : AbstractSet<T>() {
    private var map: ConcurrentHashMap<T, Any> = ConcurrentHashMap<T, Any>()
    private var PRESENT = any()
    private var mCount = AtomicInteger()
    override val size: Int
        get() = mCount.get()

    override fun iterator(): Iterator<T> {
        return map.keys().iterator()
    }

    fun add(t: T): Boolean {
        mCount.incrementAndGet()
        return map.put(t, PRESENT) == null
    }

    fun remove(o: Any): Boolean {
        mCount.decrementAndGet()
        return true
//        return map.remove(o) == PRESENT
    }
}