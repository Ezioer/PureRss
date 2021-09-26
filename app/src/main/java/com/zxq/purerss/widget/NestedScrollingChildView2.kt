package com.zxq.purerss.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.view.NestedScrollingChild2

/**
 *  created by xiaoqing.zhou
 *  on 2021/9/24
 *  fun
 */
class NestedScrollingChildView2 @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    def: Int
) : LinearLayout(context, attributeSet, def), NestedScrollingChild2 {

    override fun startNestedScroll(axes: Int, type: Int): Boolean {

    }

    override fun stopNestedScroll(type: Int) {
    }

    override fun hasNestedScrollingParent(type: Int): Boolean {
    }

    override fun dispatchNestedScroll(
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        offsetInWindow: IntArray?,
        type: Int
    ): Boolean {
    }

    override fun dispatchNestedPreScroll(
        dx: Int,
        dy: Int,
        consumed: IntArray?,
        offsetInWindow: IntArray?,
        type: Int
    ): Boolean {
    }
}