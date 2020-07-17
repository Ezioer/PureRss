package com.zxq.purerss.ui.managefolder

import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zxq.purerss.R
import com.zxq.purerss.data.entity.table.RSSFolderEntity
import com.zxq.purerss.databinding.ItemFolderListBinding
import com.zxq.purerss.databinding.ItemManageFolderBinding
import com.zxq.purerss.listener.FolderClickListener

/**
 *  created by xiaoqing.zhou
 *  on 2020/6/28
 *  fun
 */
class ManageFolderAdapter() :
    BaseQuickAdapter<RSSFolderEntity, BaseViewHolder>(R.layout.item_manage_folder) {

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        DataBindingUtil.bind<ItemManageFolderBinding>(viewHolder.itemView)
    }

    override fun convert(holder: BaseViewHolder, item: RSSFolderEntity) {
        if (item == null) {
            return
        }
        val binding = holder.getBinding<ItemManageFolderBinding>()
        addChildClickViewIds(R.id.iv_edit)
        addChildClickViewIds(R.id.iv_delete_folder)
        binding?.item = item
        binding?.executePendingBindings()
    }
}