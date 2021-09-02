package com.zxq.purerss.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.zxq.purerss.utils.PixelUtil

/**
 *  created by xiaoqing.zhou
 *  on 2020/7/10
 *  fun
 */
class BezierView(context: Context, attributeSet: AttributeSet?) : View(context, attributeSet) {
    private var paint: Paint
    private var centerX = 0
    private var centerY = 0
    private var start: PointF
    private var end: PointF
    private var control: PointF
    private var mAnima: ValueAnimator? = null

    init {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.GREEN
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = PixelUtil.dp2px(context, 1f).toFloat()
        start = PointF(0f, 0f)
        end = PointF(0f, 0f)
        control = PointF(0f, 0f)
        getAnimator()
    }

    private fun getAnimator() {
        mAnima = ValueAnimator.ofFloat(1f, 0f).apply {
            duration = 1000
            addUpdateListener {
                val value = it.getAnimatedValue() as Float
                control.x = (1 - value) * centerX + control.x * value
                control.y = (1 - value) * centerY + control.y * value
                invalidate()
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = w / 2
        centerY = h / 2
        start.x = (centerX - 200).toFloat()
        start.y = centerY.toFloat()

        end.x = (centerX + 200).toFloat()
        end.y = centerY.toFloat()

        control.x = centerX.toFloat()
        control.y = (centerY - 200).toFloat()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
            MotionEvent.ACTION_MOVE -> {
                control.x = event.getX()
                control.y = event.getY()
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                startAni()
            }
        }

        return true
    }

    private fun startAni() {
        mAnima?.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = Color.GREEN
        canvas.drawLine(start.x, start.y, control.x, control.y, paint)
        canvas.drawLine(control.x, control.y, end.x, end.y, paint)

        val path = Path()
        path.moveTo(start.x, start.y)
        path.quadTo(control.x, control.y, end.x, end.y)
        paint.color = Color.RED
        paint.style = Paint.Style.FILL_AND_STROKE
        canvas.drawPath(path, paint)
    }
}