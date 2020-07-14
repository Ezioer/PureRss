package com.zxq.purerss.ui.mainpage

import android.view.View
import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zxq.purerss.R
import com.zxq.purerss.data.entity.RssItem
import com.zxq.purerss.data.entity.table.RSSFeedEntity
import com.zxq.purerss.databinding.ItemFeedListBinding
import com.zxq.purerss.widget.SwipeMenuLayout

/**
 *  created by xiaoqing.zhou
 *  on 2020/6/28
 *  fun
 */
class MainPageAdapter(private val onClick: FeedClick, private var slideDir: Boolean) :
    BaseQuickAdapter<RSSFeedEntity, BaseViewHolder>(R.layout.item_feed_list) {

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        DataBindingUtil.bind<ItemFeedListBinding>(viewHolder.itemView)
    }

    override fun convert(holder: BaseViewHolder, item: RSSFeedEntity) {
        if (item == null) {
            return
        }
        val binding = holder.getBinding<ItemFeedListBinding>()
        binding?.delete?.setOnClickListener {
            (binding.root as SwipeMenuLayout).smoothClose()
            notifyItemRemoved(holder.adapterPosition)
            onDeleteListener?.delete(item)
        }

        binding?.edit?.setOnClickListener {
            (binding.root as SwipeMenuLayout).smoothClose()
            onEditListener?.edit(item)
        }
        binding?.swipe?.setLeftSwipe(slideDir)
        binding?.item = item
        binding?.clickHandle = onClick
        binding?.executePendingBindings()
    }

    interface FeedClick {
        fun onClick(view: View, rss: RSSFeedEntity)
    }

    private var onDeleteListener: OnDeleteListener? = null
    fun setOnDeleteListener(l: OnDeleteListener) {
        onDeleteListener = l
    }

    interface OnDeleteListener {
        fun delete(item: RSSFeedEntity)
    }


    interface OnEditListener {
        fun edit(item: RSSFeedEntity)
    }

    private var onEditListener: OnEditListener? = null
    fun setOnEditListener(listener: OnEditListener) {
        onEditListener = listener
    }


}