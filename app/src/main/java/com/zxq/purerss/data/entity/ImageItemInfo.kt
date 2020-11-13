package com.zxq.purerss.data.entity

import android.graphics.Rect
import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import com.previewlibrary.enitity.IThumbViewInfo

class ImageItemInfo(
    var cropUrl: String? = "",
    var uri: Uri? = null,
    var filePath: String? = "",
    private var mBounds: Rect? = null,
    private var videoUrl: String? = ""
) : IThumbViewInfo {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readParcelable(Uri::class.java.classLoader),
        parcel.readString(),
        parcel.readParcelable(Rect::class.java.classLoader),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(cropUrl)
        parcel.writeParcelable(uri, flags)
        parcel.writeString(filePath)
        parcel.writeParcelable(mBounds, flags)
        parcel.writeString(videoUrl)
    }


    override fun getUrl(): String {
        return uri.toString()
    }

    override fun getVideoUrl(): String? {
        return videoUrl
    }

    fun setVideoUrl(videoUrl: String?) {
        this.videoUrl = videoUrl
    }

    fun setBounds(bounds: Rect) {
        mBounds = bounds
    }

    override fun getBounds(): Rect? {
        return mBounds;
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ImageItemInfo> {
        override fun createFromParcel(parcel: Parcel): ImageItemInfo {
            return ImageItemInfo(parcel)
        }

        override fun newArray(size: Int): Array<ImageItemInfo?> {
            return arrayOfNulls(size)
        }
    }
}