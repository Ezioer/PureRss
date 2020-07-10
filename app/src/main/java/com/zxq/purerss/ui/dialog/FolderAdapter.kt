package com.zxq.purerss.ui.dialog

import android.view.View
import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zxq.purerss.R
import com.zxq.purerss.data.entity.RssItem
import com.zxq.purerss.data.entity.table.RSSFeedEntity
import com.zxq.purerss.data.entity.table.RSSFolderEntity
import com.zxq.purerss.databinding.ItemFeedListBinding
import com.zxq.purerss.databinding.ItemFolderListBinding
import com.zxq.purerss.listener.FolderClickListener
import com.zxq.purerss.widget.SwipeMenuLayout

/**
 *  created by xiaoqing.zhou
 *  on 2020/6/28
 *  fun
 */
class FolderAdapter(private val onClick: FolderClickListener) :
    BaseQuickAdapter<RSSFolderEntity, BaseViewHolder>(R.layout.item_folder_list) {

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        DataBindingUtil.bind<ItemFolderListBinding>(viewHolder.itemView)
    }

    override fun convert(holder: BaseViewHolder, item: RSSFolderEntity) {
        if (item == null) {
            return
        }
        val binding = holder.getBinding<ItemFolderListBinding>()
        binding?.item = item
        binding?.clickHandle = onClick
        binding?.executePendingBindings()
    }
}