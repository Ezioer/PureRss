package com.zxq.purerss.ui.friends

import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zxq.purerss.R
import com.zxq.purerss.data.entity.ImageItemInfo
import com.zxq.purerss.databinding.ItemStatuspicBinding
import com.zxq.purerss.utils.PixelUtil

/**
 *  created by xiaoqing.zhou
 *  on 2020/6/28
 *  fun
 */
class StatusPicAdapter() :
    BaseQuickAdapter<ImageItemInfo, BaseViewHolder>(R.layout.item_statuspic) {

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        DataBindingUtil.bind<ItemStatuspicBinding>(viewHolder.itemView)
    }

    override fun convert(holder: BaseViewHolder, item: ImageItemInfo) {
        val binding = holder.getBinding<ItemStatuspicBinding>()
        val layoutParams = binding!!.rlRoot.layoutParams
        layoutParams.width =
            (PixelUtil.getScreenWidth(context) - PixelUtil.dp2px(context, 90f) - 20) / 3
        layoutParams.height =
            (PixelUtil.getScreenWidth(context) - PixelUtil.dp2px(context, 90f) - 20) / 3
        binding!!.rlRoot.layoutParams = layoutParams
        if (item != null) {
            Glide.with(binding!!.root).load(item.uri).override(
                (PixelUtil.getScreenWidth(context) - PixelUtil.dp2px(
                    context, 90f
                ) - 20) / 3
            ).into(binding.ivPic);
        }
        binding?.executePendingBindings()
    }
}