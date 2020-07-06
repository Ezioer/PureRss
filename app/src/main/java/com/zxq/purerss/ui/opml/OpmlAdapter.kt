package com.zxq.purerss.ui.opml

import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zxq.purerss.R
import com.zxq.purerss.data.entity.RssItem
import com.zxq.purerss.data.entity.RssOpmlInfo
import com.zxq.purerss.databinding.ItemContentListBinding
import com.zxq.purerss.databinding.ItemOpmlBinding
import com.zxq.purerss.listener.ItemClickListener
import com.zxq.purerss.widget.SwipeMenuLayout

/**
 *  created by xiaoqing.zhou
 *  on 2020/6/28
 *  fun
 */
class OpmlAdapter :
    BaseQuickAdapter<RssOpmlInfo, BaseViewHolder>(R.layout.item_opml) {

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        DataBindingUtil.bind<ItemOpmlBinding>(viewHolder.itemView)
    }

    override fun convert(holder: BaseViewHolder, item: RssOpmlInfo) {
        if (item == null) {
            return
        }
        val binding = holder.getBinding<ItemOpmlBinding>()
        binding?.cbState?.setOnCheckedChangeListener { buttonView, isChecked ->
            item.state = isChecked
        }
        binding?.item = item
        binding?.executePendingBindings()
    }

    private var onCollectListener: OnCollectListener? = null
    fun setOnCollectListener(l: OnCollectListener) {
        onCollectListener = l
    }

    interface OnCollectListener {
        fun collect(item: RssItem)
    }

    private var onLaterListener: OnLaterListener? = null
    fun setOnLaterListener(l: OnLaterListener) {
        onLaterListener = l
    }

    interface OnLaterListener {
        fun later(item: RssItem)
    }
}