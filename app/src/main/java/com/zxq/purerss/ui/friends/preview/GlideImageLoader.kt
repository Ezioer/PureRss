package com.zxq.purerss.ui.friends.preview

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.previewlibrary.loader.IZoomMediaLoader
import com.previewlibrary.loader.MySimpleTarget
import com.zxq.purerss.R


class GlideImageLoader : IZoomMediaLoader {
    override fun displayGifImage(p0: Fragment, p1: String, p2: ImageView?, p3: MySimpleTarget) {

    }

    override fun clearMemory(c: Context) {
        Glide.get(c).clearMemory()
    }

    override fun displayImage(
        context: Fragment,
        path: String,
        imageView: ImageView,
        simpleTarget: MySimpleTarget
    ) {
        Glide.with(context).load(path)
            .error(R.drawable.default_two)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    simpleTarget.onLoadFailed(null)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    simpleTarget.onResourceReady()
                    return false
                }

            })
            .into(imageView)

    }

    override fun onStop(p0: Fragment) {
        Glide.with(p0).onStop()
    }

}