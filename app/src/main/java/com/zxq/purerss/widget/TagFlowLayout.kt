package com.zxq.purerss.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.zxq.purerss.R

/**
 *  created by xiaoqing.zhou
 *  on 2020/7/7
 *  fun
 */
class TagFlowLayout(context: Context, attributes: AttributeSet?) : ViewGroup(context, attributes) {
    private var isCenter = false

    init {
        var attr = context.obtainStyledAttributes(attributes, R.styleable.TagFlowLayout)
        isCenter = attr.getBoolean(R.styleable.TagFlowLayout_isCenter, false)
        attr.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthModel = MeasureSpec.getMode(widthMeasureSpec)
        //父view期待的宽度
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightModel = MeasureSpec.getMode(heightMeasureSpec)
        //父view期待的高度
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        //最终要设置的宽度
        var wWidth = 0
        //最终要设置的高度
        var wHeight = 0
        //一行的宽度
        var lineW = 0
        //一行的高度
        var lineH = 0

        var marginLayoutParams: MarginLayoutParams? = null
        val count = childCount
        for (i in 0 until count) {
            var child = getChildAt(i)
            if (child.visibility == View.GONE) {
                //如果当前子view不可见，则不管，继续下一个子view的测量
                if (i == count - 1) {
                    //如果最后一个子view是不可见状态，宽取当前最大的宽和当前行的宽最大值
                    wWidth = Math.max(lineW, wWidth)
                    wHeight += lineH
                }
                continue
            }
            measureChildren(widthMeasureSpec, heightMeasureSpec)
            marginLayoutParams = child.layoutParams as MarginLayoutParams
            val childW =
                child.measuredWidth + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin
            val childH =
                child.measuredHeight + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin
            if (lineW + childW > widthSize - paddingLeft - paddingRight) {
                //一行的内容大于当前宽度，执行换行
                //取多行中最大行的宽度作为布局的宽度
                wWidth = Math.max(wWidth, lineW)
                lineW = childW
                wHeight += lineH
                lineH = childH
            } else {
                //宽度在一行宽度范围内，累加每次的宽度作为当前行的宽度
                lineW += childW
                //取当前子view和每行最大的高度为当前行的高度
                lineH = Math.max(lineH, childH)
            }
            if (i == count - 1) {
                wWidth = Math.max(lineW, wWidth)
                //最后一个子view时，累加每行的高度为当前布局的高度
                wHeight += lineH
            }
        }

        setMeasuredDimension(
            if (widthModel == MeasureSpec.EXACTLY) widthSize else wWidth + paddingLeft + paddingRight,
            if (heightModel == MeasureSpec.EXACTLY) heightSize else wHeight + paddingTop + paddingBottom
        )
    }

    //总共行数
    private var mLines = mutableListOf<MutableList<View>>()

    //每一行的view
    private var lineViews = mutableListOf<View>()

    //每一行的宽
    private var mLineHeights = mutableListOf<Int>()

    //每一行的高
    private var mLineWidth = mutableListOf<Int>()
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        lineViews.clear()
        mLines.clear()
        mLineWidth.clear()
        mLineHeights.clear()
        var topStart = paddingTop
        var leftStart = paddingLeft
        val wWidth = width
        var lineW = 0
        var lineH = 0
        var lp: MarginLayoutParams? = null
        var count = childCount
        for (i in 0 until count) {
            var child = getChildAt(i)
            if (child.visibility == View.GONE) {
                continue
            }
            lp = child.layoutParams as MarginLayoutParams
            val childW = child.measuredWidth
            val childH = child.measuredHeight
            if (lineW + childW + lp.leftMargin + lp.rightMargin > wWidth - paddingLeft - paddingRight) {
                //新启一行，将前一行的行宽和行高存进数组
                mLineHeights.add(lineH)
                mLineWidth.add(lineW)
                mLines.add(lineViews)
                lineW = 0
                lineH = childH + lp.topMargin + lp.bottomMargin
                lineViews = mutableListOf()
            }
            //累加每个子view的宽度作为行宽
            lineW += childW + lp.leftMargin + lp.rightMargin
            lineH = Math.max(lineH, childH + lp.topMargin + lp.bottomMargin)
            lineViews.add(child)
        }

        //将最后一行的行宽和行高存进数组
        mLineHeights.add(lineH)
        mLineWidth.add(lineW)
        //将最后一行的子view数组存进数组
        mLines.add(lineViews)
        for (i in mLines.indices) {
            val views = mLines[i]
            lineH = mLineHeights[i]
            var currentW = mLineWidth[i]
            //新启一行时，左边距需要置为默认状态，此为将子view居中的设置
            leftStart = if (!isCenter) {
                paddingLeft
            } else {
                //将当前子view居中
                (wWidth - currentW) / 2 + paddingLeft
            }
            for (view in views) {
                //为每一行的子view布局
                lp = view.layoutParams as MarginLayoutParams
                val lc = leftStart + lp.leftMargin
                val tc = topStart + lp.topMargin
                val rc = lc + view.measuredWidth
                val bc = tc + view.measuredHeight
                view.layout(lc, tc, rc, bc)
                //在一行内，左边距是累加的
                leftStart += view.measuredWidth + lp.leftMargin + lp.rightMargin
            }
            //新启一行后，距顶部距离需要累加之前行的高度
            topStart += lineH
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams? {
        return MarginLayoutParams(context, attrs)
    }

    override fun generateDefaultLayoutParams(): LayoutParams? {
        return MarginLayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
    }

    override fun generateLayoutParams(p: LayoutParams?): LayoutParams? {
        return MarginLayoutParams(p)
    }
}