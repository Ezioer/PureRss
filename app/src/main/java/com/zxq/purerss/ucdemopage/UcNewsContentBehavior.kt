package com.zxq.purerss.ucdemopage

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.zxq.purerss.App
import com.zxq.purerss.R

/**
 *  created by xiaoqing.zhou
 *  on 2021/10/13
 *  fun
 */
class UcNewsContentBehavior(context: Context, attributeSet: AttributeSet?) :
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
        child.translationY =
            -(dependency.translationY / getHeaderOffsetRange() * 1.0f) * getScrollRange(dependency)
        return false
    }

    private fun getHeaderOffsetRange(): Int {
        return App.instance?.applicationContext?.resources?.getDimensionPixelOffset(R.dimen.uc_news_header_pager_offset)
            ?: 0
    }

    private fun getFinalHeight(): Int {
        return App.instance?.applicationContext?.resources?.getDimensionPixelOffset(R.dimen.uc_news_header_title_height)
            ?: 0 + App.instance?.applicationContext?.resources?.getDimensionPixelOffset(R.dimen.uc_news_tabs_height)!!
    }

    override fun getScrollRange(v: View): Int {
        return if (isDependView(v)) {
            v.measuredHeight - getFinalHeight()
        } else {
            super.getScrollRange(v)
        }
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

    private fun isDependView(view: View): Boolean {
        return (view != null && view.id == R.id.id_uc_news_header_pager)
    }
}