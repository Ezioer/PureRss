package com.zxq.purerss.widget

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.ViewGroup

class TagLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    // 设置view 支持 layout margin 属性
    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    private var childBoundsList = mutableListOf<Rect>()


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //用了多少宽度
        var widthUsed = 0
        //用了多少高度
        var heightUsed = 0

        //每一行用了多少宽度
        var lineWidthUsed = 0
        //每一行用了多少高度
        var lineHeight = 0
        //取出自己的宽度限制
        var widthMode = MeasureSpec.getMode(widthMeasureSpec)
        var widthSize = MeasureSpec.getSize(widthMeasureSpec)


        if (childBoundsList.isEmpty()) {
            for (index in 0 until childCount) {
                childBoundsList.add(Rect())
            }
        }
        for (x in 0 until childCount) {
            val child = getChildAt(x)
            //先测量一次 算一下这个child的 measureWidth
            measureChildWithMargins(
                child,
                widthMeasureSpec,
                0,
                heightMeasureSpec,
                heightUsed
            )
            //如果算出来的 宽度 比自己的宽度还大那就要重新测量 准备换行
            if (widthMode != MeasureSpec.UNSPECIFIED && lineWidthUsed + child.measuredWidth > widthSize) {
                //既然是重新测量了 那显然 每行已经用掉的宽度就是0了
                lineWidthUsed = 0
                //计算一下 已经用了多少高度了 因为既然换行了 heightUsed 就要增加了
                heightUsed += lineHeight
                measureChildWithMargins(
                    child,
                    widthMeasureSpec,
                    0,
                    heightMeasureSpec,
                    heightUsed
                )
            }
            //测量结束以后开始设置 bounds
            val childBounds = childBoundsList[x]
            //起点的left和top 很好理解 就是 这一行 已经用了多少 你就从这个位置开 layout
            // right和bottom 也就是加上自己的宽高 即可
            childBounds.set(
                lineWidthUsed,
                heightUsed,
                lineWidthUsed + child.measuredWidth,
                heightUsed + child.measuredHeight
            )
            //每一行已经用的 当然是加上这个child的宽度
            lineWidthUsed += child.measuredWidth
            //计算一下最大宽度 到时候自己要用
            widthUsed = Math.max(lineWidthUsed, widthUsed)
            //每一行的高度 就等于这一行里面 高度最大的那个view
            lineHeight = Math.max(lineHeight, child.measuredHeight)

        }

        //子view 都算出来了 那我自己也肯定就算出来了吧
        val measureWidth = widthUsed
        val measureHeight = (heightUsed + lineHeight)
        //算完了以后 直接调用这个方法 到这里测量就全部结束了
        setMeasuredDimension(measureWidth, measureHeight)

    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (childBoundsList.isNotEmpty()) {
            for (index in 0 until childCount step 1) {
                val child = getChildAt(index)
                val childBounds = childBoundsList[index]
                child.layout(
                    childBounds.left,
                    childBounds.top,
                    childBounds.right,
                    childBounds.bottom
                )
            }
        }
    }
}