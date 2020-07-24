package com.zxq.purerss.ui.dialog

import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.material.chip.Chip
import com.zxq.purerss.R
import com.zxq.purerss.data.entity.table.RSSFolderEntity
import com.zxq.purerss.databinding.ItemFolderDialogListBinding

/**
 *  created by xiaoqing.zhou
 *  on 2020/6/28
 *  fun
 */
class EditFeedsAdapter(private var id: Long) :
    BaseQuickAdapter<RSSFolderEntity, BaseViewHolder>(R.layout.item_folder_dialog_list) {

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        DataBindingUtil.bind<ItemFolderDialogListBinding>(viewHolder.itemView)
    }

    override fun convert(holder: BaseViewHolder, item: RSSFolderEntity) {
        if (item == null) {
            return
        }
        val binding = holder.getBinding<ItemFolderDialogListBinding>()
        if (item.folderId == id) {
            binding?.chipFolder?.isChecked = true
        }
        binding?.chipFolder?.setOnClickListener { buttonView ->
            for (i in 0 until this.data.size) {
                if (i == holder.position) {
                    EditFeedsDialog.parentId = item.folderId
                }
                (this.getViewByPosition(i, R.id.chip_folder) as Chip).isChecked =
                    i == holder.position
            }
        }
        binding?.item = item
        binding?.executePendingBindings()
    }
}