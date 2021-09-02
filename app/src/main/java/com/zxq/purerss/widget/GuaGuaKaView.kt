package com.zxq.purerss.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.zxq.purerss.R

/**
 *  created by xiaoqing.zhou
 *  on 2020/7/8
 *  fun
 */
class GuaGuaKaView(context: Context, attributeSet: AttributeSet?) : View(context, attributeSet) {

    private lateinit var mBgBitmap: Bitmap
    private lateinit var mMaskBitmap: Bitmap
    private lateinit var mTranPath: Path
    private var mTranPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private lateinit var mCanvas: Canvas

    init {
        mTranPaint.alpha = 0
        mTranPaint.style = Paint.Style.STROKE
        mTranPaint.strokeCap = Paint.Cap.ROUND
        mTranPaint.strokeJoin = Paint.Join.ROUND
        mTranPaint.strokeWidth = 50f
        mTranPaint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
        mTranPath = Path()

        mBgBitmap = BitmapFactory.decodeResource(resources, R.drawable.default_two)
        mMaskBitmap =
            Bitmap.createBitmap(mBgBitmap.width, mBgBitmap.height, Bitmap.Config.ARGB_8888)
        mCanvas = Canvas(mMaskBitmap)
        mCanvas.drawColor(Color.GRAY)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                mTranPath.reset()
                mTranPath.moveTo(event.getX(), event.getY())
            }
            MotionEvent.ACTION_MOVE -> {
                mTranPath.lineTo(event.getX(), event.getY())
            }
            MotionEvent.ACTION_UP -> {
            }
        }
        mCanvas.drawPath(mTranPath, mTranPaint)
        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawBitmap(mBgBitmap, 0f, 0f, null)
        canvas?.drawBitmap(mMaskBitmap, 0f, 0f, null)
        val paints = Paint()
        paints.style = Paint.Style.STROKE
        canvas?.save()
        //保存当前画布的设置，用于一些特殊的绘制，比如旋转画布等，重置后不影响接下来的正常绘制
        paints.pathEffect = DashPathEffect(floatArrayOf(1f, 1f), 0f)
        canvas?.drawCircle(0f, 0f, 10f, paints)
        canvas?.restore()
        //重置画布的设置为初始值
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.men)
        val bitmapShader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paints.setShader(bitmapShader)
        //绘制带圆角的bitmap
        canvas?.drawRoundRect(0f, 0f, 100f, 100f, 5f, 5f, paints)
    }
}