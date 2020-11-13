package com.zxq.purerss.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.Point
import android.os.Handler
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.animation.doOnEnd
import com.zxq.purerss.utils.dp2px
import com.zxq.purerss.utils.sp2px
import java.util.*


/**
 *  created by xiaoqing.zhou
 *  on 2020/6/8
 *  fun
 */
class HeadBubbleView(context: Context, attributeSet: AttributeSet?) :
    FrameLayout(context, attributeSet) {
    private var mContext: Context = context
    private var mWidth = dp2px(22f)
    private var mHeight = dp2px(22f)
    private var mParentHeight = dp2px(130f)
    private var mParentWidth = dp2px(300f)

    private var position = 0
    private var mMarginLeft = dp2px(12f)
    private var mMarginBottom = dp2px(12f)
    private lateinit var mTempImageview: ImageView
    private lateinit var mTextView: TextView
    private lateinit var mPicBean: List<Int>

    private var pos: FloatArray
    private var tan: FloatArray

    private var pointOne: Point
    private var pointTwo: Point
    private lateinit var handle: Handler

    init {
        pos = FloatArray(2)
        tan = FloatArray(2)
        pointOne = Point()
        pointTwo = Point()
        initView()
        createAniView()
        initHandle()
    }

    private fun initHandle() {
        handle = Handler()
        handle.postDelayed(object : Runnable {
            override fun run() {
                createAniView()
                handle.postDelayed(this, 1000)
            }
        }, 1000)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var height = MeasureSpec.getSize(heightMeasureSpec)
        var width = MeasureSpec.getSize(widthMeasureSpec)
        var model = MeasureSpec.getMode(heightMeasureSpec)
        var widthModel = MeasureSpec.getMode(widthMeasureSpec)
        if (model == MeasureSpec.AT_MOST) {
            height = mParentHeight
        }
        if (widthModel == MeasureSpec.AT_MOST) {
            width = mParentWidth
        }
        setMeasuredDimension(width, height)
    }

    private fun initView() {
//        mPicBean = listOf(R.drawable.desk, R.drawable.pp)
        mTempImageview = getImageView()
        mTextView = getTextView()
        initData(mTempImageview)
    }

    private fun initData(imageview: ImageView) {
        var data = mPicBean[position]
        imageview.setImageResource(data)
        mTextView.text = "xx来过"
    }

    private fun createAniView(): Boolean {
        var imageview = getImageView()
        imageview.scaleX = 0f
        imageview.scaleY = 0f
        initData(imageview)
        startScaleAnimation(imageview)
        return false
    }

    private fun startScaleAnimation(imageview: ImageView) {
        val anima = ValueAnimator.ofFloat(0.0f, 1f)
        anima.setDuration(800)
        anima.addUpdateListener {
            val value = it.animatedValue as Float
            imageview.scaleX = 0.1f + value
            imageview.scaleY = 0.1f + value
        }
        anima.doOnEnd {
            if (position == mPicBean.size - 1) {
                position = 0
            } else {
                position++
            }
            mTempImageview.setImageResource(mPicBean[position])
            setPathAnimation(imageview)
        }
        anima.start()
    }

    private fun setPathAnimation(imageview: ImageView) {
        var path: Path
        val random = (0..10).random()
        if (random % 2 == 0) {
            path = createLeftPath()
        } else {
            path = createRightPath()
        }
        val pathMeasure = PathMeasure(path, false)
        val valueAnima = ValueAnimator.ofFloat(1.0f, 0f)
        valueAnima.setDuration(2000)
        valueAnima.interpolator = LinearInterpolator()
        valueAnima.addUpdateListener {
            val value = it.getAnimatedValue() as Float
            val length = (pathMeasure.length * value)
            pathMeasure.getPosTan(length, pos, tan)
            imageview.translationX = pos[0]
            imageview.translationY = pos[1]
            imageview.alpha = value
            if (value > 0.5f) {
                imageview.scaleX = 0.2f + value
                imageview.scaleY = 0.2f + value
            }
        }
        valueAnima.doOnEnd {
            removeView(imageview)
        }
        valueAnima.start()
    }

    private fun createLeftPath(): Path {
        var path = Path()
        val nextP = Random().nextFloat()
        path.moveTo(nextP, -height * 1.0f / 1.8f)
        pointOne.x = -mWidth
        pointOne.y = -mHeight / 5
        pointTwo.x = -(mWidth + mMarginLeft / 2)
        pointTwo.y = (-height * 0.15).toInt()
        path.cubicTo(
            pointOne.x.toFloat(),
            pointOne.y.toFloat(),
            pointTwo.x.toFloat(),
            pointTwo.y.toFloat(),
            0f,
            0f
        )
        return path
    }

    private fun createRightPath(): Path {
        var path = Path()
        val nextF = Random().nextFloat()
        path.moveTo(nextF, -height * 1.0f / 1.8f)
        pointOne.x = mWidth
        pointOne.y = -mHeight / 5
        pointTwo.x = mWidth + mWidth / 2
        pointTwo.y = (-height * 0.15).toInt()
        path.cubicTo(
            pointOne.x.toFloat(),
            pointOne.y.toFloat(),
            pointTwo.x.toFloat(),
            pointTwo.y.toFloat(),
            0f,
            0f
        )
        return path
    }

    private fun getTextView(): TextView {
        var layout =
            LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layout.gravity = Gravity.BOTTOM or Gravity.END
        layout.setMargins(0, 0, dp2px(34f), dp2px(12f))
        mTextView = TextView(mContext)
        mTextView.textSize = sp2px(6f).toFloat()
        mTextView.setTextColor(Color.GREEN)
        addView(mTextView, layout)
        return mTextView
    }

    private fun getImageView(): ImageView {
        var layout = LayoutParams(mWidth, mHeight, Gravity.BOTTOM or Gravity.END)
        var mImageview = ImageView(mContext)
        mImageview.scaleType = ImageView.ScaleType.FIT_XY
        layout.setMargins(0, 0, mMarginLeft, mMarginBottom)
        addView(mImageview, layout)
        return mImageview
    }
}