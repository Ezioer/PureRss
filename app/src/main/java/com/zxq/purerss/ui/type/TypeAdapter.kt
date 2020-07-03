package com.zxq.purerss.ui.type

import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zxq.purerss.R
import com.zxq.purerss.data.entity.table.RSSItemEntity
import com.zxq.purerss.databinding.ItemTypeListBinding
import com.zxq.purerss.listener.ItemTypeClickListener
import com.zxq.purerss.widget.SwipeMenuLayout

/**
 *  created by xiaoqing.zhou
 *  on 2020/6/28
 *  fun
 */
class TypeAdapter(private val onClick: ItemTypeClickListener, private var slideDir: Boolean) :
    BaseQuickAdapter<RSSItemEntity, BaseViewHolder>(R.layout.item_type_list) {

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        DataBindingUtil.bind<ItemTypeListBinding>(viewHolder.itemView)
    }

    override fun convert(holder: BaseViewHolder, item: RSSItemEntity) {
        if (item == null) {
            return
        }
        val binding = holder.getBinding<ItemTypeListBinding>()
        binding?.remove?.setOnClickListener {
            (binding.root as SwipeMenuLayout).smoothClose()
            notifyItemRemoved(holder.adapterPosition)
            listener?.onRemove(item)
        }
        binding?.swipe?.setLeftSwipe(slideDir)
        binding?.item = item
        binding?.clickHandle = onClick
        binding?.executePendingBindings()
    }


    private var listener: OnRemoveListener? = null
    fun setOnRemoveListener(listener: OnRemoveListener) {
        this.listener = listener
    }

    interface OnRemoveListener {
        fun onRemove(item: RSSItemEntity)
    }
}