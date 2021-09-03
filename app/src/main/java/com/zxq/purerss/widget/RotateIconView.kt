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
        canvas?.translate(centerX.toFloat(), centerY.toFloat()) // 旋转之后把投影移动回来
        canvas?.rotate(-degreeZ)//转动270° 顺时针为正方向，此处为逆时针旋转，所以为负值
        camera?.rotateY(degreeY) // 旋转 Camera 的三维空间
        camera?.applyToCanvas(canvas) // 把旋转投影到 Canvas
        /*mPaint.color = Color.GREEN
        canvas?.drawRect(0f, -centerY.toFloat(), centerX.toFloat(), centerY.toFloat(), mPaint)*/
        canvas?.clipRect(0f, -centerY.toFloat(), centerX.toFloat(), centerY.toFloat())
        canvas?.rotate(degreeZ)
        canvas?.translate(-centerX.toFloat(), -centerY.toFloat()) // 旋转之前把绘制内容移动到轴心（原点）
        camera?.restore() // 恢复 Camera 的状态
        canvas?.drawBitmap(bitmap!!, bitmapX.toFloat(), bitmapY.toFloat(), mPaint)
        canvas?.restore()

        //画不变换的另一半
        canvas?.save()
        camera?.save()
        canvas?.translate(centerX.toFloat(), centerY.toFloat())
        canvas?.rotate(-degreeZ)
        //计算裁切参数时清注意，此时的canvas的坐标系已经移动
        canvas?.clipRect(-centerX, -centerY, 0, centerY)
        //此时的canvas的坐标系已经旋转，所以这里是rotateY
        camera?.rotateY(fixDegreeY)
        camera?.applyToCanvas(canvas)
        canvas?.rotate(degreeZ)
        canvas?.translate(-centerX.toFloat(), -centerY.toFloat())
        camera?.restore()
        canvas?.drawBitmap(bitmap!!, bitmapX.toFloat(), bitmapY.toFloat(), mPaint)
        canvas?.restore()
    }

    fun reset() {
        fixDegreeY = 0f
        degreeY = 0f
        fixDegreeY = 0f
        invalidate()
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