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

class RuleGroupView(
    context: Context, attributeSet: AttributeSet?
) : ViewGroup(context, attributeSet) {

    private var mLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mRule: RulerView? = null
    var mLineColor = Color.parseColor("#282832")
    var mPointColor = Color.parseColor("#282832")
    private var mCenterX = 0
    private var mCenterY = 0

    init {
        initArray(context, attributeSet)
        mRule = RulerView(context, this)
        addView(mRule)
        setWillNotDraw(false)
    }

    private fun initArray(
        context: Context,
        attributeSet: AttributeSet?
    ) {
        var array = context.obtainStyledAttributes(attributeSet, R.styleable.InnerRules)
        mLineColor = array.getColor(R.styleable.InnerRules_lineColor, Color.parseColor("#282832"))
        mPointColor = array.getColor(R.styleable.InnerRules_pointColor, Color.parseColor("#282832"))
        array.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCenterX = width / 2
        mCenterY = height / 2
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        mRule?.layout(0, 0, r - l, b - t)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mLinePaint.color = mLineColor
        mLinePaint.strokeWidth = PixelUtil.dp2px(context, 1f).toFloat()
        canvas?.drawLine(0f, 0f, width.toFloat(), 0f, mLinePaint)
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        mLinePaint.color = mPointColor
        mLinePaint.strokeWidth = dp2px(2f).toFloat()
        canvas?.drawLine(mCenterX.toFloat(), 0f, mCenterX.toFloat(), mCenterY.toFloat(), mLinePaint)
    }

    fun setScaleCallback(call: RulerView.ScaleCallback) {
        mRule?.setCallback(call)
    }
}