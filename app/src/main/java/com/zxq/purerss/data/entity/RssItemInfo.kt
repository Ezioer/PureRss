package com.zxq.purerss.data.entity

import android.os.Parcel
import android.os.Parcelable

/**
 *  created by xiaoqing.zhou
 *  on 2020/6/22
 *  fun
 */
class RssItemInfo(
    var title: String = "",
    var link: String = "",
    var description: String = "",
    var pubdate: String = "",
    var author: String = "",
    var feedId: Long,
    var feedTitle: String
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString()?:"",
        source.readString()?:"",
        source.readString()?:"",
        source.readString()?:"",
        source.readString()?:"",
        source.readLong(),
        source.readString()?:""
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(title)
        writeString(link)
        writeString(description)
        writeString(pubdate)
        writeString(author)
        writeLong(feedId)
        writeString(feedTitle)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<RssItemInfo> = object : Parcelable.Creator<RssItemInfo> {
            override fun createFromParcel(source: Parcel): RssItemInfo = RssItemInfo(source)
            override fun newArray(size: Int): Array<RssItemInfo?> = arrayOfNulls(size)
        }
    }
}