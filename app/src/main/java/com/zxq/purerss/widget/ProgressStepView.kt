package com.zxq.purerss.widget

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.zxq.purerss.R
import com.zxq.purerss.utils.PixelUtil

class ProgressStepView @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null
    , def: Int = 0
) : View(context, attributeSet, def) {
    private var mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mCenterX = 0
    private var mCenterY = 0
    private var mSmallRadius = 0
    private var smallCircleColor = Color.parseColor("#a06563")
    private var bigCircleColor = Color.parseColor("#c67d78")
    private var progressColor = Color.parseColor("#f7d2c0")
    private var mBigRadius = 0
    private var progress = 40f
    private var currentProgress = 0f
    private var maxProgress = 100f
    private var progressText = "0k"
    private var mWalkBitmap: Bitmap
    private var anima: ValueAnimator? = null
    private var scaleBitmap: Bitmap

    init {
        mWalkBitmap = BitmapFactory.decodeResource(resources, R.drawable.walking)
        val matrix = Matrix()
        matrix.setScale(0.5f, 0.5f)
        scaleBitmap = Bitmap.createBitmap(
            mWalkBitmap,
            0,
            0,
            mWalkBitmap.width,
            mWalkBitmap.height,
            matrix,
            false
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCenterX = width / 2
        mCenterY = height / 2
        mBigRadius = width / 2 - width / 16
        mSmallRadius = width / 2 - mBigRadius / 4
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mPaint.setColor(bigCircleColor)
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = (mBigRadius / 4).toFloat()
        canvas?.drawCircle(mCenterX.toFloat(), mCenterY.toFloat(), mBigRadius.toFloat(), mPaint)
        mPaint.style = Paint.Style.FILL
        mPaint.setColor(smallCircleColor)
        canvas?.drawCircle(mCenterX.toFloat(), mCenterY.toFloat(), mSmallRadius.toFloat(), mPaint)
        val ovel = RectF(
            mCenterX.toFloat() - mBigRadius,
            mCenterY.toFloat() - mBigRadius,
            mCenterX.toFloat() + mBigRadius,
            mCenterY.toFloat() + mBigRadius
        )
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.setColor(progressColor)
        canvas?.drawArc(ovel, 0f, 360f * currentProgress / maxProgress, false, mPaint)
        mPaint.style = Paint.Style.FILL
        mPaint.setColor(Color.WHITE)
        var rect = Rect()
        mPaint.getTextBounds(progressText, 0, progressText.length, rect)
        val fontMetrics: Paint.FontMetricsInt = mPaint.getFontMetricsInt()
        val baseline =
            (measuredHeight - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top //获得文字的基准线
        mPaint.textSize = PixelUtil.sp2px(this.context, 22f).toFloat()
        canvas?.drawText(
            progressText, (getMeasuredWidth() / 2 - rect.width() / 2).toFloat(),
            baseline.toFloat() - mSmallRadius / 4, mPaint
        )

        canvas?.drawBitmap(
            scaleBitmap, mCenterX.toFloat() - PixelUtil.px2dp(context, 120f),
            mCenterY.toFloat() + PixelUtil.px2dp(context, 100f), mPaint
        )
    }

    fun startAni() {
        if (anima == null) {
            anima = ObjectAnimator.ofFloat(0f, progress)
        }
        anima?.addUpdateListener {
            currentProgress = it.getAnimatedValue() as Float
//            progressText = currentProgress.toInt().toString()
            postInvalidate()
        }
        anima?.setDuration(2000)
        anima?.start()
    }

    //设置当前进度
    fun setProgress(progressValue: Int) {
        progress = progressValue.toFloat()
        progressText = "${progressValue / 1000.00} k"
        postInvalidate()
    }

    //设置目标值
    fun setGoal(goldValue: Int) {
        maxProgress = goldValue.toFloat()
        postInvalidate()
    }
}