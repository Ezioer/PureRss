package com.zxq.purerss.ui.feedlist

import android.view.View
import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zxq.purerss.R
import com.zxq.purerss.data.entity.RssItem
import com.zxq.purerss.databinding.ItemContentListBinding

/**
 *  created by xiaoqing.zhou
 *  on 2020/6/28
 *  fun
 */
class FeedListAdapter(private val onClick: FeedItemClick): BaseQuickAdapter<RssItem,BaseViewHolder>(R.layout.item_content_list) {

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        DataBindingUtil.bind<ItemContentListBinding>(viewHolder.itemView)
    }
    override fun convert(holder: BaseViewHolder, item: RssItem) {
        if (item == null){
            return
        }
        val binding = holder.getBinding<ItemContentListBinding>()
        binding?.item = item
        binding?.clickHandle = onClick
        binding?.executePendingBindings()
    }

    interface FeedItemClick {
        fun onClick(view: View, rss: RssItem)
    }
}