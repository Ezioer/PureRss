package com.zxq.purerss.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.zxq.purerss.R
import com.zxq.purerss.utils.dp2px

class RoundImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var mWidth = dp2px(100f)
    private var mHeight = dp2px(80f)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        var bitmapShader = BitmapShader(
            BitmapFactory.decodeResource(resources, R.drawable.bg),
            Shader.TileMode.CLAMP,
            Shader.TileMode.CLAMP
        )
        paint.shader = bitmapShader

        val border = dp2px(2f)
        var radius = dp2px(5f)
        val rect = RectF(
            border.toFloat(),
            border.toFloat(),
            (mWidth - border).toFloat(),
            (mHeight - border).toFloat()
        )
        canvas?.drawRoundRect(rect, radius.toFloat(), radius.toFloat(), paint)
        //绘制图片border
        var borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        borderPaint.setColor(Color.GREEN)
        borderPaint.strokeWidth = dp2px(1f).toFloat()
        borderPaint.style = Paint.Style.STROKE
        canvas?.drawRoundRect(rect, radius.toFloat(), radius.toFloat(), borderPaint)
        /* canvas?.drawBitmap(
             generateRoundBorderImage(
                 BitmapFactory.decodeResource(
                     resources,
                     R.drawable.bg
                 ), dp2px(50f), dp2px(40f)
             ), 0f, 0f, Paint(Paint.ANTI_ALIAS_FLAG)
         )*/
    }

    private fun generateRoundBorderImage(bitmap: Bitmap, width: Int, height: Int): Bitmap {

        var desBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        var canvas = Canvas(desBitmap)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        var bitmapShader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.shader = bitmapShader

        val border = dp2px(2f)
        var radius = dp2px(5f)
        val rect = RectF(
            border.toFloat(),
            border.toFloat(),
            (width - border).toFloat(),
            (height - border).toFloat()
        )
        canvas.drawRoundRect(rect, radius.toFloat(), radius.toFloat(), paint)
        return desBitmap
    }
}