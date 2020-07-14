package com.zxq.purerss.ui.dialog

import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zxq.purerss.R
import com.zxq.purerss.data.entity.table.RSSFolderEntity
import com.zxq.purerss.databinding.ItemFolderDialogListBinding
import com.zxq.purerss.listener.FolderClickListener

/**
 *  created by xiaoqing.zhou
 *  on 2020/6/28
 *  fun
 */
class EditFeedsAdapter(private val onClick: FolderClickListener) :
    BaseQuickAdapter<RSSFolderEntity, BaseViewHolder>(R.layout.item_folder_dialog_list) {

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        DataBindingUtil.bind<ItemFolderDialogListBinding>(viewHolder.itemView)
    }

    override fun convert(holder: BaseViewHolder, item: RSSFolderEntity) {
        if (item == null) {
            return
        }
        val binding = holder.getBinding<ItemFolderDialogListBinding>()
        binding?.item = item
        binding?.clickHandle = onClick
        binding?.executePendingBindings()
    }
}