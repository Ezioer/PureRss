package com.zxq.purerss.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.zxq.purerss.utils.PixelUtil

class DiyTextView constructor(context: Context, attributeSet: AttributeSet, def: Int) :
    TextView(context, attributeSet, def) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var widthSize = MeasureSpec.getSize(widthMeasureSpec)
        var heightSize = MeasureSpec.getSize(heightMeasureSpec)
        if (heightMode == MeasureSpec.AT_MOST) {
            //at most---> wrapcontent   exactly----> 实际数值，matchparent子view会充满父view的宽度，一般不做改动
            //一般只需要处理wrapcontent的设置，这时如果是view，就按预期效果或者自定义一个值，如果是viewgroup
            //就按这个viewgroup的特性来设置实际的宽高
            heightSize = PixelUtil.dp2px(this.context, 100f)
        }

        if (widthMode == MeasureSpec.EXACTLY) {
            widthSize = PixelUtil.dp2px(this.context, 200f)
        }

        /* if (widthMode == MeasureSpec.AT_MOST){
             widthSize = PixelUtil.dp2px(this.context,200f)
         }*/
        setMeasuredDimension(widthSize, heightSize)
    }
}