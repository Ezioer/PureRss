package com.zxq.purerss.data.entity

import android.os.Parcel
import android.os.ParcelFileDescriptor
import android.os.Parcelable

/**
 *  created by xiaoqing.zhou
 *  on 2020/7/15
 *  fun
 */
class FilePathInfo(var filepath: String? = "", var des: ParcelFileDescriptor? = null) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        source.readFileDescriptor()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(filepath)
        writeParcelable(des, flags)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<FilePathInfo> = object : Parcelable.Creator<FilePathInfo> {
            override fun createFromParcel(source: Parcel): FilePathInfo = FilePathInfo(source)
            override fun newArray(size: Int): Array<FilePathInfo?> = arrayOfNulls(size)
        }
    }
}