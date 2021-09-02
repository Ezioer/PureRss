package com.zxq.purerss.widget

import android.content.Context
import android.graphics.*
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.View
import com.zxq.purerss.utils.PixelUtil
import com.zxq.purerss.utils.getSpValue
import java.lang.ref.WeakReference
import java.util.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class ClockView @JvmOverloads constructor(
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
        mPaint.setColor(context!!.getSpValue("bgcolor", Color.GREEN))
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = 2f
//        canvas?.drawCircle(mCenterX.toFloat(), mCenterY.toFloat(), mRadius.toFloat(), mPaint)
        canvas?.save()
        val lineP = Path()
        canvas?.translate(mCenterX.toFloat(), mCenterY.toFloat())
        //每个path的转角处圆滑处理
        val pathEffect: PathEffect = CornerPathEffect(24f)
        mPaint.setPathEffect(pathEffect)
        lineP.moveTo(mRadius.toFloat(), 0f)
        for (i in 1 until 25 step 2) {
            lineP.lineTo(
                (mRadius + 24f) * cos(Math.toRadians(15.0 * i)).toFloat(),
                (mRadius + 24f) * sin(15 * i * PI / 180).toFloat()
            )
            lineP.lineTo(
                (mRadius) * cos(15 * (i + 1) * PI / 180).toFloat(),
                (mRadius) * sin(15 * (i + 1) * PI / 180).toFloat()
            )
        }

        canvas?.drawPath(lineP, mPaint)
        canvas?.restore()

        //绘制中心点
        mPaint.style = Paint.Style.FILL
        canvas?.drawCircle(mCenterX.toFloat(), mCenterY.toFloat(), 10f, mPaint)

        //绘制刻度
        /* var lineLength = 0f
         canvas?.save()
         for (i in 0 until 60) {
             if (i % 5 == 0) {
                 //绘制长刻度
                 lineLength = 40f
                 mPaint.strokeWidth = 4f
                 mPaint.setColor(Color.BLACK)
             } else {
                 //绘制短刻度
                 lineLength = 20f
                 mPaint.strokeWidth = 2f
                 mPaint.setColor(Color.BLUE)
             }

             canvas?.drawLine(mCenterX.toFloat(), 30f, mCenterX.toFloat(), 30f + lineLength, mPaint)
             canvas?.rotate(6f, mCenterX.toFloat(), mCenterY.toFloat())
         }
         canvas?.restore()*/

        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)
        val secondDegree = second * 6f
        val minuteDegree = minute * 6f
        val hourDegree = if (hour * 30f < 360f) hour * 30f else hour * 30f - 360f
        //绘制秒针
        /* canvas?.save()
         canvas?.rotate(secondDegree, mCenterX.toFloat(), mCenterY.toFloat())
         mPaint.setColor(Color.RED)
         mPaint.style = Paint.Style.FILL
         mPaint.strokeWidth = 8f
         mPaint.strokeCap = Paint.Cap.ROUND
         canvas?.drawLine(
             mCenterX.toFloat(),
             mCenterY.toFloat(),
             mCenterX.toFloat(), mRadius / 5f + 20 ,
             mPaint
         )
         canvas?.restore()*/

        //绘制分针
        canvas?.save()
        canvas?.rotate(minuteDegree, mCenterX.toFloat(), mCenterY.toFloat())
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.setColor(Color.parseColor("#3d4851"))
        mPaint.strokeWidth = 20f
        canvas?.drawLine(
            mCenterX.toFloat(),
            mRadius.toFloat() + mRadius / 3,
            mCenterX.toFloat(), mCenterY.toFloat(),
            mPaint
        )
        canvas?.restore()

        //绘制时针
        canvas?.save()
        canvas?.rotate(hourDegree, mCenterX.toFloat(), mCenterY.toFloat())
        mPaint.strokeWidth = 20f
        mPaint.setColor(Color.parseColor("#bdc7d3"))
        canvas?.drawLine(
            mCenterX.toFloat(),
            mRadius.toFloat() + mRadius * 2 / 3,
            mCenterX.toFloat(), mCenterY.toFloat(),
            mPaint
        )
        canvas?.restore()

        //绘制6点时的小球 #8fecfc
        mPaint.setColor(Color.parseColor("#8fecfc"))
        mPaint.style = Paint.Style.FILL
        canvas?.drawCircle(mCenterX.toFloat(), mCenterY.toFloat() + mRadius * 5 / 6, 10f, mPaint)

        //绘制星期
        canvas?.save()
//        canvas?.rotate(25f, mCenterX.toFloat(), mCenterY.toFloat())
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
        week += "  $day"
        val path = Path()
        canvas?.translate(0f, 0f)
        mPaint.strokeWidth = 2f
        val rectF = RectF(
            mCenterX - mRadius + 46f, mCenterX - mRadius + 46f,
            mCenterX.toFloat() + mRadius / 2, mCenterY.toFloat() + mRadius / 2
        )
        /* mPaint.color = Color.GREEN
         canvas?.drawRect(rectF,mPaint)*/
        path.addArc(rectF, 180f, 90f)
        /* mPaint.setColor(Color.RED)
         canvas?.drawPath(path,mPaint)*/
        mPaint.textSize = PixelUtil.sp2px(this.context, 10f).toFloat()
        mPaint.setColor(Color.BLACK)
        mPaint.pathEffect = null
        mPaint.isFakeBoldText = true
        mPaint.hinting = Paint.HINTING_ON
        mPaint.isSubpixelText = true
        canvas?.drawTextOnPath(week, path, 0f, 0f, mPaint)
        canvas?.restore()
    }

    private fun startAni() {
        Handler().postAtTime({
            invalidate()
        }, 1000)
    }

    private class TimeHandle(clockView: ClockView) : Handler() {
        private val weakView: WeakReference<ClockView> = WeakReference(clockView)
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