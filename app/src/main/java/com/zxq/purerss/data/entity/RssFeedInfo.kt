package com.zxq.purerss.data.entity

import android.os.Parcel
import android.os.Parcelable

/**
 *  created by xiaoqing.zhou
 *  on 2020/6/22
 *  fun
 */
class RssFeedInfo(
    var title: String = "",
    var link: String = "",
    var description: String = "",
    var pubdate: String = "",
    var id: Long
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString()?:"",
        source.readString()?:"",
        source.readString()?:"",
        source.readString()?:"",
        source.readLong()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(title)
        writeString(link)
        writeString(description)
        writeString(pubdate)
        writeLong(id)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<RssFeedInfo> = object : Parcelable.Creator<RssFeedInfo> {
            override fun createFromParcel(source: Parcel): RssFeedInfo = RssFeedInfo(source)
            override fun newArray(size: Int): Array<RssFeedInfo?> = arrayOfNulls(size)
        }
    }
}