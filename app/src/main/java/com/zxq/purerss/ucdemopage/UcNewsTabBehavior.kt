package com.zxq.purerss.ucdemopage

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.zxq.purerss.App
import com.zxq.purerss.R

/**
 *  created by xiaoqing.zhou
 *  on 2021/10/13
 *  fun
 */
class UcNewsTabBehavior(context: Context, attributeSet: AttributeSet?) :
    HeaderScrollingViewBehavior(context, attributeSet) {
    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        return isDependView(dependency)
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        val offsetRange = (dependency.top + getFinalHeight() - child.top).toFloat()
        val headerOffsetRange = getHeaderOffsetRange().toFloat()
        Log.i("dependency", "y ------->" + dependency.translationY + "offser------->" + offsetRange)
        when {
            dependency.translationY == headerOffsetRange + getFinalHeight() -> {
                child.translationY = offsetRange + getFinalHeight()
            }
            dependency.translationY === 0f -> {
                child.translationY = 0f
            }
            else -> {
                child.translationY =
                    (dependency.translationY / headerOffsetRange * 1.0f * offsetRange - dependency.translationY / headerOffsetRange * 1.0 * getFinalHeight()).toFloat()
                Log.i("dependency", "child y ------->" + dependency.translationY)
            }
        }
        return false
    }

    private fun getHeaderOffsetRange(): Int {
        return App.instance?.applicationContext?.resources?.getDimensionPixelOffset(R.dimen.uc_news_header_pager_offset)
            ?: 0
    }

    private fun getFinalHeight(): Int {
        return App.instance?.applicationContext?.resources?.getDimensionPixelOffset(R.dimen.uc_news_header_title_height)!!
    }

    override fun findFirstDependency(views: MutableList<View>): View? {
        for (i in 0 until views.size) {
            var view = views[i]
            if (isDependView(view)) {
                return view
            }
        }
        return null
    }

    override fun layoutChild(parent: CoordinatorLayout, child: View, layoutDirection: Int) {
        super.layoutChild(parent, child, layoutDirection)
    }

    private fun isDependView(view: View): Boolean {
        return (view != null && view.id == R.id.id_uc_news_header_pager)
    }
}