package com.zxq.purerss.ui.wxboom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.*
import com.zxq.purerss.R

class BoomShitView @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null, def: Int = 0
) : View(context, attributeSet, def) {

    private var mRect: Rect? = null
    private var mBitmap: Bitmap? = null
    private var paint: Paint? = null

    init {
        mBitmap = BitmapFactory.decodeResource(resources, R.drawable.shit)
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        for (i in 0 until 6) {
            mRect = Rect()
            mRect?.let {
                it.left = i * 50 + 150
                it.top = 80 + (Math.random() * 400).toInt() - 200 / 4
                it.right = it.left + 100
                it.bottom = it.top + 100
            }
            var matrix = Matrix()
            matrix.postScale(i.toFloat() + 4, i.toFloat() + 4)
            matrix.postTranslate(
                -(mBitmap!!.width / 2).toFloat(),
                -(mBitmap!!.height) / 2.toFloat()
            )
            matrix.postRotate((Math.random() * 70).toFloat())
            matrix.postTranslate(-mRect!!.top.toFloat(), mRect!!.left.toFloat())
            var bitmap1 = Bitmap.createBitmap(
                mBitmap!!,
                0,
                0,
                mBitmap!!.width,
                mBitmap!!.height,
                matrix,
                true
            )
            canvas?.drawBitmap(bitmap1, null, mRect!!, paint)
            matrix.reset()
        }
    }
}