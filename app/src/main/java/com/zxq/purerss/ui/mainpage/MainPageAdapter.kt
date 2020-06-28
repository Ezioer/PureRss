package com.zxq.purerss.ui.mainpage

import android.view.View
import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zxq.purerss.R
import com.zxq.purerss.data.entity.table.RSSFeedEntity
import com.zxq.purerss.databinding.ItemFeedListBinding

/**
 *  created by xiaoqing.zhou
 *  on 2020/6/28
 *  fun
 */
class MainPageAdapter(private val onClick: FeedClick): BaseQuickAdapter<RSSFeedEntity,BaseViewHolder>(R.layout.item_feed_list) {

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        DataBindingUtil.bind<ItemFeedListBinding>(viewHolder.itemView)
    }
    override fun convert(holder: BaseViewHolder, item: RSSFeedEntity) {
        if (item == null){
            return
        }
        val binding = holder.getBinding<ItemFeedListBinding>()
        binding?.item = item
        binding?.clickHandle = onClick
        binding?.executePendingBindings()
    }

    interface FeedClick {
        fun onClick(view: View, rss: RSSFeedEntity)
    }
}