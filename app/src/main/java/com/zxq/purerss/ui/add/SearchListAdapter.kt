package com.zxq.purerss.ui.add

import android.view.View
import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zxq.purerss.R
import com.zxq.purerss.data.entity.RssItem
import com.zxq.purerss.data.entity.table.RSSItemEntity
import com.zxq.purerss.databinding.ItemContentListBinding
import com.zxq.purerss.databinding.ItemSearchListBinding
import com.zxq.purerss.listener.ItemClickListener
import com.zxq.purerss.listener.ItemSearchClickListener
import com.zxq.purerss.widget.SwipeMenuLayout

/**
 *  created by xiaoqing.zhou
 *  on 2020/6/28
 *  fun
 */
class SearchListAdapter(
    private val onClick: ItemSearchClickListener,
    private var slideDir: Boolean
) :
    BaseQuickAdapter<RssItem, BaseViewHolder>(R.layout.item_search_list) {

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        DataBindingUtil.bind<ItemSearchListBinding>(viewHolder.itemView)
    }

    override fun convert(holder: BaseViewHolder, item: RssItem) {
        if (item == null) {
            return
        }
        val binding = holder.getBinding<ItemSearchListBinding>()

        binding?.ivReddot?.visibility = if (item.itemRead == 0) View.VISIBLE else View.GONE
        binding?.item = item
        binding?.clickHandle = onClick
        binding?.executePendingBindings()
    }
}