package com.zxq.purerss.widget

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.*
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.widget.FrameLayout
import com.zxq.purerss.R
import kotlin.math.abs

class PieLayoutX(
    context: Context, attributeSet: AttributeSet?
) : ViewGroup(context, attributeSet) {
    //每一个image的间隔
    private var mInterval = 0

    //默认显示的个数,有可能一张图片显示不全
    private var mDisPlayCount = 2.5f

    //每个子view的宽高
    private var mEveryWidth = 0
    private var mEveryHeight = 0

    //宽高比
    private var mHRatio = 1.4f

    //移动放大量
    private var mScaleSize = 0.36f

    //存储每个view的位置
    private var mIndexList = mutableListOf<Int>()


    private var mDownX = 0
    private var mDownY = 0
    private var mLastX = 0f
    private var mScrollState = 0
    private val MODE_IDLE = 0
    private val MODE_HORIZONTAL = 1
    private val MODE_VERTICAL = 2
    private var mTouchSlop: Int = 0
    private var mMaximumVelocity = 0
    private val VELOCITY_THRESHOLD = 200

    //滑动的最大距离,从正常状态滑到最左边的放大状态
    private var scrollDistanceMax = 0 // 滑动参考值

    private var mTrack: VelocityTracker? = null

    private lateinit var mAdapter: Adapter
    private var mIsSetAdapter = false

    private var animateValue = 0f
    private var animator: ObjectAnimator? = null
    private var interpolator: Interpolator = DecelerateInterpolator(1.6f)
    private var animatingView: FrameLayout? = null

    init {
        initArray(context, attributeSet)
        setWillNotDraw(false)
    }

    private fun initArray(
        context: Context,
        attributeSet: AttributeSet?
    ) {
        var array = context.obtainStyledAttributes(attributeSet, R.styleable.InnerRules)
        mInterval = array.getDimension(R.styleable.PieLayoutX_pieInterval, 12f).toInt()
        mDisPlayCount = array.getFloat(R.styleable.PieLayoutX_pieCount, 2.5f)
        mHRatio = array.getFloat(R.styleable.PieLayoutX_pieHRatio, 1.4f)
        mScaleSize = array.getFloat(R.styleable.PieLayoutX_pieScaleSize, 0.36f)
        array.recycle()
        var config = ViewConfiguration.get(context)
        mTouchSlop = config.scaledTouchSlop
        mMaximumVelocity = config.scaledMaximumFlingVelocity
        viewTreeObserver.addOnGlobalLayoutListener {
            if (height > 0 && null != mAdapter && !mIsSetAdapter) {
                setAdapter(mAdapter)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var width = getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        mEveryWidth = ((width - paddingLeft - paddingRight - mInterval * 8) / mDisPlayCount).toInt()
        mEveryHeight = (mEveryWidth * mHRatio).toInt()
        setMeasuredDimension(width, (mEveryHeight * (1 + mScaleSize) + top + bottom).toInt())

        //添加每个view的坐标位置
        if (mIndexList.size == 0) {
            mIndexList.add(0)
            var pos1 = mInterval
            mIndexList.add(pos1)
            var pos2 = mInterval * 2
            mIndexList.add(pos2)
            var pos3 = mInterval * 3
            mIndexList.add(pos3)

            var pos4 = pos3 + mEveryWidth * (1 + mScaleSize) + mInterval
            mIndexList.add(pos4.toInt())

            var pos5 = pos4 + mEveryWidth + mInterval
            mIndexList.add(pos5.toInt())

            var pos6 = pos5 + mEveryWidth + mInterval
            mIndexList.add(pos6.toInt())
            var pos7 = pos6 + mEveryWidth + mInterval
            mIndexList.add(pos7.toInt())
            var pos8 = pos7 + mEveryWidth + mInterval
            mIndexList.add(pos8.toInt())
            scrollDistanceMax = (pos4 - pos3).toInt()

        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var count = childCount
        for (i in 0 until count) {
            var itemView = getChildAt(i)
            val left = mIndexList[i]
            var top = (measuredHeight - mEveryHeight) / 2
            var right = left + mEveryWidth
            var bottom = top + mEveryHeight
            itemView.layout(left, top.toInt(), right.toInt(), bottom.toInt())
            //当前view执行缩放动画时的默认轴点,以左边中点为缩放轴点
            itemView.pivotX = 0f
            itemView.pivotY = (mEveryHeight / 2).toFloat()
            setViewScale(itemView)
            setViewAlpha(itemView)
        }
    }

    //设置初始位置时的透明度
    private fun setViewAlpha(view: View) {
        var pos2 = mIndexList[2]
        if (view.left >= pos2) {
            view.alpha = 1f
        } else {
            val alpha = view.left / pos2 - mIndexList[0]
            view.alpha = alpha.toFloat()
        }
    }

    //设置初始位置时的缩放大小
    private fun setViewScale(view: View) {
        var scale = 1f
        var pos4 = mIndexList[4]
        var currentViewLeft = view.left
        if (currentViewLeft < pos4) {
            var pos3 = mIndexList[3]
            if (currentViewLeft > pos3) {
                // everyWidth * (1 + scaleStep) + interval
                scale = 1 + mScaleSize - mScaleSize * (currentViewLeft - pos3) / (pos4 - pos3)
            } else {
                //小于3的按顺序缩小
                var pos2 = mIndexList[2]
                scale = 1 + (currentViewLeft - pos2) * mScaleSize / mInterval
            }
        }
        //大于位置4的view不缩放
        view.scaleX = scale
        view.scaleY = scale
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        var action = ev.actionMasked
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                mDownX = ev.x.toInt()
                mDownY = ev.y.toInt()
                mLastX = ev.x
                mScrollState = MODE_IDLE
                if (null != animator) {
                    animator?.cancel()
                }

                if (mTrack == null) {
                    mTrack = VelocityTracker.obtain()
                }
                mTrack?.addMovement(ev)
                animatingView = null
            }

            MotionEvent.ACTION_MOVE -> {
                if (mScrollState == MODE_IDLE) {
                    var disX = abs(mDownX - ev.x)
                    var disY = abs(mDownY - ev.y)
                    if (disX > disY && disX > mMaximumVelocity) {
                        //水平滑动
                        mScrollState = MODE_HORIZONTAL
                        return true
                    } else {
                        //垂直滑动
                        mScrollState = MODE_VERTICAL
                    }
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (mTrack != null) {
                    mTrack?.recycle()
                    mTrack = null
                }
                onRelease(ev.x, 0)
            }
        }
        return false
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        mTrack?.addMovement(event)
        var action = event?.actionMasked
        when (action) {
            MotionEvent.ACTION_DOWN -> {

            }
            MotionEvent.ACTION_MOVE -> {
                var currentX = event.x
                var dis = currentX - mLastX
                handleSlide(dis.toInt())
                mLastX = currentX
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                //计算滑动的最大值
                mTrack?.computeCurrentVelocity(1000, mMaximumVelocity.toFloat())
                var velocityX = mTrack?.xVelocity?.toInt()
                if (mTrack != null) {
                    mTrack?.recycle()
                    mTrack = null
                }
                onRelease(event.x, velocityX ?: 0)
            }
        }
        return true
    }

    private fun handleSlide(dis: Int) {
        var dx = dis
        if (dx == 0) {
            return
        }

        val currentPosition = getChildAt(3).tag.toString().toInt()
        if (dx < 0 && currentPosition >= mAdapter?.itemCount) {
            return
        } else if (dx > 0) {
            if (currentPosition <= 0) {
                return
            } else if (currentPosition == 1) {
                if (getChildAt(3).left + dx >= mIndexList.get(4)) {
                    dx = mIndexList.get(4) - getChildAt(3).left
                }
            }
        }


        val num = childCount

        // 1. View循环复用

        // 1. View循环复用
        val firstView = getChildAt(0) as FrameLayout
        if (dx > 0 && firstView.left >= mIndexList.get(1)) {
            // 向右滑动，从左边把View补上
            val lastView = getChildAt(childCount - 1) as FrameLayout
            val lp = lastView.layoutParams
            removeViewInLayout(lastView)
            addViewInLayout(lastView, 0, lp)
            var tag = lastView.tag.toString().toInt()
            tag -= num
            lastView.tag = tag
            if (tag < 0) {
                lastView.visibility = INVISIBLE
            } else {
                lastView.visibility = VISIBLE
                mAdapter?.bindView(lastView.getChildAt(0), tag)
            }
        } else if (dx < 0 && firstView.left <= mIndexList.get(0)) {
            // 向左滑动，从右边把View补上
            val lp = firstView.layoutParams
            removeViewInLayout(firstView)
            addViewInLayout(firstView, -1, lp)
            var tag = firstView.tag.toString().toInt()
            tag += num
            firstView.tag = tag
            if (tag >= mAdapter?.itemCount) {
                firstView.visibility = INVISIBLE
            } else {
                firstView.visibility = VISIBLE
                mAdapter?.bindView(firstView.getChildAt(0), tag)
            }
        }

        // 2. 位置修正

        // 2. 位置修正
        val view3 = getChildAt(3)
        var rate: Float = (view3.left + dx - mIndexList.get(3)) as Float / scrollDistanceMax
        if (rate < 0) {
            rate = 0f
        }
        val position1: Int =
            Math.round(rate * (mIndexList.get(2) - mIndexList.get(1))) + mIndexList.get(1)
        var endAnim = false
        if (position1 >= mIndexList.get(2) && null != animatingView) {
            animator!!.cancel()
            endAnim = true
        }
        for (i in 0 until num) {
            val itemView = getChildAt(i)
            if (endAnim) {
                itemView.offsetLeftAndRight(mIndexList.get(i + 1) - itemView.left)
            } else if (itemView === animatingView) {
                itemView.offsetLeftAndRight(dx)
            } else {
                var position: Int =
                    Math.round(rate * (mIndexList.get(i + 1) - mIndexList.get(i))) + mIndexList.get(
                        i
                    )
                if (i + 1 < mIndexList.size && position >= mIndexList.get(i + 1)) {
                    position = mIndexList.get(i + 1)
                }
                itemView.offsetLeftAndRight(position - itemView.left)
            }
            setViewAlpha(itemView) // 调整透明度
            setViewScale(itemView) // 调整缩放
        }
    }

    private fun onRelease(x: Float, i: Int) {

    }

    fun setAdapter(adapter: Adapter) {
        mAdapter = adapter
        if (mEveryWidth > 0 && mEveryHeight > 0) {
            doBindAdapter()
        }
    }

    private fun doBindAdapter() {
        if (mAdapter == null) {
            return
        }
        if (mIsSetAdapter) {
            throw RuntimeException("only can set one adapter")
        }
        mIsSetAdapter = true
        if (childCount == 0) {
            var layoutInflater = LayoutInflater.from(context)
            for (i in 0 until 6) {
                var frameLayout = FrameLayout(context)
                var view = layoutInflater.inflate(mAdapter.layoutId, null)
                var lp1 = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
                lp1.width = mEveryWidth
                lp1.height = mEveryHeight
                frameLayout.addView(view, lp1)
                var lp2 = LayoutParams(mEveryWidth, mEveryHeight)
                lp2.width = mEveryWidth
                lp2.height = mEveryHeight
                frameLayout.layoutParams = lp2
                addView(frameLayout)
                frameLayout.tag = i - 3
                frameLayout.measure(mEveryWidth, mEveryHeight)
            }
        }

        var count = childCount
        for (i in 0 until count) {
            if (i < 3) {
                getChildAt(i).visibility = INVISIBLE
            } else {
                val frameLayout = getChildAt(i) as FrameLayout
                if (i - 3 < mAdapter.itemCount) {
                    frameLayout.visibility = VISIBLE
                    mAdapter.bindView(frameLayout.getChildAt(0), i - 3)
                } else {
                    frameLayout.visibility = INVISIBLE
                }
            }
        }

        if (mAdapter.itemCount > 0) {
            mAdapter.displaying(0)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)

    }

    fun setAnimateValue(animateValue: Float) {
        this.animateValue = animateValue // 当前应该在的位置
        var dx = Math.round(animateValue - animatingView!!.getLeft())
        handleSlide(dx)
    }

    fun getAnimateValue(): Float {
        return animateValue
    }

    /**
     * 适配器
     */
    abstract class Adapter {
        /**
         * layout文件ID，调用者必须实现
         */
        abstract val layoutId: Int

        /**
         * item数量，调用者必须实现
         */
        abstract val itemCount: Int

        /**
         * View与数据绑定回调，可重载
         */
        abstract fun bindView(view: View, index: Int)

        /**
         * 正在展示的回调，可重载
         */
        abstract fun displaying(position: Int)

        /**
         * item点击，可重载
         */
        abstract fun onItemClick(view: View, position: Int)
        abstract fun onMove()
        abstract fun getWidth(width: Int)
    }
}