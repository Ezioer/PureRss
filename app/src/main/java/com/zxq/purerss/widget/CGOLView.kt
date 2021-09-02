package com.zxq.purerss.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.zxq.purerss.utils.PixelUtil
import com.zxq.purerss.utils.putSpValue
import java.util.*
import kotlin.math.max

class CGOLView @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null
    , def: Int = 0
) : View(context, attributeSet, def) {
    private var mTablePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    //细胞的大小
    private var mBoxWidth = 40
    private var whiteColor = Color.WHITE
    private var blackColor = Color.BLACK
    private var mLineColor = Color.parseColor("#88e24d3d")
    private var mGene = 1
    private var mIsChange = false

    //总细胞个数
    private var mCellList: MutableList<CellInfo> = mutableListOf()

    init {
        mTablePaint.style = Paint.Style.FILL
        mLinePaint.color = mLineColor
        mLinePaint.strokeWidth = PixelUtil.dp2px(context, 1f).toFloat()
        initCells()
    }

    private fun initCells() {
        //初始化细胞
        for (k in 0 until (10 - 1)) {
            for (i in 0 until (10 - 1)) {
                mCellList.add(
                    CellInfo(
                        i + k * (10 - 1),
                        i * mBoxWidth + 4,
                        4 + k * (mBoxWidth),
                        (i + 1) * mBoxWidth - 4,
                        (k + 1) * mBoxWidth - 4,
                        0,
                        0,
                        mutableListOf()
                    )
                )
            }
        }

        for (i in 0 until 80) {
            mCellList[i].roundCell?.addAll(
                mutableListOf(
                    if (i - 1 < 0 || i - 1 == 8 || i - 1 == 17 || i - 1 == 26 || i - 1 == 35 || i - 1 == 44 || i - 1 == 53 || i - 1 == 62 || i - 1 == 71) null else mCellList[i - 1],
                    if (i - 9 >= 0) mCellList[i - 9] else null,
                    if (i + 1 == 9 || i + 1 == 18 || i + 1 == 27 || i + 1 == 36 || i + 1 == 45 || i + 1 == 54 || i + 1 == 63 || i + 1 == 72) null else mCellList[i + 1],
                    if (i >= 72) null else mCellList[i + 9],
                    if (i - 10 < 0 || i - 10 == 8 || i - 10 == 17 || i - 10 == 26 || i - 10 == 35 || i - 10 == 44 || i - 10 == 53 || i - 10 == 62 || i - 10 == 71) null else mCellList[i - 10],
                    if (i - 8 <= 0 || i - 8 == 9 || i - 8 == 18 || i - 8 == 27 || i - 8 == 36 || i - 8 == 45 || i - 8 == 54 || i - 8 == 63 || i - 8 == 72) null else mCellList[i - 8],
                    if (i + 6 == 8 || i + 6 == 17 || i + 6 == 26 || i + 6 == 35 || i + 6 == 44 || i + 6 == 53 || i + 6 == 62 || i + 6 == 71 || i + 6 > 80) null else mCellList[i + 6],
                    if (i + 10 == 18 || i + 10 == 27 || i + 10 == 36 || i + 10 == 45 || i + 10 == 54 || i + 10 == 63 || i + 10 == 72 || i + 10 > 80) null else mCellList[i + 10]
                )
            )
        }
    }

    //生命遊戲中，對於任意細胞，規則如下：
    //每個細胞有兩種狀態 - 存活或死亡，每個細胞與以自身為中心的周圍八格細胞產生互動（如圖，黑色為存活，白色為死亡）
    //當前細胞為存活狀態時，當周圍的存活細胞低於2個時（不包含2個），該細胞變成死亡狀態。（模擬生命數量稀少）
    //當前細胞為存活狀態時，當周圍有2個或3個存活細胞時，該細胞保持原樣。
    //當前細胞為存活狀態時，當周圍有超過3個存活細胞時，該細胞變成死亡狀態。（模擬生命數量過多）
    //當前細胞為死亡狀態時，當周圍有3個存活細胞時，該細胞變成存活狀態。（模擬繁殖）
    //可以把最初的細胞結構定義為種子，當所有在種子中的細胞同時被以上規則處理後，可以得到第一代細胞圖。按規則繼續處理當前的細胞圖，可以得到下一代的細胞圖，周而復始

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(getHeightValue(widthMeasureSpec), getHeightValue(heightMeasureSpec));
    }

    private fun getHeightValue(heightMeasureSpec: Int): Int {
        var result = 0
        val model = MeasureSpec.getMode(heightMeasureSpec)
        val size = MeasureSpec.getSize(heightMeasureSpec)
        if (model == MeasureSpec.AT_MOST) {
            result = mBoxWidth * (10 - 1)
        } else if (model == MeasureSpec.EXACTLY) {
            result = size
            result = max(result, mBoxWidth * (10 - 1))
        }
        return result
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawTable(canvas)
    }

    private fun drawTable(canvas: Canvas?) {
        for (i in 0 until 10) {
            canvas?.drawLine(
                0f, (i * mBoxWidth).toFloat(),
                (10 - 1) * mBoxWidth.toFloat(), (i * mBoxWidth).toFloat(), mLinePaint
            )
        }

        for (i in 0 until 10) {
            canvas?.drawLine(
                (i * mBoxWidth).toFloat(), 0f,
                (i * mBoxWidth).toFloat(), (10 - 1) * mBoxWidth.toFloat(), mLinePaint
            )
        }

        for (i in 0 until (10 - 1) * (10 - 1)) {
            val cell = mCellList[i]
            mTablePaint.color = if (cell.status == 1) blackColor else whiteColor
            canvas?.drawRoundRect(
                cell.left.toFloat(),
                cell.top.toFloat(),
                cell.right.toFloat(),
                cell.bottom.toFloat(),
                PixelUtil.dp2px(context, 2f).toFloat(),
                PixelUtil.dp2px(context, 2f).toFloat(),
                mTablePaint
            )
        }
    }

    fun setDieOrLive(index: Int, type: Int) {
        mCellList[index].status = type
        mCellList[index].originStatus = type
        postInvalidate()
    }

    fun startGame() {
        var time = Timer()
        time.schedule(object : TimerTask() {
            override fun run() {
                mCellList.forEach {
                    var filter = it.roundCell?.filter { it?.originStatus == 1 }
                    if (it.originStatus == 1) {
                        if (filter?.size ?: 0 < 2) {
                            it.status = 0
                            mIsChange = true
                        } else if (filter?.size ?: 0 == 2 || filter?.size ?: 0 == 3) {
                            it.status = 1
                        } else if (filter?.size ?: 0 > 3) {
                            it.status = 0
                            mIsChange = true
                        }
                    } else {
                        if (filter?.size ?: 0 == 3) {
                            it.status = 1
                            mIsChange = true
                        }
                    }
                }
                mCellList.forEach {
                    it.originStatus = it.status
                }
                mGene++
                if (mIsChange) {
                    listener?.invoke(mGene.toString())
                    postInvalidate()
                } else {
                    time.cancel()
                    context.putSpValue("record", mGene.toString())
                    recordListener?.invoke(mGene.toString())
                    listener?.invoke(0.toString())
                }
                mIsChange = false
            }
        }, 1000, 1000)
    }

    private var listener: ((count: String) -> Unit)? = null
    fun setListener(getNums: (String) -> Unit) {
        listener = getNums
    }

    private var recordListener: ((count: String) -> Unit)? = null
    fun setRecordListener(getNums: (String) -> Unit) {
        recordListener = getNums
    }
}