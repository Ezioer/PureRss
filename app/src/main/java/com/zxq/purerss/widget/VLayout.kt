package com.zxq.purerss.widget

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.view.marginBottom
import kotlin.math.max

class VLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    def: Int = 0
) :
    ViewGroup(context, attributeSet, def) {

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var widthModel = MeasureSpec.getMode(widthMeasureSpec)
        var widthSize = MeasureSpec.getSize(widthMeasureSpec)
        var heightModel = MeasureSpec.getMode(heightMeasureSpec)
        var heightSize = MeasureSpec.getSize(heightMeasureSpec)

        var mWidth = 0
        var mHeight = 0
        for (i in 0 until childCount) {
            var view = getChildAt(i)
            measureChild(view, widthMeasureSpec, heightMeasureSpec)
            val layoutParams = view.layoutParams as MarginLayoutParams
            val childWidth = view.measuredWidth + layoutParams.leftMargin + layoutParams.rightMargin
            val childHeight =
                view.measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin
            mWidth = max(childWidth, mWidth)
            mHeight += childHeight
        }
        setMeasuredDimension(
            if (widthModel == MeasureSpec.EXACTLY) widthSize else mWidth,
            if (heightModel == MeasureSpec.EXACTLY) heightSize else mHeight
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var childWidth = 0
        var childHeight = 0
        var layoutParams: LayoutParams
        var childTopMargin = 0
        for (i in 0 until childCount) {
            var view = getChildAt(i)
            childWidth = view.measuredWidth
            childHeight = view.measuredHeight
            layoutParams = view.layoutParams as MarginLayoutParams
            childTopMargin += layoutParams.topMargin
            view.layout(
                layoutParams.leftMargin, childTopMargin,
                layoutParams.leftMargin + childWidth, childTopMargin + childHeight
            )
            childTopMargin += childHeight + view.marginBottom
        }
    }
}