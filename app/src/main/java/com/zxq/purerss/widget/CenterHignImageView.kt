package com.zxq.purerss.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class CenterHignImageView(context: Context, attributeSet: AttributeSet?) :
    View(context, attributeSet) {

    private lateinit var mPaint: Paint

    //去掉后面画的与前面重叠的部分
    private var porterMode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)

    init {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //先画中间高亮的圆角矩形
        canvas?.drawRoundRect(
            60f,
            60f,
            (width - 60).toFloat(),
            (height - 60).toFloat(),
            10f,
            10f,
            mPaint
        )

        mPaint.setColor(Color.parseColor("#99000000"))
        mPaint.xfermode = porterMode
        //画整个半透明的矩形
        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), mPaint)
    }
}