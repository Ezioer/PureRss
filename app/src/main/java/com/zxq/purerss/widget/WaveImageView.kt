package com.zxq.purerss.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.zxq.purerss.R

class WaveImageView @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null
    , def: Int = 0
) : View(context, attributeSet, def) {
    private var mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mCenterX = 0
    private var mCenterY = 0
    private var mRadius = 0
    var bitmap: Bitmap? = null

    init {
        bitmap = BitmapFactory.decodeResource(resources, R.drawable.spring)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCenterX = width / 2
        mCenterY = height / 2
        mRadius = width / 4
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var desBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        var mCanvas = Canvas(desBitmap)
        var bitmapShader = BitmapShader(bitmap!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        mPaint.setShader(bitmapShader)
        mCanvas?.drawRoundRect(0f, 0f, width.toFloat(), height.toFloat(), 80f, 80f, mPaint)
        canvas?.drawBitmap(desBitmap, 0f, 0f, mPaint)
    }
}