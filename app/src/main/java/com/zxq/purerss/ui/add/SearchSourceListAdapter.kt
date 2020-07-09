package com.zxq.purerss.ui.add

import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zxq.purerss.R
import com.zxq.purerss.data.entity.table.RSSSourceEntity
import com.zxq.purerss.databinding.ItemSearchSourceListBinding

/**
 *  created by xiaoqing.zhou
 *  on 2020/6/28
 *  fun
 */
class SearchSourceListAdapter(
) :
    BaseQuickAdapter<RSSSourceEntity, BaseViewHolder>(R.layout.item_search_source_list) {

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        DataBindingUtil.bind<ItemSearchSourceListBinding>(viewHolder.itemView)
    }

    override fun convert(holder: BaseViewHolder, item: RSSSourceEntity) {
        if (item == null) {
            return
        }
        val binding = holder.getBinding<ItemSearchSourceListBinding>()

        binding?.item = item
        binding?.executePendingBindings()
    }
}