package com.zxq.purerss.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.ViewGroup
import com.zxq.purerss.R
import com.zxq.purerss.utils.PixelUtil
import com.zxq.purerss.utils.dp2px
import kotlin.math.min

class PieLayoutX(
    context: Context, attributeSet: AttributeSet?
) : ViewGroup(context, attributeSet) {
    //每一个image的间隔
    private var mInterval = 0

    //默认显示的个数,有可能一张图片显示不全
    private var mDisPlayCount = 2.5f

    //每个子view的宽高
    private var mEveryWidth = 0f
    private var mEveryHeight = 0f

    //宽高比
    private var mHRatio = 1.4f

    //移动放大量
    private var mScaleSize = 0.36f

    //存储每个view的位置
    private var mIndexList = mutableListOf<Int>()

    //滑动的最大距离,从正常状态滑到最左边的放大状态
    private var mDisMax = 0f

    init {
        initArray(context, attributeSet)
        setWillNotDraw(false)
    }

    private fun initArray(
        context: Context,
        attributeSet: AttributeSet?
    ) {
        var array = context.obtainStyledAttributes(attributeSet, R.styleable.InnerRules)
        mInterval = array.getDimension(R.styleable.PieLayoutX_pieInterval, 12f).toInt()
        mDisPlayCount = array.getFloat(R.styleable.PieLayoutX_pieCount, 2.5f)
        mHRatio = array.getFloat(R.styleable.PieLayoutX_pieHRatio, 1.4f)
        mScaleSize = array.getFloat(R.styleable.PieLayoutX_pieScaleSize, 0.36f)
        array.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var width = getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        mEveryWidth = (width - paddingLeft - paddingRight - mInterval * 8) / mDisPlayCount
        mEveryHeight = mEveryWidth * mHRatio
        setMeasuredDimension(width, (mEveryHeight * (1 + mScaleSize) + top + bottom).toInt())

        //添加每个view的坐标位置
        if (mIndexList.size == 0) {
            mIndexList.add(0)
            var pos2 = mInterval
            mIndexList.add(pos2)
            var pos3 = mInterval * 2
            mIndexList.add(pos3)

            var pos4 = pos3 + mEveryWidth * (1 + mScaleSize) + mInterval
            mIndexList.add(pos4.toInt())

            var pos5 = pos4 + mEveryWidth + mInterval
            mIndexList.add(pos5.toInt())

            var pos6 = pos5 + mEveryWidth + mInterval
            mIndexList.add(pos6.toInt())
            var pos7 = pos6 + mEveryWidth + mInterval
            mIndexList.add(pos7.toInt())
            var pos8 = pos7 + mEveryWidth + mInterval
            mIndexList.add(pos8.toInt())
            mDisMax = pos4 - pos3
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)

    }
}