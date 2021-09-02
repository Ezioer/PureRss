package com.zxq.purerss.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.OvershootInterpolator
import com.zxq.purerss.R
import com.zxq.purerss.utils.PixelUtil


class PraiseView @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null
    , def: Int = 0
) : View(context, attributeSet, def), View.OnClickListener {
    private var mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var isPraise = false
    private var mCenterX = 0
    private var mCenterY = 0

    private var mPraiseBitmap: Bitmap
    private var mNotPraiseBitmap: Bitmap
    private var mShiningBitmap: Bitmap

    private var mScale = 0f
    private var mScaleMax = 1f
    private var mScaleMin = 0.9f
    private var mScaleDuration = 150L

    private var mClipPath: Path
    private var mRadiusMax: Float = 0f
    private var mRadiusMin: Float = 0f
    private var mCircleX = 0f
    private var mCircleY = 0f

    private var mRadius = 0f
    private var nums: MutableList<String>
    private var offerMax: Float
    private var oldOffer: Float = 0f


    //num[0]是不变的部分，nums[1]变化前的部分，nums[2]变化后的部分
    private var count: Int = 12

    private val START_COLOR = Color.parseColor("#00e24d3d")
    private val END_COLOR = Color.parseColor("#88e24d3d")
    private var mNewOffer: Float

    //文本颜色
    private val TEXT_DEFAULT_COLOR = Color.parseColor("#cccccc")
    private val TEXT_DEFAULT_END_COLOR = Color.parseColor("#00cccccc")

    private var mIsChangeBig = false

    init {
        mPraiseBitmap =
            BitmapFactory.decodeResource(resources, R.drawable.ic_messages_like_selected)
        mNotPraiseBitmap =
            BitmapFactory.decodeResource(resources, R.drawable.ic_messages_like_unselected)
        mShiningBitmap =
            BitmapFactory.decodeResource(resources, R.drawable.ic_messages_like_selected_shining)
        setOnClickListener(this)
        mRadiusMax = PixelUtil.dp2px(context, 16f).toFloat()
        mRadiusMin = PixelUtil.dp2px(context, 8f).toFloat()
        mCircleX = mCenterX.toFloat() + mPraiseBitmap.width / 2
        mCircleY = mCenterY.toFloat() + mPraiseBitmap.height / 2
        mClipPath = Path()
        mClipPath.addCircle(mCircleX, mCircleY, mRadiusMax, Path.Direction.CW)
        mCirclePaint.style = Paint.Style.STROKE
        mCirclePaint.strokeWidth = PixelUtil.dp2px(context, 2f).toFloat()
        mCirclePaint.setColor(START_COLOR)
        mTextPaint.textSize = PixelUtil.sp2px(context, 15f).toFloat()
        mTextPaint.setColor(TEXT_DEFAULT_COLOR)
        nums = mutableListOf(count.toString(), "", "")
        offerMax = 1.5f * PixelUtil.sp2px(context, 15f).toFloat()
        mNewOffer = 0f
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCenterX = width / 2
        mCenterY = height / 2

        mCircleX = mCenterX.toFloat() + mPraiseBitmap.width / 2
        mCircleY = mCenterY.toFloat() + mPraiseBitmap.height / 2
    }

    private fun setNotPraiseScale(scale: Float) {
        mScale = scale
        var matrix = Matrix()
        matrix.postScale(mScale, mScale)
        mNotPraiseBitmap =
            BitmapFactory.decodeResource(resources, R.drawable.ic_messages_like_unselected)
        mNotPraiseBitmap = Bitmap.createBitmap(
            mNotPraiseBitmap, 0, 0, mNotPraiseBitmap.width, mNotPraiseBitmap.height, matrix
            , true
        )
        postInvalidate()
    }

    private fun getNotPraiseScale(): Float {
        return mScale
    }

    private fun setPraiseScale(scale: Float) {
        mScale = scale
        var matrix = Matrix()
        matrix.postScale(mScale, mScale)
        mPraiseBitmap =
            BitmapFactory.decodeResource(resources, R.drawable.ic_messages_like_selected)
        mPraiseBitmap = Bitmap.createBitmap(
            mPraiseBitmap, 0, 0, mPraiseBitmap.width, mPraiseBitmap.height, matrix
            , true
        )
        postInvalidate()
    }

    private fun getPraiseScale(): Float {
        return mScale
    }

    private fun setCircleScale(scale: Float) {
        mRadius = scale
        mClipPath = Path()
        mClipPath.addCircle(mCircleX, mCircleY, mRadius, Path.Direction.CW)
        val fraction: Float =
            (mRadiusMax - scale) / (mRadiusMax - mRadiusMin)
        mCirclePaint.color = evaluate(fraction, START_COLOR, END_COLOR) as Int
        postInvalidate()
    }

    private fun getCircleScale(): Float {
        return mRadiusMax
    }

    private fun setTextOfferY(offer: Float) {
        //变大 0 - 1，从下往上
        //变小 0 - -1 从上往下
        oldOffer = offer
        if (mIsChangeBig) {
            //newoffer慢慢变小 -1 - 0
            mNewOffer = offer - offerMax
        } else {
            //newoffer慢慢增大 1 - 0
            mNewOffer = offerMax + offer
        }
        postInvalidate()
    }

    private fun getTextOfferY(): Float {
        return 0f
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawIcon(canvas)
        drawText(canvas)
    }

    private fun calculateChange(changeValue: Int) {
        mIsChangeBig = changeValue > 0
        val old = count.toString()
        val new = (count + changeValue).toString()
        if (old.length != new.length) {
            //长度改变了，说明是位数改变了，则直接绘制改变前后的就行
            //例 9 - 10
            nums[0] = ""
            nums[1] = old
            nums[2] = new
        } else {
            //长度没变化，则位数没改变
            //此时还是有两种情况，一种是10 - 11，一种是19 - 20
            for (i in new.indices) {
                var oldChar = old[i]
                var newChar = new[i]
                if (oldChar != newChar) {
                    if (i == 0) {
                        //如果是在首位，则说明字符的内容都都发生了变化
                        nums[0] = ""
                    } else {
                        nums[0] = old.substring(0, i)
                    }
                    nums[1] = oldChar.toString()
                    nums[2] = newChar.toString()
                    break
                }
            }
        }
    }

    private fun drawText(canvas: Canvas?) {
        //绘制不变的部分
        mTextPaint.color = TEXT_DEFAULT_COLOR
        canvas?.drawText(
            nums[0],
            mCircleX + PixelUtil.dp2px(context, 10f),
            mCircleY + PixelUtil.dp2px(context, 8f), mTextPaint
        )

        val text = count.toString()
        val textWidth = mTextPaint.measureText(text) / text.length
        val fraction: Float =
            (offerMax - Math.abs(oldOffer)) / (offerMax - 0f)

        //绘制改变前的部分
        mTextPaint.color =
            (evaluate(fraction, TEXT_DEFAULT_END_COLOR, TEXT_DEFAULT_COLOR) as Int?)!!
        canvas!!.drawText(
            (nums[1]),
            mCircleX + PixelUtil.dp2px(context, 10f) + textWidth * nums[0].length,
            mCircleY + PixelUtil.dp2px(context, 8f) - oldOffer,
            mTextPaint
        )

        //绘制改变后的部分
        mTextPaint.color =
            (evaluate(fraction, TEXT_DEFAULT_COLOR, TEXT_DEFAULT_END_COLOR) as Int?)!!
        canvas!!.drawText(
            nums[2],
            mCircleX + PixelUtil.dp2px(context, 10f) + textWidth * nums[0].length,
            mCircleY + PixelUtil.dp2px(context, 8f) - mNewOffer,
            mTextPaint
        )
    }

    private fun drawIcon(canvas: Canvas?) {
        if (isPraise) {
            //已点赞
            if (mClipPath != null) {
                canvas?.save()
                //裁切路径区域，只在这一区域里才能进行绘制
                canvas?.clipPath(mClipPath)
                canvas?.drawBitmap(
                    mShiningBitmap,
                    mCenterX.toFloat(),
                    mCenterY.toFloat() - PixelUtil.dp2px(context, 8f),
                    mPaint
                )
                canvas?.restore()
                canvas?.drawCircle(mCircleX, mCircleY, mRadius, mCirclePaint)
            } else {
                canvas?.drawBitmap(
                    mShiningBitmap,
                    mCenterX.toFloat(),
                    mCenterY.toFloat() - PixelUtil.dp2px(context, 8f),
                    mPaint
                )
            }
            canvas?.drawBitmap(mPraiseBitmap, mCenterX.toFloat(), mCenterY.toFloat(), mPaint)
        } else {
            canvas?.drawBitmap(mNotPraiseBitmap, mCenterX.toFloat(), mCenterY.toFloat(), mPaint)
        }
    }

    override fun onClick(v: View?) {
        if (isPraise) {
            //取消点赞
            calculateChange(-1)
            count--
            showNotPraiseAni()
        } else {
            //点赞
            calculateChange(1)
            count++
            showPraiseAni()
        }
    }

    private fun showPraiseAni() {
        val notPraiseScaleAni = ObjectAnimator.ofFloat(this, "notPraiseScale", mScaleMax, mScaleMin)
        notPraiseScaleAni.setDuration(mScaleDuration)
        notPraiseScaleAni.addListener(object : ClickAnimatorListener() {
            override fun onAnimRealEnd(animation: Animator?) {
                isPraise = true
            }
        })

        val textAni = ObjectAnimator.ofFloat(this, "textOfferY", 0f, offerMax)
        textAni.setDuration(mScaleDuration + 100)

        val praiseScaleAni = ObjectAnimator.ofFloat(this, "praiseScale", mScaleMin, mScaleMax)
        praiseScaleAni.setDuration(mScaleDuration)
        praiseScaleAni.interpolator = OvershootInterpolator()

        val circleScaleAni = ObjectAnimator.ofFloat(this, "circleScale", mRadiusMin, mRadiusMax)
        circleScaleAni.setDuration(100)

        val aniSet = AnimatorSet()
        aniSet.play(praiseScaleAni).with(circleScaleAni)
        aniSet.play(textAni).with(notPraiseScaleAni)
        aniSet.play(praiseScaleAni).after(notPraiseScaleAni)
        aniSet.start()
    }

    private fun showNotPraiseAni() {
        val notPraiseScaleAni = ObjectAnimator.ofFloat(this, "praiseScale", mScaleMax, mScaleMin)
        notPraiseScaleAni.setDuration(mScaleDuration)
        notPraiseScaleAni.addListener(object : ClickAnimatorListener() {
            override fun onAnimRealEnd(animation: Animator?) {
                isPraise = false
            }
        })

        val textAni = ObjectAnimator.ofFloat(this, "textOfferY", 0f, -offerMax)
        textAni.setDuration(mScaleDuration + 100)

        val praiseScaleAni = ObjectAnimator.ofFloat(this, "notPraiseScale", mScaleMin, mScaleMax)
        praiseScaleAni.setDuration(mScaleDuration)

        val circleScaleAni = ObjectAnimator.ofFloat(this, "circleScale", mRadiusMax, mRadiusMin)
        circleScaleAni.setDuration(100)

        val aniSet = AnimatorSet()
        aniSet.play(notPraiseScaleAni).with(circleScaleAni).with(textAni)
        //先执行after动画再执行前面动画，before先执行前面动画再执行before动画
        aniSet.play(praiseScaleAni)
        aniSet.start()
    }


    private abstract class ClickAnimatorListener : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator?) {
            super.onAnimationStart(animation)

        }

        override fun onAnimationEnd(animation: Animator?) {
            super.onAnimationEnd(animation)
            onAnimRealEnd(animation)
        }

        override fun onAnimationCancel(animation: Animator?) {
            super.onAnimationCancel(animation)
        }

        abstract fun onAnimRealEnd(animation: Animator?)
    }

    //值从end 变化到start
    fun evaluate(
        fraction: Float,
        startValue: Any,
        endValue: Any
    ): Any? {
        val startInt = startValue as Int
        val startA = (startInt shr 24 and 0xff) / 255.0f
        var startR = (startInt shr 16 and 0xff) / 255.0f
        var startG = (startInt shr 8 and 0xff) / 255.0f
        var startB = (startInt and 0xff) / 255.0f
        val endInt = endValue as Int
        val endA = (endInt shr 24 and 0xff) / 255.0f
        var endR = (endInt shr 16 and 0xff) / 255.0f
        var endG = (endInt shr 8 and 0xff) / 255.0f
        var endB = (endInt and 0xff) / 255.0f

        // convert from sRGB to linear
        startR = Math.pow(startR.toDouble(), 2.2).toFloat()
        startG = Math.pow(startG.toDouble(), 2.2).toFloat()
        startB = Math.pow(startB.toDouble(), 2.2).toFloat()
        endR = Math.pow(endR.toDouble(), 2.2).toFloat()
        endG = Math.pow(endG.toDouble(), 2.2).toFloat()
        endB = Math.pow(endB.toDouble(), 2.2).toFloat()

        // compute the interpolated color in linear space
        var a = startA + fraction * (endA - startA)
        var r = startR + fraction * (endR - startR)
        var g = startG + fraction * (endG - startG)
        var b = startB + fraction * (endB - startB)

        // convert back to sRGB in the [0..255] range
        a = a * 255.0f
        r = Math.pow(r.toDouble(), 1.0 / 2.2).toFloat() * 255.0f
        g = Math.pow(g.toDouble(), 1.0 / 2.2).toFloat() * 255.0f
        b = Math.pow(b.toDouble(), 1.0 / 2.2).toFloat() * 255.0f
        return Math.round(a) shl 24 or (Math.round(r) shl 16) or (Math.round(
            g
        ) shl 8) or Math.round(b)
    }
}