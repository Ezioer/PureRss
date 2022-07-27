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
class UcNewsTitleBehavior(context: Context, attributeSet: AttributeSet?) :
    CoordinatorLayout.Behavior<View>(context, attributeSet) {
    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        return isDependView(dependency)
    }

    override fun onLayoutChild(
        parent: CoordinatorLayout,
        child: View,
        layoutDirection: Int
    ): Boolean {
        (child.layoutParams as CoordinatorLayout.LayoutParams).topMargin = -getTitleHeight()
        parent.onLayoutChild(child, layoutDirection)
        return true
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        val offsetRange = getHeaderOffsetRange().toFloat()
        val titleHeight = getTitleHeight().toFloat()
        when {
            dependency.translationY == offsetRange -> {
                child.translationY = titleHeight
            }
            dependency.translationY === 0f -> {
                child.translationY = 0f
            }
            else -> {
                child.translationY =
                    (dependency.translationY / (offsetRange * 1.0f) * titleHeight)
                Log.i("dependency", "title ------->" + child.translationY)
            }
        }
        return false
    }

    private fun getHeaderOffsetRange(): Int {
        return App.instance?.applicationContext?.resources?.getDimensionPixelOffset(R.dimen.uc_news_header_pager_offset)
            ?: 0
    }

    private fun getTitleHeight(): Int {
        return App.instance?.applicationContext?.resources?.getDimensionPixelOffset(R.dimen.uc_news_header_title_height)!!
    }

    private fun isDependView(view: View): Boolean {
        return (view != null && view.id == R.id.id_uc_news_header_pager)
    }
}