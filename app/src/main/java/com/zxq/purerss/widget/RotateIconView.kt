package com.zxq.purerss.widget

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.View
import com.zxq.purerss.R


class RotateIconView @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null
    , def: Int = 0
) : View(context, attributeSet, def) {
    private var mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var bitmap: Bitmap? = null
    private var camera: Camera? = null

    //Y轴方向旋转角度
    private var degreeY = 0f

    //不变的那一半，Y轴方向旋转角度
    private var fixDegreeY = 0f

    //Z轴方向（平面内）旋转的角度
    private var degreeZ = 0f

    init {
        var array = context.obtainStyledAttributes(attributeSet, R.styleable.RotateIcon)
        var drawableIcon = array.getDrawable(R.styleable.RotateIcon_icon_drawable) as BitmapDrawable
        array.recycle()
        bitmap = if (drawableIcon != null) {
            drawableIcon.bitmap
        } else {
            BitmapFactory.decodeResource(context.resources, R.drawable.flip_board)
        }

        camera = Camera()
        var dis = resources.displayMetrics
        var newZ = -dis.density * 6
        camera?.setLocation(0f, 0f, newZ)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var bitmapW = bitmap?.width
        var bitmapH = bitmap?.height
        var centerX = width / 2
        var centerY = height / 2
        var bitmapX = centerX - bitmapW!! / 2
        var bitmapY = centerY - bitmapH!! / 2
        canvas?.save()

        camera?.save() // 保存 Camera 的状态
        camera?.rotateY(45f) // 旋转 Camera 的三维空间
        canvas?.translate(centerX.toFloat(), centerY.toFloat()) // 旋转之后把投影移动回来
        camera?.applyToCanvas(canvas) // 把旋转投影到 Canvas
        canvas?.translate(-centerX.toFloat(), -centerY.toFloat()) // 旋转之前把绘制内容移动到轴心（原点）
        camera?.restore() // 恢复 Camera 的状态

        canvas?.drawBitmap(bitmap!!, bitmapX.toFloat(), bitmapY.toFloat(), mPaint)
        canvas?.restore()
    }

    fun setFixDegreeY(fixDegreeY: Float) {
        this.fixDegreeY = fixDegreeY
        invalidate()
    }

    fun setDegreeY(degreeY: Float) {
        this.degreeY = degreeY
        invalidate()
    }

    fun setDegreeZ(degreeZ: Float) {
        this.degreeZ = degreeZ
        invalidate()
    }
}