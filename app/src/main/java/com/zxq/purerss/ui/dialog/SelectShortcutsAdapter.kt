package com.zxq.purerss.ui.dialog

import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zxq.purerss.R
import com.zxq.purerss.data.entity.ShortCutsInfo
import com.zxq.purerss.data.entity.table.RSSFeedEntity
import com.zxq.purerss.databinding.ItemAppShortcutsBinding
import com.zxq.purerss.databinding.ItemSelectshortcutsBinding

/**
 *  created by xiaoqing.zhou
 *  on 2020/6/28
 *  fun
 */
class SelectShortcutsAdapter() :
    BaseQuickAdapter<RSSFeedEntity, BaseViewHolder>(R.layout.item_selectshortcuts) {

    private var count = 0
    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        DataBindingUtil.bind<ItemSelectshortcutsBinding>(viewHolder.itemView)
    }

    override fun convert(holder: BaseViewHolder, item: RSSFeedEntity) {
        if (item == null) {
            return
        }
        val binding = holder.getBinding<ItemSelectshortcutsBinding>()
        binding?.cbState?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked && count < 4) {
                item.state = isChecked
                count++
            }
            if (!isChecked) {
                item.state = isChecked
                count--
            }
        }

        binding?.item = item
        binding?.executePendingBindings()
    }
}