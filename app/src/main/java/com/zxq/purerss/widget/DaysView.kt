package com.zxq.purerss.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.zxq.purerss.utils.PixelUtil
import java.util.*

class DaysView @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null
    , def: Int = 0
) : View(context, attributeSet, def) {
    private var mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mCenterX = 0
    private var mCenterY = 0
    private var mRadius = 0

    init {
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCenterX = width / 2
        mCenterY = height / 2
        mRadius = width / 4
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mPaint.setColor(Color.parseColor("#deac98"))
        canvas?.drawRoundRect(0f, 0f, width.toFloat(), height.toFloat(), 80f, 80f, mPaint)
        //绘制星期
        canvas?.save()
        val calendar = Calendar.getInstance()
        val weeks = calendar.get(Calendar.DAY_OF_WEEK)
        var week = ""
        when (weeks) {
            1 -> {
                week = "Sun"
            }
            2 -> {
                week = "Mon"
            }
            3 -> {
                week = "Tue"
            }
            4 -> {
                week = "Wed"
            }
            5 -> {
                week = "Thu"
            }
            6 -> {
                week = "Fri"
            }
            7 -> {
                week = "Sat"
            }
            //绘制日期
        }
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val textDayLength: Float = mPaint.measureText(day.toString()) //获得字体长度
        val textWeekLength: Float = mPaint.measureText(week) //获得字体长度
        mPaint.color = Color.WHITE
        mPaint.textSize = PixelUtil.sp2px(this.context, 30f).toFloat()
        canvas?.drawText(week, mCenterX / 4f, mCenterY.toFloat() - mCenterY / 4, mPaint)
        mPaint.textSize = PixelUtil.sp2px(this.context, 60f).toFloat()
        canvas?.drawText("$day", mCenterX.toFloat(), mCenterY.toFloat() + mCenterY / 2, mPaint)
    }
}