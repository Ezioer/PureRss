package com.zxq.purerss.widget.diybehavior

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.RecyclerView

/**
 *  created by xiaoqing.zhou
 *  on 2021/10/11
 *  fun
 */
class SimpleTitleBehavior(context: Context?, attrs: AttributeSet? = null) :
    CoordinatorLayout.Behavior<View>(context, attrs) {
    //列表顶部和title底部重合时列表滑动的距离
    private var mDy = 0f
    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        return dependency is RecyclerView
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        if (mDy == 0f) {
            mDy = dependency.y - child.height
        }
        var dy = dependency.y - child.height
        dy = if (dy < 0) 0f else dy
        var y = -(dy / mDy) * child.height
        child.translationY = y
        child.alpha = 1 - dy / mDy
        return true
    }
}