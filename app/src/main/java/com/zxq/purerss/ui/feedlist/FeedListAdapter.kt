package com.zxq.purerss.ui.feedlist

import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zxq.purerss.R
import com.zxq.purerss.data.entity.RssItem
import com.zxq.purerss.databinding.ItemContentListBinding
import com.zxq.purerss.listener.ItemClickListener
import com.zxq.purerss.widget.SwipeMenuLayout

/**
 *  created by xiaoqing.zhou
 *  on 2020/6/28
 *  fun
 */
class FeedListAdapter(private val onClick: ItemClickListener, private var slideDir: Boolean) :
    BaseQuickAdapter<RssItem, BaseViewHolder>(R.layout.item_content_list) {

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        DataBindingUtil.bind<ItemContentListBinding>(viewHolder.itemView)
    }

    override fun convert(holder: BaseViewHolder, item: RssItem) {
        if (item == null) {
            return
        }
        val binding = holder.getBinding<ItemContentListBinding>()
        binding?.collect?.setOnClickListener {
            (binding.root as SwipeMenuLayout).smoothClose()
            onCollectListener?.collect(item)
        }
        binding?.later?.setOnClickListener {
            (binding.root as SwipeMenuLayout).smoothClose()
            onLaterListener?.later(item)
        }
        binding?.swipe?.setLeftSwipe(slideDir)
        binding?.item = item
        binding?.clickHandle = onClick
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