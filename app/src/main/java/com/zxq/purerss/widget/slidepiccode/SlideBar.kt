package com.zxq.purerss.widget.slidepiccode

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.zxq.purerss.R
import com.zxq.purerss.utils.PixelUtil
import kotlin.properties.Delegates

/**
 *  created by xiaoqing.zhou
 *  on 2020/7/8
 *  fun
 */
class SlideBar(context: Context, attributeSet: AttributeSet?) : View(context, attributeSet) {
    private lateinit var mPaint: Paint
    private lateinit var mBitmap: Bitmap
    private lateinit var backgroundRctf: RectF
    private var mBitmapW = 0
    private var mBitmapH = 0
    private var mDistance = 0

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.style = Paint.Style.FILL
        mPaint.setColor(Color.GRAY)
        backgroundRctf = RectF()
        mBitmap = BitmapFactory.decodeResource(resources, R.drawable.add_file_64px)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mBitmapW = width / 8
        mBitmapH = height / 5 * 3
        mBitmap = resizeBitmap(mBitmap, mBitmapW, mBitmapH)
        backgroundRctf.set(0f, height.toFloat() / 5 * 2, width.toFloat(), height.toFloat() / 5 * 3)
        //绘制拖动条背景
        canvas?.drawRoundRect(
            backgroundRctf,
            PixelUtil.dp2px(context, 5f).toFloat(),
            PixelUtil.dp2px(context, 5f).toFloat(),
            mPaint
        )
        if (mDistance > width - mBitmapW) mDistance = width - mBitmapW
        if (mDistance < 0) mDistance = 0
        canvas?.drawBitmap(
            mBitmap,
            mDistance.toFloat(),
            ((height - mBitmapH) / 2).toFloat(),
            mPaint
        )
    }

    private lateinit var progressAnimator: ValueAnimator
    private lateinit var distanceAnimator: ValueAnimator
    private var userTime by Delegates.notNull<Float>() //拖动使用时间
    private var currentTemp by Delegates.notNull<Long>() //拖动开始时间

    private var _onDrag: ((Float, Float?, Boolean) -> Unit)? = null //滑动监听

    fun setOnDragListener(listener: (Float, Float?, Boolean) -> Unit) {
        _onDrag = listener
    }

    fun reset() {
        //重置拖动位置
        distanceAnimator = ValueAnimator.ofInt(mDistance, 0).apply {
            duration = 1000
            addUpdateListener {
                mDistance = it.animatedValue as Int
                invalidate()
            }
            start()
        }
        //重置滑块位置
        progressAnimator =
            ValueAnimator.ofFloat(mDistance.toFloat() / (width - mBitmapW), 0f).apply {
                duration = 1000
                addUpdateListener { _onDrag?.invoke(it.animatedValue as Float, null, false) }
                start()
            }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                currentTemp = System.currentTimeMillis()
            }
            MotionEvent.ACTION_MOVE -> {
                mDistance = if (event.getX().toInt() < 0) 0 else event.x.toInt()
                _onDrag?.invoke(mDistance.toFloat() / (width - mBitmapW), null, false)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                userTime = (System.currentTimeMillis() - currentTemp) / 1000f
                _onDrag?.invoke(mDistance.toFloat() / (width - mBitmapW), userTime, true)
            }
        }
        return true
    }

    fun resizeBitmap(bitmap: Bitmap, w: Int, h: Int): Bitmap {
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