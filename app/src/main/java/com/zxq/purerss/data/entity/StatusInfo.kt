package com.zxq.purerss.data.entity

import com.zxq.purerss.utils.DateUtils

data class StatusInfo(
    val id: Long = 0L,
    val name: String = "Ezio",
    val content: String = "",
    val time: Long = 0L,
    val like: Int = 0,
    val isHasPic: Int = 0,
    var pics: MutableList<ImageItemInfo>? = null
) {

    fun getDate(): String {
        return DateUtils.getFormatDate(time, DateUtils.YMD)
    }
}