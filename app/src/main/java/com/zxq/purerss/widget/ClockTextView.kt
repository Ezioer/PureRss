package com.zxq.purerss.widget

import android.content.Context
import android.graphics.*
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.View
import com.zxq.purerss.utils.PixelUtil
import java.lang.ref.WeakReference
import java.util.*

class ClockTextView @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null
    , def: Int = 0
) : View(context, attributeSet, def) {
    private var mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mCenterX = 0
    private var mCenterY = 0
    private var mRadius = 0
    private var mTimeHandle: TimeHandle

    init {
        mTimeHandle = TimeHandle(this)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCenterX = width / 2
        mCenterY = height / 2
        mRadius = width / 4
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //绘制表盘
        mPaint.setColor(Color.parseColor("#deac98"))
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = 2f
        canvas?.drawCircle(mCenterX.toFloat(), mCenterY.toFloat(), mRadius.toFloat(), mPaint)

        canvas?.save()
        //绘制12，3，6，9点刻度大文字
        mPaint.textSize = PixelUtil.sp2px(this.context, 44f).toFloat()
        mPaint.color = Color.parseColor("#f7e1d7")
        mPaint.isFakeBoldText = true
        mPaint.setTypeface(Typeface.SERIF)
        val rect = Rect()
        mPaint.getTextBounds("12", 0, "12".length, rect)
        val text12height = rect.height()
        val text12Length: Float = mPaint.measureText("12") //获得字体长度
        val text3Length: Float = mPaint.measureText("6") //获得字体长度
        canvas?.drawText(
            "12",
            mCenterX.toFloat() - text12Length / 2 - 8,
            mCenterY.toFloat() - mRadius + text12height,
            mPaint
        )
        canvas?.drawText(
            "3",
            mCenterX.toFloat() + mRadius - text3Length - 8,
            mCenterY.toFloat() + 40,
            mPaint
        )
        canvas?.drawText(
            "6",
            mCenterX.toFloat() - text3Length / 2,
            mCenterY.toFloat() + mRadius - 8,
            mPaint
        )
        canvas?.drawText("9", mCenterX.toFloat() - mRadius + 8, mCenterY.toFloat() + 40, mPaint)
        canvas?.restore()

        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)
        val secondDegree = second * 6f
        val minuteDegree = minute * 6f
        val hourDegree = if (hour * 30f < 360f) hour * 30f else hour * 30f - 360f

        //绘制时针
        canvas?.save()
        canvas?.rotate(hourDegree, mCenterX.toFloat(), mCenterY.toFloat())
        mPaint.strokeWidth = 20f
        mPaint.setColor(Color.parseColor("#f3e7c9"))
        canvas?.drawLine(
            mCenterX.toFloat(),
            mRadius.toFloat() + mRadius / 2,
            mCenterX.toFloat(), mCenterY.toFloat(),
            mPaint
        )
        canvas?.restore()

        //绘制分针
        canvas?.save()
        canvas?.rotate(minuteDegree, mCenterX.toFloat(), mCenterY.toFloat())
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.setColor(Color.parseColor("#fefefc"))
        mPaint.strokeWidth = 6f
        canvas?.drawLine(
            mCenterX.toFloat(),
            mRadius.toFloat() + mRadius / 6,
            mCenterX.toFloat(), mCenterY.toFloat(),
            mPaint
        )
        canvas?.restore()

        //绘制秒针
        canvas?.save()
        canvas?.rotate(secondDegree, mCenterX.toFloat(), mCenterY.toFloat())
        mPaint.setColor(Color.BLACK)
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = 2f
        mPaint.strokeCap = Paint.Cap.ROUND
        canvas?.drawLine(
            mCenterX.toFloat(),
            mRadius.toFloat() + mRadius / 8,
            mCenterX.toFloat(), mCenterY.toFloat(),
            mPaint
        )
        canvas?.restore()

        //绘制中心点
        mPaint.style = Paint.Style.FILL
        mPaint.color = Color.BLACK
        canvas?.drawCircle(mCenterX.toFloat(), mCenterY.toFloat(), 4f, mPaint)
    }

    private fun startAni() {
        Handler().postAtTime({
            invalidate()
        }, 1000)
    }

    private class TimeHandle(clockView: ClockTextView) : Handler() {
        private val weakView: WeakReference<ClockTextView> = WeakReference(clockView)
        override fun handleMessage(msg: Message) {
            if (msg.what == 0) {
                val view = weakView.get()
                if (view != null) {
                    view.invalidate()
                    sendEmptyMessage(0)
                }
            }
        }
    }

    public fun startTime() {
        mTimeHandle.removeMessages(0)
        mTimeHandle.sendEmptyMessage(0)
    }

    public fun stopTime() {
        mTimeHandle.removeMessages(0)
    }

}