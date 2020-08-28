package com.zxq.purerss.ui.dialog

import android.content.pm.ShortcutInfo
import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.DraggableModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zxq.purerss.R
import com.zxq.purerss.data.entity.ShortCutsInfo
import com.zxq.purerss.databinding.ItemAppShortcutsBinding

/**
 *  created by xiaoqing.zhou
 *  on 2020/6/28
 *  fun
 */
class ShortcutsAdapter() :
    BaseQuickAdapter<ShortcutInfo, BaseViewHolder>(R.layout.item_app_shortcuts), DraggableModule {

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        DataBindingUtil.bind<ItemAppShortcutsBinding>(viewHolder.itemView)
    }

    override fun convert(holder: BaseViewHolder, item: ShortcutInfo) {
        if (item == null) {
            return
        }
        val binding = holder.getBinding<ItemAppShortcutsBinding>()
        binding?.item = item
        binding?.executePendingBindings()
    }
}