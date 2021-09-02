package com.zxq.purerss.ui

import android.content.Context
import android.graphics.*
import com.zxq.purerss.utils.PixelUtil
import com.zxq.purerss.utils.getSpValue
import java.util.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class DrawUtils {

    companion object {

        private var width = 0
        private var height = 0
        private var mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        private var mCenterX = 0
        private var mCenterY = 0
        private var mRadius = 0
        private var mRectF: RectF? = null
        private var bitmap: Bitmap? = null
        private var canvas: Canvas? = null

        private var context: Context? = null
        fun drawClock(context: Context, type: Int): Bitmap? {
            this.context = context
            width = PixelUtil.dp2px(context, 200f)
            height = PixelUtil.dp2px(context, 200f)
            mCenterX = width / 2
            mCenterY = height / 2
            mRadius = width / 4
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            canvas = Canvas(bitmap!!)
            if (type == 0) {
                return draw()
            } else if (type == 1) {
                return drawTextClock()
            } else if (type == 2) {
                return drawDays()
            }
            return null
        }

        private fun draw(): Bitmap {
            //绘制表盘
            mPaint.setColor(context!!.getSpValue("bgcolor", Color.GREEN))
            mPaint.style = Paint.Style.FILL
            mPaint.strokeWidth = 2f
            //canvas?.drawCircle(mCenterX.toFloat(), mCenterY.toFloat(), mRadius.toFloat(), mPaint)
            canvas?.save()
            val lineP = Path()
            canvas?.translate(mCenterX.toFloat(), mCenterY.toFloat())
            //每个path的转角处圆滑处理
            val pathEffect: PathEffect = CornerPathEffect(24f)
            mPaint.setPathEffect(pathEffect)
            lineP.moveTo(mRadius.toFloat(), 0f)
            for (i in 1 until 25 step 2) {
                lineP.lineTo(
                    (mRadius + 24f) * cos(15 * i * PI / 180).toFloat(),
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
             mPaint.strokeWidth = 20f
             mPaint.strokeCap = Paint.Cap.ROUND
             canvas?.drawLine(
                 mCenterX.toFloat(),
                 mRadius.toFloat() + mRadius / 3,
                 mCenterX.toFloat(), mCenterY.toFloat(),
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
            canvas?.drawCircle(
                mCenterX.toFloat(),
                mCenterY.toFloat() + mRadius * 5 / 6,
                10f,
                mPaint
            )

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
            path.addArc(rectF, 180f, 90f)
            mPaint.textSize = PixelUtil.sp2px(context!!, 8f).toFloat()
            mPaint.setColor(Color.BLACK)
            mPaint.setPathEffect(null)
            mPaint.isFakeBoldText = true
            canvas?.drawTextOnPath(week, path, 0f, 0f, mPaint)
            canvas?.restore()
            return bitmap!!
        }

        private fun drawTextClock(): Bitmap {
            //绘制表盘
            mPaint.setColor(context!!.getSpValue("bgcolor", Color.GREEN))
            mPaint.style = Paint.Style.FILL
            mPaint.strokeWidth = 2f
            canvas?.drawCircle(mCenterX.toFloat(), mCenterY.toFloat(), mRadius.toFloat(), mPaint)

            canvas?.save()
            //绘制12，3，6，9点刻度大文字
            mPaint.textSize = PixelUtil.sp2px(context!!, 44f).toFloat()
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
            return bitmap!!
        }

        private fun drawDays(): Bitmap {
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
            mPaint.textSize = PixelUtil.sp2px(context!!, 30f).toFloat()
            canvas?.drawText(week, mCenterX / 4f, mCenterY.toFloat() - mCenterY / 4, mPaint)
            mPaint.textSize = PixelUtil.sp2px(context!!, 60f).toFloat()
            canvas?.drawText("$day", mCenterX.toFloat(), mCenterY.toFloat() + mCenterY / 2, mPaint)
            return bitmap!!
        }
    }
}