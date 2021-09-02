package com.zxq.purerss.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class PathText @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null
    , def: Int = 0
) : View(context, attributeSet, def) {
    private var mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mRadius = 0
    private var mCenterX = 0
    private var mCenterY = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCenterX = width / 2
        mCenterY = height / 2
        mRadius = width / 4
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val mPath = Path()
        mPaint.setColor(Color.parseColor("#fcf0ce"))
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = 2f
        canvas?.drawCircle(mCenterX.toFloat(), mCenterY.toFloat(), mRadius.toFloat(), mPaint)

//        canvas?.translate(mCenterX.toFloat(),mCenterY.toFloat())
        mPaint.setColor(Color.RED)
        mPaint.style = Paint.Style.STROKE
        mPath.lineTo(mRadius * cos(15 * PI / 180).toFloat(), mRadius * sin(15 * PI / 180).toFloat())
        canvas?.drawPath(mPath, mPaint)
    }

}