package com.zxq.purerss.utils

import android.os.Parcelable
import com.tencent.mmkv.MMKV

class MmkvUtils {
    private var kv: MMKV? = null

    private constructor() {
        kv = MMKV.defaultMMKV()
    }

    companion object {
        @Volatile
        private var instance: MmkvUtils? = null
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: MmkvUtils().also { instance = it }
            }
    }

    fun putValue(key: String, value: Any) {
        when (value) {
            is Long -> kv?.encode(key, value)
            is String -> kv?.encode(key, value)
            is Boolean -> kv?.encode(key, value)
            is Int -> kv?.encode(key, value)
            is Float -> kv?.encode(key, value)
            is Parcelable -> kv?.encode(key, value)
        }
    }

    fun <T> getValue(key: String, type: T): T {
        var result = when (type) {
            is Long -> kv?.decodeLong(key)
            is String -> kv?.decodeString(key)
            is Boolean -> kv?.decodeBool(key)
            is Int -> kv?.decodeInt(key)
//            is Parcelable -> kv?.decodeParcelable(key,type.javaClass)
            else -> kv?.decodeFloat(key)
        }
        return result as T
    }
}