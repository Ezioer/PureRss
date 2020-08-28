package com.zxq.purerss.widget.slidepiccode

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.util.AttributeSet
import android.view.View
import com.zxq.purerss.R
import kotlin.properties.Delegates
import kotlin.random.Random

/**
 *  created by xiaoqing.zhou
 *  on 2020/7/9
 *  fun
 */
class Puzzle(context: Context, attributeSet: AttributeSet?) : View(context, attributeSet) {
    private var mProgress = 0f
    private var isShowAnim = false
    private var path by Delegates.notNull<Path>()
    private lateinit var rect: Rect
    private lateinit var mBitmap: Bitmap
    private lateinit var mBackgroundBitmap: Bitmap
    private lateinit var mPuzzleBitmap: Bitmap
    private lateinit var mShadowPaint: Paint
    private lateinit var mWhitePaint: Paint
    private lateinit var mPuzzlePaint: Paint

    private var mTranslateX = 0f
    private var mTranslateY = 0f
    private var valueAnimator: ValueAnimator? = null
    private lateinit var mSuccessPaint: Paint
    private lateinit var linearGradient: LinearGradient
    private lateinit var mGradientMatrix: Matrix

    init {
        path = Path()
        rect = Rect()
        mGradientMatrix = Matrix()
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        mWhitePaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
            color = Color.WHITE
            style = Paint.Style.STROKE
            strokeWidth = 1f
        }
        mShadowPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
            color = 0x77000000
            maskFilter = BlurMaskFilter(20f, BlurMaskFilter.Blur.SOLID)
        }
        mPuzzlePaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
            color = Color.WHITE
            maskFilter = BlurMaskFilter(5f, BlurMaskFilter.Blur.NORMAL)
        }
        mSuccessPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
        initValueAnimator()
    }

    private fun initValueAnimator() {
        valueAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 1000
            addUpdateListener {
                val progress = it.getAnimatedValue() as Float
                mTranslateX = 4 * mWidth * progress - 2 * mWidth
                mTranslateY = mHeight * progress
                mGradientMatrix.setTranslate(mTranslateX, mTranslateY)
                linearGradient.setLocalMatrix(mGradientMatrix)
                invalidate()
            }
        }
    }

    fun showSuccess() {
        isShowAnim = true
        valueAnimator?.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (isShowAnim) {
            //验证成功
            canvas.save()
            //绘制成功后完整图片和闪光动画
            canvas.drawBitmap(mBitmap, 0f, 0f, null)
            canvas.drawRect(rect, mSuccessPaint)
            canvas.restore()
        } else {
            //绘制带缺口的图片和滑块
            canvas.save()
            mBackgroundBitmap?.let { canvas.drawBitmap(it, 0f, 0f, null) }
            canvas.restore()
            canvas.save()
            //绘制滑块
            mPuzzleBitmap?.let {
                canvas.drawBitmap(
                    it,
                    -(randomX * mWidth) + mProgress * 0.9f * width,
                    0f,
                    null
                )
            }
            canvas.restore()
        }
    }

    fun setProgress(progress: Float) {
        mProgress = progress
        invalidate()
    }

    private fun getBackgroundBitmap(bitmap: Bitmap): Bitmap {
        val bgBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bgBitmap)
        canvas.save()
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        canvas.restore()
        canvas.save()
        canvas.drawPath(path, mShadowPaint)
        canvas.drawPath(path, mWhitePaint)
        canvas.restore()
        return bgBitmap
    }

    private fun getPuzzleBitmap(bitmap: Bitmap): Bitmap {
        val newBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newBitmap)
        canvas.save()
        canvas.drawPath(path, mPuzzlePaint)
        mPuzzlePaint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
        canvas.drawBitmap(bitmap, 0f, 0f, mPuzzlePaint)
        mPuzzlePaint.xfermode = null
        canvas.restore()
        canvas.drawPath(path, mWhitePaint)
        return newBitmap
    }

    //模糊Bitmap
    private fun getBlurBitmap(bitmap: Bitmap): Bitmap {
        val targetBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val rs = RenderScript.create(context)
        val blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
        val allIn = Allocation.createFromBitmap(rs, bitmap)
        val allOut = Allocation.createFromBitmap(rs, targetBitmap)
        blurScript.setRadius(10f)
        blurScript.setInput(allIn)
        blurScript.forEach(allOut)
        allOut.copyTo(targetBitmap)
        rs.destroy()
        return targetBitmap
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
        createPath()
        rect.set(0, 0, w, h)
        linearGradient = LinearGradient(
            mWidth.toFloat() / 2.toFloat(), 0f, 0f, mHeight.toFloat(),
            intArrayOf(Color.BLACK, Color.WHITE, Color.BLACK), null, Shader.TileMode.CLAMP
        )
        mSuccessPaint.shader = linearGradient
        mSuccessPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.LIGHTEN)
        mGradientMatrix.setTranslate(-2f * mWidth.toFloat(), mHeight.toFloat())
        linearGradient.setLocalMatrix(mGradientMatrix)

        mBitmap =
            resizeBitmap(
                BitmapFactory.decodeResource(resources, R.drawable.default_two),
                mWidth,
                mHeight
            )
        mBackgroundBitmap = getBackgroundBitmap(mBitmap)
        mPuzzleBitmap = getPuzzleBitmap(getBlurBitmap(mBitmap))
    }

    fun getRandomX() = randomX

    private var randomX = 0f
    private var randomY = 0f
    private var mWidth by Delegates.notNull<Int>()
    private var mHeight by Delegates.notNull<Int>()
    private fun createPath() {
        randomX = randomFloat(0.5f, 0.8f)
        randomY = randomFloat(0.1f, 0.3f)
        val gapArray = randomGap()
        val sideLength = if (0.1f * mWidth >= 0.1f * mHeight) {
            0.1f * mWidth
        } else {
            0.1f * mHeight
        }
        path.moveTo(randomX * mWidth, randomY * mHeight)
        path.lineTo(randomX * mWidth + 0.2f * sideLength, randomY * mHeight)
        if (1 in gapArray) {
            path.arcTo(
                RectF(
                    randomX * mWidth + 0.2f * sideLength, randomY * mHeight - 0.2f * sideLength,
                    randomX * mWidth + 0.6f * sideLength, randomY * mHeight + 0.2f * sideLength
                ), 180f, randomDirection()
            )
        }
        path.lineTo(randomX * mWidth + sideLength, randomY * mHeight)
        path.lineTo(randomX * mWidth + sideLength, randomY * mHeight + 0.2f * sideLength)
        if (2 in gapArray) {
            path.arcTo(
                RectF(
                    randomX * mWidth + 0.8f * sideLength, randomY * mHeight + 0.2f * sideLength,
                    randomX * mWidth + 1.2f * sideLength, randomY * mHeight + 0.6f * sideLength
                ), 270f, randomDirection()
            )
        }
        path.lineTo(randomX * mWidth + sideLength, randomY * mHeight + sideLength)
        path.lineTo(randomX * mWidth + 0.6f * sideLength, randomY * mHeight + sideLength)
        if (3 in gapArray) {
            path.arcTo(
                RectF(
                    randomX * mWidth + 0.2f * sideLength, randomY * mHeight + 0.8f * sideLength,
                    randomX * mWidth + 0.6f * sideLength, randomY * mHeight + 1.2f * sideLength
                ), 0f, randomDirection()
            )
        }
        path.lineTo(randomX * mWidth, randomY * mHeight + sideLength)
        path.lineTo(randomX * mWidth, randomY * mHeight + 0.8f * sideLength)
        if (4 in gapArray) {
            path.arcTo(
                RectF(
                    randomX * mWidth - 0.2f * sideLength, randomY * mHeight + 0.4f * sideLength,
                    randomX * mWidth + 0.2f * sideLength, randomY * mHeight + 0.8f * sideLength
                ), 90f, randomDirection()
            )
        }
        path.close()
    }

    fun randomFloat(min: Float, max: Float): Float {
        if (min > max) throw IllegalArgumentException("min must less-then max")
        return min + Random.nextFloat() * (max - min)
    }

    private fun randomDirection(): Float {
        return if (Random.nextInt(0, 2) == 0) {
            -180f
        } else {
            180f
        }
    }

    private fun randomGap(): Array<Int?> {
        val list = arrayOfNulls<Int>(Random.nextInt(2, 5))
        for (i in list.indices) {
            list[i] = Random.nextInt(1, 5)
        }
        return list
    }

    fun resizeBitmap(bitmap: Bitmap, w: Int, h: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val scaleWidth = w.toFloat() / width
        val scaleHeight = h.toFloat() / height
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        return Bitmap.createBitmap(
            bitmap, 0, 0, width,
            height, matrix, true
        )
    }
}