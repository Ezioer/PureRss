package com.zxq.purerss.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.*
import android.widget.OverScroller
import com.zxq.purerss.utils.dp2px
import com.zxq.purerss.utils.sp2px
import kotlin.math.roundToInt

class RulerView @JvmOverloads constructor(
    context: Context, mParent: RuleGroupView, attributeSet: AttributeSet? = null
    , def: Int = 0
) : View(context, attributeSet, def) {
    private var mLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mLineColor = Color.parseColor("#282832")
    private var mCenterX = 0
    private var mCenterY = 0

    //以0。1kg为单位
    private var mMin = 400
    private var mMax = 2000

    //每个刻度相隔的距离
    private var mInterval = 18

    //每个大刻度包含的小刻度数
    private var mCount = 10

    //惯性最大最小速度
    private var mMaximumVelocity = 0  //惯性最大最小速度
    private var mMinimumVelocity = 0

    //速度获取
    private var mVelocityTracker: VelocityTracker? = null

    //记录上次滑动的位置
    private var mLastX = 0f

    //控制滑动
    private var mOverScroller: OverScroller? = null

    //长度、最小可滑动值、最大可滑动值
    private var mLength = 0  //长度、最小可滑动值、最大可滑动值
    private var mMinPositionX = 0  //长度、最小可滑动值、最大可滑动值
    private var mMaxPositionX = 0

    private var mCurrentScale = 440f

    init {
        mLineColor = mParent.mLineColor
        mTextPaint.color = mLineColor
        mTextPaint.textSize = sp2px(22f).toFloat()

//        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        //配置速度
        mVelocityTracker = VelocityTracker.obtain()
        mMaximumVelocity = ViewConfiguration.get(context)
            .scaledMaximumFlingVelocity
        mMinimumVelocity = ViewConfiguration.get(context)
            .scaledMinimumFlingVelocity

        mOverScroller = OverScroller(getContext())

        //第一次进入，跳转到设定刻度
        viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                goToScale(mCurrentScale)
            }
        })
    }

    private fun goToScale(scale: Float) {
        mCurrentScale = Math.round(scale).toFloat()
        scrollTo(scale2ScrollX(mCurrentScale).toInt(), 0)
        callBack?.scaleValue(mCurrentScale)
    }

    //滑动的距离转换为刻度
    private fun scrollX2Scale(disX: Float): Float {
        return (disX - mMinPositionX) * (mMax - mMin) / mLength + mMin
    }

    //刻度转换为滑动的距离
    private fun scale2ScrollX(scale: Float): Float {
        return (scale - mMin) * mLength / (mMax - mMin) + mMinPositionX
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCenterX = width / 2
        mCenterY = height / 2
        mLength = (mMax - mMin) * mInterval
        mMinPositionX = -width / 2
        mMaxPositionX = mLength - width / 2
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        var currentX = event?.getX() ?: 0f
        //开始速度检测
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        }
        mVelocityTracker!!.addMovement(event)
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                if (!mOverScroller!!.isFinished()) {
                    mOverScroller?.abortAnimation()
                }
                mLastX = currentX
            }

            MotionEvent.ACTION_MOVE -> {
                var dis = mLastX - currentX
                mLastX = currentX
                scrollBy(dis.toInt(), 0)
            }

            MotionEvent.ACTION_UP -> {
                //处理松手后的Fling
                mVelocityTracker!!.computeCurrentVelocity(1000, mMaximumVelocity.toFloat())
                val velocityX = mVelocityTracker!!.xVelocity.toInt()
                if (Math.abs(velocityX) > mMinimumVelocity) {
                    fling(-velocityX)
                } else {
                    scroll2ScaleLine()
                }

                if (mVelocityTracker != null) {
                    mVelocityTracker?.recycle()
                    mVelocityTracker = null
                }
            }

            MotionEvent.ACTION_CANCEL -> {
                if (!mOverScroller!!.isFinished) {
                    mOverScroller?.abortAnimation()
                }
                if (mVelocityTracker != null) {
                    mVelocityTracker?.recycle()
                    mVelocityTracker = null
                }
            }
        }
        return true
    }

    private fun fling(vX: Int) {
        mOverScroller!!.fling(scrollX, 0, vX, 0, mMinPositionX, mMaxPositionX, 0, 0)
        invalidate()
    }

    override fun computeScroll() {
        //返回true滑动动画还没停止，则继续滑动
        if (mOverScroller!!.computeScrollOffset()) {
            scrollTo(mOverScroller!!.currX, mOverScroller!!.currY)
            //如果滑动停止了并且当前的刻度值不是蒸熟，说明滑到中间去了，此时应该继续滑动到最近的刻度
            if (!mOverScroller!!.computeScrollOffset() && mCurrentScale != mCurrentScale.roundToInt()
                    .toFloat()
            ) {
                scroll2ScaleLine()
            }
            invalidate()
        }
    }

    private fun scroll2ScaleLine() {
        mCurrentScale = Math.round(mCurrentScale).toFloat()
        mOverScroller?.startScroll(
            scrollX,
            0,
            (scale2ScrollX(mCurrentScale) - scrollX).toInt(),
            0,
            1000
        )
        invalidate()
    }


    override fun scrollTo(x: Int, y: Int) {
        var disX = x
        if (disX < mMinPositionX) {
            disX = mMinPositionX
        }
        if (disX > mMaxPositionX) {
            disX = mMaxPositionX
        }
        if (disX != scrollX) {
            super.scrollTo(disX, y)
        }
        mCurrentScale = scrollX2Scale(x.toFloat())
        callBack?.scaleValue(Math.round(mCurrentScale).toFloat())
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawLines(canvas)
    }

    //绘制长短刻度以及长刻度下面的文字
    private fun drawLines(canvas: Canvas?) {
        //长刻度的长和宽都是短刻度的一倍
        mLinePaint.color = mLineColor

        for (i in mMin until mMax) {
            var disX = (i - mMin) * mInterval
            if (i % mCount == 0) {
                mLinePaint.strokeWidth = dp2px(2f).toFloat()
                canvas?.drawLine(disX.toFloat(), 0f, disX.toFloat(), mCenterY.toFloat(), mLinePaint)
                canvas?.drawText(
                    (i / 10).toString(),
                    disX.toFloat() - mTextPaint.measureText((i / 10).toString()) / 2,
                    mCenterY + dp2px(30f).toFloat(),
                    mTextPaint
                )
            } else {
                mLinePaint.strokeWidth = dp2px(1f).toFloat()
                canvas?.drawLine(
                    disX.toFloat(),
                    0f,
                    disX.toFloat(),
                    mCenterY.toFloat() / 2,
                    mLinePaint
                )
            }
        }
    }

    private var callBack: ScaleCallback? = null
    fun setCallback(callback: ScaleCallback) {
        this.callBack = callback
    }

    interface ScaleCallback {
        fun scaleValue(scale: Float)
    }
}