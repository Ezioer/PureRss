package com.zxq.purerss.widget

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import kotlin.math.max

class HLayout @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet, def: Int
) : ViewGroup(context, attributeSet, def) {

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var widthMode = MeasureSpec.getMode(widthMeasureSpec)
        var widthSize = MeasureSpec.getSize(widthMeasureSpec)
        var heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var heightSize = MeasureSpec.getSize(heightMeasureSpec)
        var mHeight = 0
        var mWidth = 0
        for (i in 0 until childCount) {
            var view = getChildAt(i)
            measureChild(view, widthMeasureSpec, heightMeasureSpec)
            var lp = view.layoutParams as MarginLayoutParams
            var childWidth = view.measuredWidth + lp.leftMargin + lp.rightMargin
            var childHeight = view.measuredHeight + lp.topMargin + lp.bottomMargin
            mHeight = max(mHeight, childHeight)
            mWidth += childWidth
        }

        setMeasuredDimension(
            if (widthMode == MeasureSpec.EXACTLY) widthSize else mWidth,
            if (heightMode == MeasureSpec.EXACTLY) heightSize else mHeight
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var mHeight = 0
        var mWidth = 0
        var lp: LayoutParams
        var lineH = 0
        var useWidth = 0
        var useHeight = 0
        for (i in 0 until childCount) {
            var view = getChildAt(i)
            lp = view.layoutParams as MarginLayoutParams
            mHeight = view.measuredHeight
            mWidth = view.measuredWidth
            if (useWidth + mWidth > width) {
                useWidth = 0
                useHeight += lineH
                lineH = mHeight
            }
            view.layout(
                useWidth + lp.leftMargin,
                useHeight + lp.topMargin,
                useWidth + mWidth + lp.rightMargin,
                useHeight + mHeight + lp.bottomMargin
            )
            useWidth += mWidth + lp.rightMargin
            lineH = max(mHeight, lineH)
        }
    }
}