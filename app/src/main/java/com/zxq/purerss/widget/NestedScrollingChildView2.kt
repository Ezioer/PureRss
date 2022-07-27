package com.zxq.purerss.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.ViewConfiguration
import android.widget.LinearLayout
import android.widget.Scroller
import androidx.core.view.NestedScrollingChild2
import androidx.core.view.NestedScrollingChildHelper
import androidx.core.view.ViewCompat
import androidx.core.view.ViewCompat.TYPE_NON_TOUCH
import androidx.core.view.ViewCompat.TYPE_TOUCH

/**
 *  created by xiaoqing.zhou
 *  on 2021/9/24
 *  fun
 */
class NestedScrollingChildView2 @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    def: Int = 0
) : LinearLayout(context, attributeSet, def), NestedScrollingChild2 {
    private val mScrollingChildHelper: NestedScrollingChildHelper = NestedScrollingChildHelper(this)
    private var mMinVelocity = 0
    private var mMaxVelocity = 0
    private var mScroller: Scroller
    private var mOrientation = 0
    private var mVelocityTracker: VelocityTracker? = null
    private var mLastX = -1
    private var mLastY = -1
    private val offset = IntArray(2)
    private val consumed = IntArray(2)

    init {
        orientation = VERTICAL
        mOrientation = orientation
        var conf = ViewConfiguration.get(context)
        mMinVelocity = conf.scaledMinimumFlingVelocity
        mMaxVelocity = conf.scaledMaximumFlingVelocity
        mScroller = Scroller(context)
        isNestedScrollingEnabled = true
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        var action = event?.actionMasked
        cancelFling()
        if (mLastX == -1 || mLastY == -1) {
            mLastX = event!!.rawX.toInt()
            mLastY = event!!.rawY.toInt()
        }
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        }
        mVelocityTracker?.addMovement(event)
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                mLastX = event!!.rawX.toInt()
                mLastY = event!!.rawY.toInt()
                if (mOrientation == VERTICAL) {
                    startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, TYPE_TOUCH)
                } else {
                    startNestedScroll(ViewCompat.SCROLL_AXIS_HORIZONTAL, TYPE_TOUCH)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                var currentX = event!!.rawX.toInt()
                var currentY = event!!.rawY.toInt()
                var dx = mLastX - currentX
                var dy = mLastY - currentY
                if (dispatchNestedPreScroll(dx, dy, consumed, offset, TYPE_TOUCH)) {
                    dx -= consumed[0]
                    dy -= consumed[1]
                }
                var consumeX = 0
                var consumeY = 0
                if (mOrientation == VERTICAL) {
                    consumeY = childConsumedY(dy)
                } else {
                    consumeX = childConsumedX(dx)
                }

                dispatchNestedScroll(
                    consumeX,
                    consumeY,
                    dx - consumeX,
                    dy - consumeY,
                    null,
                    TYPE_TOUCH
                )
                mLastX = currentX
                mLastY = currentY
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                stopNestedScroll(TYPE_TOUCH)
                mVelocityTracker?.computeCurrentVelocity(1000, mMaxVelocity.toFloat())
                var xVel = mVelocityTracker!!.xVelocity.toInt()
                var yVel = mVelocityTracker!!.yVelocity.toInt()
                fling(xVel, yVel)
                if (mVelocityTracker != null) {
                    mVelocityTracker?.recycle()
                    mVelocityTracker = null
                }
                mLastY = -1
                mLastX = -1
            }
        }
        return true
    }

    private fun fling(xVel: Int, yVel: Int): Boolean {
        var x = xVel
        var y = yVel
        if (Math.abs(xVel) < mMinVelocity) {
            x = 0
        }
        if (Math.abs(yVel) < mMinVelocity) {
            y = 0
        }
        if (x == 0 || y == 0) {
            return false
        }
        if (mOrientation == VERTICAL) {
            startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, TYPE_NON_TOUCH)
        } else {
            startNestedScroll(ViewCompat.SCROLL_AXIS_HORIZONTAL, TYPE_NON_TOUCH)
        }
        x = Math.max(-mMaxVelocity, Math.min(xVel, mMaxVelocity))
        y = Math.max(-mMaxVelocity, Math.min(yVel, mMaxVelocity))
        doFling(x, y)
        return true
    }

    private fun doFling(x: Int, y: Int) {
        fling = true
        mScroller.fling(
            0,
            0,
            x,
            y,
            Int.MIN_VALUE,
            Int.MAX_VALUE,
            Int.MIN_VALUE,
            Int.MAX_VALUE
        )
        postInvalidate()
    }

    private var fling = false
    private var mLastFlingX = 0
    private var mLastFlingY = 0
    private val flingConsumed = IntArray(2)
    override fun computeScroll() {
        if (mScroller.computeScrollOffset() && fling) {
            var currentX = mScroller.currX
            var currentY = mScroller.currY
            var dx = mLastFlingX - currentX
            var dy = mLastFlingY - currentY
            mLastFlingY = currentY
            mLastFlingX = currentX
            if (dispatchNestedPreScroll(dx, dy, flingConsumed, null, TYPE_NON_TOUCH)) {
                dx -= flingConsumed[0]
                dy -= flingConsumed[1]
            }
            var hResult = 0
            var vResult = 0
            var leaveV = 0
            var leaveH = 0
            if (dx != 0) {
                leaveH = childFlingX(dx)
                hResult = dx - leaveH
            }
            if (dy != 0) {
                leaveV = childFlingY(dy)
                vResult = dy - leaveV
            }
            dispatchNestedScroll(leaveH, leaveV, hResult, vResult, null, TYPE_NON_TOUCH)
            postInvalidate()
        } else {
            stopNestedScroll(TYPE_NON_TOUCH)
            cancelFling()
        }
    }

    private fun childFlingY(dy: Int): Int {
        return 0
    }

    private fun childFlingX(dx: Int): Int {
        return 0
    }

    private fun cancelFling() {
        fling = false
        mLastFlingX = 0
        mLastFlingY = 0
    }

    private fun childConsumedY(dx: Int): Int {

        return 0
    }

    private fun childConsumedX(dy: Int): Int {
        return 0
    }

    override fun startNestedScroll(axes: Int, type: Int): Boolean {
        return mScrollingChildHelper.startNestedScroll(axes, type)
    }

    override fun stopNestedScroll(type: Int) {
        mScrollingChildHelper.stopNestedScroll(type)
    }

    override fun hasNestedScrollingParent(type: Int): Boolean {
        return mScrollingChildHelper.hasNestedScrollingParent(type)
    }

    override fun setNestedScrollingEnabled(enabled: Boolean) {
        mScrollingChildHelper.isNestedScrollingEnabled = enabled
    }

    override fun isNestedScrollingEnabled(): Boolean {
        return mScrollingChildHelper.isNestedScrollingEnabled
    }

    override fun dispatchNestedScroll(
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        offsetInWindow: IntArray?,
        type: Int
    ): Boolean {
        return mScrollingChildHelper.dispatchNestedScroll(
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            offsetInWindow,
            type
        )
    }

    override fun dispatchNestedPreScroll(
        dx: Int,
        dy: Int,
        consumed: IntArray?,
        offsetInWindow: IntArray?,
        type: Int
    ): Boolean {
        return mScrollingChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type)
    }

    private fun canScroll(): Boolean {
        return true
    }
}