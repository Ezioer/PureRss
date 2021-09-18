package com.zxq.purerss.ui.wxboom

import android.animation.ValueAnimator
import android.graphics.PathMeasure
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zxq.purerss.R
import java.lang.Exception
import java.nio.file.Path
import java.util.*

/**
 *  created by xiaoqing.zhou
 *  on 2021/9/15
 *  fun
 */
class WxBoomAdapter : BaseDelegateMultiAdapter<ImMsgInfo?, BaseViewHolder>() {
    companion object {
        private const val TAG = "WxBoomAdapter"
        private const val TYPE_LEFT = 0
        private const val TYPE_RIGHT = 1
    }

    init {
        setMultiTypeDelegate(object : BaseMultiTypeDelegate<ImMsgInfo?>() {
            override fun getItemType(data: List<ImMsgInfo?>, position: Int): Int {
                return if (data[position]?.type == 0) {
                    TYPE_LEFT
                } else {
                    TYPE_RIGHT
                }
            }
        })
        Objects.requireNonNull(getMultiTypeDelegate())!!
            .addItemType(TYPE_LEFT, R.layout.item_chat_left)
            .addItemType(TYPE_RIGHT, R.layout.item_chat_right)
    }

    override fun convert(holder: BaseViewHolder, item: ImMsgInfo?) {
        if (holder.itemViewType == TYPE_LEFT) {
            holder.setText(R.id.tv_left_content, item?.content)
        } else {
            holder.setText(R.id.tv_right_content, item?.content)
        }
    }
}