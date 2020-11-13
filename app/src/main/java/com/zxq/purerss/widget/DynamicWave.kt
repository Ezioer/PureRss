package com.zxq.purerss.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.DrawFilter
import android.graphics.Paint
import android.graphics.PaintFlagsDrawFilter
import android.util.AttributeSet
import android.view.View
import com.zxq.purerss.utils.dp2px

/**
 * created by xiaoqing.zhou
 * on 2020/5/8
 * fun
 */
class DynamicWave(
    context: Context?,
    attrs: AttributeSet?
) : View(context, attrs) {
    private var mCycleFactorW = 0f
    private var mTotalWidth = 0
    private var mTotalHeight = 0
    private lateinit var mYPositions: FloatArray
    private lateinit var mResetOneYPositions: FloatArray
    private lateinit var mResetTwoYPositions: FloatArray
    private val mXOffsetSpeedOne: Int
    private val mXOffsetSpeedTwo: Int
    private var mXOneOffset = 0
    private var mXTwoOffset = 0
    private val mWavePaint: Paint
    private val mDrawFilter: DrawFilter
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 从canvas层面去除绘制时锯齿
        canvas.drawFilter = mDrawFilter
        resetPositonY()
        for (i in 0 until mTotalWidth) { // 减400只是为了控制波纹绘制的y的在屏幕的位置，大家可以改成一个变量，然后动态改变这个变量，从而形成波纹上升下降效果
// 绘制第一条水波纹
            canvas.drawLine(
                i.toFloat(), mTotalHeight - mResetOneYPositions[i] - 400, i.toFloat(),
                mTotalHeight.toFloat(),
                mWavePaint
            )
            // 绘制第二条水波纹
            canvas.drawLine(
                i.toFloat(), mTotalHeight - mResetTwoYPositions[i] - 400, i.toFloat(),
                mTotalHeight.toFloat(),
                mWavePaint
            )
        }
        // 改变两条波纹的移动点
        mXOneOffset += mXOffsetSpeedOne
        mXTwoOffset += mXOffsetSpeedTwo
        // 如果已经移动到结尾处，则重头记录
        if (mXOneOffset >= mTotalWidth) {
            mXOneOffset = 0
        }
        if (mXTwoOffset > mTotalWidth) {
            mXTwoOffset = 0
        }
        // 引发view重绘，一般可以考虑延迟20-30ms重绘，空出时间片
        postInvalidate()
    }

    private fun resetPositonY() { // mXOneOffset代表当前第一条水波纹要移动的距离
        val yOneInterval = mYPositions.size - mXOneOffset
        // 使用System.arraycopy方式重新填充第一条波纹的数据
        System.arraycopy(mYPositions, mXOneOffset, mResetOneYPositions, 0, yOneInterval)
        System.arraycopy(mYPositions, 0, mResetOneYPositions, yOneInterval, mXOneOffset)
        val yTwoInterval = mYPositions.size - mXTwoOffset
        System.arraycopy(
            mYPositions, mXTwoOffset, mResetTwoYPositions, 0,
            yTwoInterval
        )
        System.arraycopy(mYPositions, 0, mResetTwoYPositions, yTwoInterval, mXTwoOffset)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // 记录下view的宽高
        mTotalWidth = w
        mTotalHeight = h
        // 用于保存原始波纹的y值
        mYPositions = FloatArray(mTotalWidth)
        // 用于保存波纹一的y值
        mResetOneYPositions = FloatArray(mTotalWidth)
        // 用于保存波纹二的y值
        mResetTwoYPositions = FloatArray(mTotalWidth)
        // 将周期定为view总宽度
        mCycleFactorW = (2 * Math.PI / mTotalWidth).toFloat()
        // 根据view总宽度得出所有对应的y值
        for (i in 0 until mTotalWidth) {
            mYPositions[i] =
                (STRETCH_FACTOR_A * Math.sin(mCycleFactorW * i.toDouble()) + OFFSET_Y).toFloat()
        }
    }

    companion object {
        var colorIndex = (0..8).random()

        // 波纹颜色
//        var WAVE_PAINT_COLOR = colors[colorIndex]
        // y = Asin(wx+b)+h
        private const val STRETCH_FACTOR_A = 40f
        private var OFFSET_Y = 0

        // 第一条水波移动速度
        private const val TRANSLATE_X_SPEED_ONE = 4f

        // 第二条水波移动速度
        private const val TRANSLATE_X_SPEED_TWO = 2f
    }

    init {
        // 将dp转化为px，用于控制不同分辨率上移动速度基本一致
        mXOffsetSpeedOne = dp2px(TRANSLATE_X_SPEED_ONE)
        mXOffsetSpeedTwo = dp2px(TRANSLATE_X_SPEED_TWO)
        OFFSET_Y = dp2px(160f)
        // 初始绘制波纹的画笔
        mWavePaint = Paint()
        // 去除画笔锯齿
        mWavePaint.isAntiAlias = true
        // 设置风格为实线
        mWavePaint.style = Paint.Style.FILL
        // 设置画笔颜色
//        mWavePaint.color = Color.parseColor(WAVE_PAINT_COLOR)
        mDrawFilter = PaintFlagsDrawFilter(
            0,
            Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG
        )
    }
}