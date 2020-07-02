package com.zxq.purerss.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LevelListDrawable
import android.text.Html.ImageGetter
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition


/**
 * created by xiaoqing.zhou
 * on 2020/6/16
 * fun
 */
open class MImageGetter(var container: TextView, var c: Context) : ImageGetter {
    override fun getDrawable(source: String): Drawable {
        var drawable = LevelListDrawable()
        Glide.with(c).asBitmap().load(source).into(object : SimpleTarget<Bitmap?>() {

            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
                if (resource != null && resource.width > 0 && resource.height > 0) {
                    var width = container.width
                    if (resource.width > width) {
                        val scale = (width / resource.width.toFloat())
                        val handleWidth = (resource.width * scale).toInt()
                        val handleHeight = (resource.height * scale).toInt()
                        val bitmapDrawable =
                            BitmapDrawable(resizeBitmap(resource, handleWidth, handleHeight))
                        drawable.addLevel(1, 1, bitmapDrawable)
                        drawable.setBounds(0, 0, handleWidth, handleHeight)
                        drawable.setLevel(1)
                    } else {
                        val bitmapDrawable = BitmapDrawable(resource)
                        drawable.addLevel(1, 1, bitmapDrawable)
                        drawable.setBounds(0, 0, (resource.width), (resource.height))
                        drawable.setLevel(1)
                    }
                    container.invalidate()
                    container.text = container.text
                }
            }
        })
        return drawable
    }

    open fun resizeBitmap(bitmap: Bitmap, w: Int, h: Int): Bitmap? {
        val width = bitmap.width
        val height = bitmap.height
        val scaleWidth = w.toFloat() / width
        val scaleHeight = h.toFloat() / height
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        return Bitmap.createBitmap(
            bitmap, 0, 0, width,
            height, matrix, true
        )
    }
}