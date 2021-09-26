package com.zxq.purerss.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Scroller
import android.widget.TextView
import androidx.core.view.NestedScrollingParent2
import androidx.core.view.NestedScrollingParentHelper
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.zxq.purerss.R

/**
 *  created by xiaoqing.zhou
 *  on 2021/9/23
 *  fun
 */
class StickLinearLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    def: Int = 0
) : LinearLayout(context, attributeSet, def), NestedScrollingParent2 {

    private var mTopViewHeight = 0
    private var mRecyclerView: RecyclerView? = null
    private var mTopView: TextView? = null
    private var mScroller: Scroller? = null

    private val mNestedScrollingParentHelper: NestedScrollingParentHelper =
        NestedScrollingParentHelper(this)

    init {
        mScroller = Scroller(getContext())
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        mTopView = findViewById(R.id.tv_topview)
        mRecyclerView = findViewById(R.id.rv_chat)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mTopViewHeight = mTopView!!.measuredHeight
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var lp = mRecyclerView?.layoutParams
        lp?.height = measuredHeight
        mRecyclerView?.layoutParams = lp
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        return (axes and ViewCompat.SCROLL_AXIS_VERTICAL) != 0
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes, type)
    }

    override fun onStopNestedScroll(target: View, type: Int) {
        mNestedScrollingParentHelper.onStopNestedScroll(target, type)
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {
        if (dyUnconsumed < 0) {
            scrollBy(0, dyUnconsumed)
        }
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        Log.i("scrollY", scrollY.toString())
        var isHideTopView = dy > 0 && scrollY < mTopViewHeight
        var isShowTopView = dy < 0 && scrollY > 0 && !ViewCompat.canScrollVertically(target, -1)
        if (isHideTopView || isShowTopView) {
            scrollBy(0, dy)
            consumed[1] = dy
        }
    }

    override fun onNestedFling(
        target: View,
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean
    ): Boolean {
        return false
    }

    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        //返回true表示拦截到内部的事件,如果返回true，则会拦截recyclerview的滑动传给外部布局滑动，导致欢动不流畅给
        return false
        if (scrollY >= mTopViewHeight) return false
        flingY(velocityY.toInt())
        return true
    }

    private fun flingY(toInt: Int) {
        mScroller?.fling(0, scrollY, 0, toInt, 0, 0, 0, mTopViewHeight)
        invalidate()
    }

    override fun scrollTo(x: Int, y: Int) {
        var disY = y
        if (y < 0) {
            disY = 0
        }

        if (y > mTopViewHeight) {
            disY = mTopViewHeight
        }
        if (y != scrollY) {
            super.scrollTo(x, disY)
        }
    }
}